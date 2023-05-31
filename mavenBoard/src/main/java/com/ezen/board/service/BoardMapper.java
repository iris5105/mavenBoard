package com.ezen.board.service;


import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.board.dto.BoardDTO;


@Service
public class BoardMapper {
	
	@Autowired
	private SqlSession sqlSession;
	
	
	public List<BoardDTO> listBoard(int startRow, int endRow){		
		Map<String, Integer> map = new Hashtable<String, Integer>();
		map.put("start", startRow);
		map.put("end", endRow);
		return sqlSession.selectList("listBoard", map);
	}	
	public int insertBoard(BoardDTO dto) {
			String sql = null;
			if (dto.getNum() == 0) {
				sql = "update filespring set re_step = re_step + 1";
			}else {
				sql = "update filespring set re_step = re_step + 1 where re_step > "+dto.getRe_step();
				dto.setRe_step(dto.getRe_step()+1);
				dto.setRe_level(dto.getRe_level()+1);
			}
			Map<String, String> map = new Hashtable<String, String>();
			map.put("sql", sql);
			sqlSession.update("plusRe_step", map);
			return sqlSession.insert("insertBoard", dto);
	}
	
	public BoardDTO getBoard(int num, String mode) {
			if (mode.equals("content")) {
				sqlSession.update("plusReadcount", num);
			}
			return sqlSession.selectOne("getBoard", num);
	}
	
	
	//Re_Step으로Re_level 가져오기
	public int getBoard(int Re_Step) {
		//DB에서 Re_Step을 기준으로 Re_level가져오기
		Object res = sqlSession.selectOne("getBoard_d",Re_Step);
		
		//DB에 Re_step으로 검색한 값이 없어서 Null로 반환 된다면 Re_level을 0으로
		if(res==null) {
			return 0;
		}
		//DB에 Re_Step으로 검색한 값이 있다면 해당하는 값을 int로 반환.
		else {
			return Integer.parseInt(String.valueOf(res));
		}
	}
	//Re_step으로 게시글 DTO 가져오기
	public BoardDTO getRBoard(int re_step) {
		return sqlSession.selectOne("getRBoard",re_step);
	}
	
//	public int deleteBoard(int num, String passwd) {
//		BoardDTO dto = getBoard(num, "password");
//		if (dto.getPasswd().equals(passwd)) {
//			int d_level = dto.getRe_level();
//			BoardDTO dto2 = getRBoard(dto.getRE_step()+1);
//			int n_level = (dto2==null)? 0:dto2.getRe_level();
//			if(n_level == d_level+1 ) {
//				sqlSession.update("deleteBoard_d", num);
//			}else if(n_level ==0 && d_level!=0 ){
//				int d_step = dto.getRe_step();
//				for(int i=d_level;i>=0; i--) {
//					System.out.println("d_step="+d_step);
//					int d_num = setDBoard(d_step);
//					sqlSession.delete("deleteBoard", d_num);
//					d_step--;
//				}
//				sqlSession.delete("deleteBoard", num);
//			}
//			return 1;
//		}else {
//			return -1;
//		}
//	}
	
//	public int deleteBoard(int num, String passwd) {
//	    BoardDTO dto = getBoard(num, "password");
//	    if (dto.getPasswd().equals(passwd)) {
//	        int d_level = dto.getRe_level();
//	        int n_level = getBoard(dto.getRe_step() + 1);
//	        System.out.println("n_level=" + n_level);
//	        if (n_level == d_level + 1) {
//	            sqlSession.update("deleteBoard_d", num);
//	        } else if (n_level == 0 && d_level != 0) {
//	            int d_step = dto.getRe_step();
//	            while (n_level == 0 && d_level != 0) {
//	                int d_num = getDBoard(d_step);
//	                BoardDTO ddto = sqlSession.selectOne("getBoard", d_num);
//	                String filename = ddto.getFilename();
//	                if (filename.equals("deleted")) {
//	                    sqlSession.delete("deleteBoard", d_num);
//	                } else {
//	                    break;
//	                }
//	                d_step--;
//	                d_level--;
//	                n_level = getBoard(d_step - 1);
//	            }
//	            sqlSession.delete("deleteBoard", num);
//	        }
//	        return 1;
//	    } else {
//	        return -1;
//	    }
//	}
	public int deleteBoard(int num,String passwd) {
		BoardDTO dto = getBoard(num,"delete");
		if(passwd.equals(dto.getPasswd())) {		//passwd일치 여부
			int DRe_step = dto.getRe_step();		//삭제할 게시글의 Re_step 반환
			int DRe_level = dto.getRe_level();		//삭제할 게시글의 Re_level을 반환
			BoardDTO dto2 = getRBoard(DRe_step+1);
			int NRe_level = (dto2==null)? 0:dto2.getRe_level();
			
			ArrayList<Integer> d_list = new ArrayList<Integer>();	//삭제할 게시글 번호를 담을 리스트
			int co=0;												// 카운트.
			//답글이 없는 원 게시글일 경우
			if(DRe_level==0 && NRe_level==0) { 
				return sqlSession.delete("deleteBoard",num);
			}
			
			//마지막 답글을 삭제할 경우
			else if(NRe_level==0 || dto2==null) {
/*				ArrayList<Integer> d_list = new ArrayList<Integer>();
				int co=0;*/
				while(true) {
					DRe_step --;
					co++;
					BoardDTO dto3 = getRBoard(DRe_step);
					if(dto3==null) {
						break;
					}
					else if(dto3.getFilename().equals("deleted")) {
						d_list.add(dto3.getNum());
						if(dto3.getRe_level()==0) {
							break;
						}
					}
				}
				if(d_list.size()==co) {
					d_list.add(num);
					for(int i=0; i<d_list.size(); i++) {
						int dnum= d_list.get(i);
						sqlSession.delete("deleteBoard",dnum);
					}
					return 1;
				}else{
					return sqlSession.update("deleteBoard_d",num);
				}
			}
			
			else { //중간 답글을 삭제할 경우
				while(true) {
					DRe_step --;
					co++;
					BoardDTO dto3 = getRBoard(DRe_step);
					if(dto3==null) {
						break;
					}
					else if(dto3.getFilename().equals("deleted")) {
						d_list.add(dto3.getNum());
						if(dto3.getRe_level()==0) {
							break;
						}
					}
				}
				if(co==d_list.size()) {
					while(true) {
						DRe_step ++;
						co++;
						BoardDTO dto3 = getRBoard(DRe_step);
						if(dto3==null) {
							break;
						}
						else if(dto3.getFilename().equals("deleted")) {
							d_list.add(dto3.getNum());
							if(dto3.getRe_level()==0) {
								break;
							}
						}
					}
				}
				if(d_list.size()==co) {
					d_list.add(num);
					for(int i=0; i<d_list.size(); i++) {
						int dnum= d_list.get(i);
						sqlSession.delete("deleteBoard",dnum);
					}
					return 1;
				}else{
					return sqlSession.update("deleteBoard_d",num);
				}
				
			}
		}
		return -1;
	}
	
	public int updateBoard(BoardDTO dto) {
		BoardDTO dto2 = getBoard(dto.getNum(), "password");
		if (dto.getPasswd().equals(dto2.getPasswd())) {
			return sqlSession.update("updateBoard", dto);
		}else {
			return -1;
		}
	}
	
	public int getCount() {

		return sqlSession.selectOne("getCount");

	}
}





