package com.ezen.board;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.BindingType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ezen.board.dto.BoardDTO;
import com.ezen.board.service.BoardMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "index";
	}
	@Autowired
	BoardMapper boardMapper;
	
	@RequestMapping(value="/board_list.do")
	public String list(HttpServletRequest req, @RequestParam(required = false)String pageNum) {
		HttpSession session = req.getSession();
		String upPath = session.getServletContext().getRealPath("/resources/img/");
		session.setAttribute("upPath", upPath);
//		session.setAttribute("upPath", "/resources/img/");
		if(pageNum == null) {
			pageNum="1";
		}
		int currentPage = Integer.parseInt(pageNum);
		//한페이지에 노출되는 게시글 수
		int pageSize =10;
		//해당 페이지의 첫 게시글(제일 하위) 번호 ex)
		int startRow = (currentPage-1)*pageSize+1;
		//해당 페이지의 마지막(제일 상위) 게시글  번호
		int endRow = startRow + pageSize-1;
		//DB안의 게시글 수
		int countRow = boardMapper.getCount();
		//위의 식 대로 계산한 endRow가 countRow를 넘어서면 데이터가 없기에 종료점을 countrow로 지정
		if(endRow > countRow) endRow=countRow;
		
		List<BoardDTO> list = boardMapper.listBoard(startRow, endRow);
		//각 페이지의 상위 페이지 번호.
		int num = countRow -(startRow-1);
		req.setAttribute("listBoard", list);
		req.setAttribute("num", num);
		int pageCount = countRow / pageSize + (countRow%pageSize ==0? 0:1);
		//페이지 수 지정, 3페이지를 넘어간다면 첫 목록에 1,2,3이 표시되고 다음이 표시 다음을 누르면 4,5,6이 표시되고 이전이 표시
		int pageBlock =3;
		int startPage = (currentPage-1)/pageBlock * pageBlock +1;
		int endPage = startPage+pageBlock -1;
		if(endPage > pageCount)endPage=pageCount;
		req.setAttribute("pageCount", pageCount);
		req.setAttribute("pageBlock", pageBlock);
		req.setAttribute("startPage", startPage);
		req.setAttribute("endPage", endPage);
		return "/board/list";
	}
	@RequestMapping(value="/write.do")
	public String write() {
		return "/board/write_form";
	}
	@RequestMapping(value="/write.do", method=RequestMethod.POST)
	public String write(HttpServletRequest req,
			@ModelAttribute BoardDTO dto, BindingResult result) {
		System.out.println("dto.num="+dto.getNum());
		System.out.println("dto.Re_step()="+dto.getRe_step());
		System.out.println("dto.Re_level="+dto.getRe_level());
		if(result.hasErrors()) {
			if(dto.getNum()==0) {
			dto.setNum(0);
			dto.setRe_step(0);
			dto.setRe_level(0);
			}else if(dto.getNum()!=0 && dto.getFilename()=="") {
				dto.setFilename(null);
			}
		}
		System.out.println("dto.num="+dto.getNum());
		System.out.println("dto.Re_step()="+dto.getRe_step());
		System.out.println("dto.Re_level="+dto.getRe_level());
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
		MultipartFile file = mr.getFile("filename");
		HttpSession session = req.getSession();
		String upPath = session.getServletContext().getRealPath("/resources/img");
		File target = new File(upPath, file.getOriginalFilename());
		try {
			file.transferTo(target);
		}catch(IOException e) {
			e.printStackTrace();
		}
		String fname= file.getOriginalFilename();
		dto.setFilename(fname);
		dto.setIp(req.getRemoteAddr());
		int res=boardMapper.insertBoard(dto);
		if(res>0) {
			req.setAttribute("msg", "게시글 등록 성공!! 게시글 목록 페이지로 이동합니다.");
			req.setAttribute("url", "board_list.do");
		}else {
			req.setAttribute("msg", "게시글 등록 실패!! 게시글 등록 페이지로 이동합니다.");
			req.setAttribute("url", "write.do");
		}
		
		return "message";
	}
	@RequestMapping(value="content_board.do")
	public String content_board(HttpServletRequest req, @RequestParam int num) {
		HttpSession session = req.getSession();
		BoardDTO dto = boardMapper.getBoard(num,"content");
		req.setAttribute("content", dto);
		req.setAttribute("upPath", session.getAttribute("upPath"));
		return "board/content";
	}
	
	@RequestMapping(value="update.do")
	public String update(HttpServletRequest req, @RequestParam int num) {
		BoardDTO dto = boardMapper.getBoard(num,"update");
		req.setAttribute("getBoard", dto);
		return "board/update_form";
	}
	@RequestMapping(value="update.do", method=RequestMethod.POST)
	public String update(HttpServletRequest req, BoardDTO dto,BindingResult result) {
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
		MultipartFile file = mr.getFile("filename");
		HttpSession session = req.getSession();
		String upPath = session.getServletContext().getRealPath("/resources/img");
		File target = new File(upPath, file.getOriginalFilename());
		try {
			file.transferTo(target);
		}catch(IOException e) {
			e.printStackTrace();
		}
		String fname= file.getOriginalFilename();
		dto.setFilename(fname);
		int res = boardMapper.updateBoard(dto);
		System.out.println(res);
		if(res>0) {
			req.setAttribute("msg", "업데이트 성공 목록으로 돌아갑니다");
			req.setAttribute("url", "board_list.do");

		}else if(res==-1){
			req.setAttribute("msg", "비밀번호가 틀렸습니다");
			req.setAttribute("url", "update.do?num="+dto.getNum());
		}else {
			req.setAttribute("msg", "업데이트 실패 게시글 목록으로 돌아갑니다");
			req.setAttribute("url", "update.do?num="+dto.getNum());
		}
		return "message";
		
	}
	@RequestMapping(value="delete.do")
	public String delete(HttpServletRequest req, @RequestParam int num) {
		req.setAttribute("num", num);
		return "board/delete_form";
	}
	@RequestMapping(value="delete.do", method=RequestMethod.POST)
	public String delete(HttpServletRequest req, @RequestParam int num, @RequestParam String passwd) {
		int res = boardMapper.deleteBoard(num, passwd);
		if(res>0) {
			req.setAttribute("msg", "삭제성공 게시글 목록으로 이동합니다");
			req.setAttribute("url", "board_list.do");
		}else {
			req.setAttribute("msg", "삭제 실패 게시글 목록으로 이동합니다");
			req.setAttribute("url", "board_list.do");
		}
		return "message";
		
	}
}