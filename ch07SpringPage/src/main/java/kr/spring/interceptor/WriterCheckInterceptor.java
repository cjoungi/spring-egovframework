package kr.spring.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.spring.board.service.BoardService;
import kr.spring.board.vo.BoardVO;
import kr.spring.member.vo.MemberVO;

public class WriterCheckInterceptor implements HandlerInterceptor{
	private static final Logger logger = LoggerFactory.getLogger(WriterCheckInterceptor.class);
	
	@Autowired
	private BoardService boardService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler)throws Exception {
		//로그인 회원번호 구하기
		HttpSession session = request.getSession();
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		//작성자의 회원번호 구하기
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		BoardVO board = boardService.selectBoard(board_num);
		
		if(user!=null) {
		logger.debug("<<로그인 회원번호>> : " + user.getMem_num());
		logger.debug("<<작성자 회원번호>> : " + board.getMem_num());
		}
		
		if(user==null || user.getMem_num() != board.getMem_num()) {
			logger.debug("<<로그인 회원번호와 작성자 회원번호 불일치>>");
			
			request.setAttribute("accessMsg", "로그인 아이디와 작성자 아이디 불일치");
			request.setAttribute("accessBtn", "게시판 목록");
			request.setAttribute("accessUrl", request.getContextPath()+"/board/list.do");
			
			//포워드 방식으로 화면 호출
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/notice.jsp");
			dispatcher.forward(request, response);
			return false;
		}

		logger.debug("<<로그인 회원번호와 작성자 회원번호 일치>>");
		
		return true;
	}
	
}
