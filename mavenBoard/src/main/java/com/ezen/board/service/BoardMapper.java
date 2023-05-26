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
	public int getBoard(int num) {
		return sqlSession.selectOne("getBoard_d",num);
		
	}
	
	public int detDBoard(int re_step) {
		return sqlSession.selectOne("getDBoard",re_step);
	}
	
	public int deleteBoard(int num, String passwd) {
		
		BoardDTO dto = getBoard(num, "password");
		if (dto.getPasswd().equals(passwd)) {
			int d_level = dto.getRe_level();
			int n_level = getBoard(dto.getRe_step()+1);
			System.out.println(n_level);
			if(n_level == d_level+1) {
				sqlSession.update("deleteBoard_d", num);
			}else if(n_level ==0 && d_level!=0 ){
				int d_step = dto.getRe_step();
				for(int i=d_level;i>=0; i--) {
					int d_num = getBoard(d_step);
					sqlSession.delete("deleteBoard", d_num);
					d_step--;
				}
				sqlSession.delete("deleteBoard", num);
			}
			return 1;
		}else {
			return -1;
		}
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





