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
	
	
	//Re_Step����Re_level ��������
	public int getBoard(int Re_Step) {
		//DB���� Re_Step�� �������� Re_level��������
		Object res = sqlSession.selectOne("getBoard_d",Re_Step);
		
		//DB�� Re_step���� �˻��� ���� ��� Null�� ��ȯ �ȴٸ� Re_level�� 0����
		if(res==null) {
			return 0;
		}
		//DB�� Re_Step���� �˻��� ���� �ִٸ� �ش��ϴ� ���� int�� ��ȯ.
		else {
			return Integer.parseInt(String.valueOf(res));
		}
	}
	//Re_step���� �Խñ� DTO ��������
	public BoardDTO getRBoard(int re_step) {
		return sqlSession.selectOne("getRBoard",re_step);
	}
	
	public int deleteBoard(int num,String passwd) {
		BoardDTO dto = getBoard(num,"delete");
		if(passwd.equals(dto.getPasswd())) {		//passwd��ġ ����
			int DRe_step = dto.getRe_step();		//������ �Խñ��� Re_step ��ȯ
			int DRe_level = dto.getRe_level();		//������ �Խñ��� Re_level�� ��ȯ
			BoardDTO dto2 = getRBoard(DRe_step+1);
			int NRe_level = (dto2==null)? 0:dto2.getRe_level();
			
			ArrayList<Integer> d_list = new ArrayList<Integer>();	//������ �Խñ� ��ȣ�� ���� ����Ʈ
			int co=0;												// ī��Ʈ.
			//����� ���� �� �Խñ��� ���
			if(DRe_level==0 && NRe_level==0) { 
				return sqlSession.delete("deleteBoard",num);
			}
			
			//������ ����� ������ ���
			else if(NRe_level==0 || dto2==null) {

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
			
			else { //�߰� ����� ������ ���
				int DoRe_step = DRe_step;
				while(true) {
					DoRe_step --;
					co++;
					BoardDTO dto3 = getRBoard(DoRe_step);
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





