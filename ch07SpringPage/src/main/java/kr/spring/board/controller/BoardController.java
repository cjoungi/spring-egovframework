package kr.spring.board.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.spring.board.service.BoardService;
import kr.spring.board.vo.BoardFavVO;
import kr.spring.board.vo.BoardReplyVO;
import kr.spring.board.vo.BoardVO;
import kr.spring.member.vo.MemberVO;
import kr.spring.util.PagingUtil;
import kr.spring.util.StringUtil;

@Controller
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	private int rowCount=20;
	
	@Autowired
	private BoardService boardService;
	
	//자바빈(VO) 초기화
	@ModelAttribute
	public BoardVO initCommand() {
		return new BoardVO();
	}
	
	//게시판 글 목록
	@RequestMapping("/board/list.do")
	public ModelAndView process(@RequestParam(value="pageNum",defaultValue="1") 
						int currentPage,String keyfield,String keyword) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("keyfield", keyfield);
		map.put("keyword", keyword);
		
		//글의 총개수 또는 검색된 글의 개수
		int count = boardService.selectRowCount(map);
		
		logger.debug("<<count>> : " + count);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(keyfield, keyword, currentPage, count, 20, 10, "list.do");

		List<BoardVO> list = null;
		if(count > 0) {
			map.put("start", page.getStartRow());
			map.put("end", page.getEndRow());
			
			list = boardService.selectList(map);
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("boardList");//타일스의 definition name 값
		mav.addObject("count",count);
		mav.addObject("list",list);
		mav.addObject("page",page.getPage());
		
		return mav;
	}

	//글쓰기
	//등록 폼 호출
	@GetMapping("/board/write.do")
	public String form() {
		return "boardWrite";
	}
	
	//등록 폼에서 전송된 데이터 처리
	@PostMapping("/board/write.do")
	public String submit(@Valid BoardVO boardVO,BindingResult result,HttpServletRequest request,
							RedirectAttributes redirect,HttpSession session) {
		logger.debug("<<게시판 글쓰기>> : " + boardVO);
		logger.debug("<<업로드 파일 용량>> : " + boardVO.getUploadfile().length);
		
		if(boardVO.getUploadfile().length >= 52428800){//5MB
			result.reject("limitUploadSize");
		}
		
		//유효성 체크 결과 오류가 있으면 폼을 호출
		if(result.hasErrors()) {
			return form();
		}
		//회원번호 셋팅
		boardVO.setMem_num(((MemberVO)session.getAttribute("user")).getMem_num());
		//ip셋팅
		boardVO.setIp(request.getRemoteAddr());
		//글쓰기
		boardService.insertBoard(boardVO);
		
		//RedirectAttributes 객체는 리다이렉트 시점에 한번만 사용되는 데이터를 전송
		//브라우저에 데이터를 전송하지만 URL상에 보이지 않는 숨겨진 데이터 형태로 전달
		redirect.addFlashAttribute("result","success");
		
		return "redirect:/board/list.do";
	}
	
	//게시판 글상세
	@RequestMapping("/board/detail.do")
	public ModelAndView process(@RequestParam int board_num) {
		
		logger.debug("<<board_num>> : " + board_num);
		
		//해당 글의 조회수 증가
		boardService.updateHit(board_num);
		
		BoardVO board = boardService.selectBoard(board_num);
		
		//제목에 태그를 허용하지 않음
		board.setTitle(StringUtil.useNoHtml(board.getTitle()));
		
		//내용에 태그를 허용하지 않음
		//CKEditor 사용시 주석 처리
		//board.setContent(StringUtil.useBrNoHtml(board.getContent()));
								//뷰이름       속성명   속성값
		return new ModelAndView("boardView","board",board);
	}
	
	//게시판 글 수정
	//수정 폼 호출
	@GetMapping("/board/update.do")
	public String formUpdate(@RequestParam int board_num,Model model){
		BoardVO boardVO = boardService.selectBoard(board_num);
		model.addAttribute("boardVO",boardVO);
		
		return "boardModify";
	}
	
	//파일삭제
	@RequestMapping("/board/deleteFile.do")
	@ResponseBody
	public Map<String,String> processFile(int board_num,HttpSession session){
		Map<String,String> mapJson = new HashMap<String,String>();
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user==null) {
			mapJson.put("result", "logout");
		}else {
			boardService.deleteFile(board_num);
			mapJson.put("result", "success");
		}
		
		return mapJson;
	}
	
	//전송된 데이터 처리
	@PostMapping("/board/update.do")
	public String submitUpdate(@Valid BoardVO boardVO,BindingResult result,
							HttpServletRequest request,Model model){
		logger.debug("<<글수정>> : " + boardVO);
		logger.debug("<<업로드 파일 욜량>> : " + boardVO.getUploadfile().length);
		
		if(boardVO.getUploadfile().length >= 52428800) {//5MB
			result.reject("limitUploadSize");
		}
		
		//유효성 체크 결과 오류가 있으면 폼을 호출
		if(result.hasErrors()) {
			//title 또는 content가 입력되지 않아서 유효성체크에 걸리면 파일정보를 잃어버리기 때문에
			//폼을 호출할 때 파일정보를 다시 셋팅
			BoardVO vo = boardService.selectBoard(boardVO.getBoard_num());
			boardVO.setFilename(vo.getFilename());
			return "boardModify";
		}
		//ip셋팅
		boardVO.setIp(request.getRemoteAddr());
		
		//글수정
		boardService.updateBoard(boardVO);
		
		//View에 표시할 메시지
		model.addAttribute("message","글수정완료");
		model.addAttribute("url",request.getContextPath()+"/board/detail.do?board_num="+boardVO.getBoard_num());
		
		return "common/resultView";
	}
	
	//게시판 글삭제
	@RequestMapping("/board/delete.do")
	public String submitDelete(@RequestParam int board_num,Model model,HttpServletRequest request) {
		logger.debug("<<게시판 글삭제>> : " + board_num);
		
		//글삭제
		boardService.deleteBoard(board_num);
		
		return "redirect:/board/list.do";
	}
		
	
	//이미지 출력
	@RequestMapping("/board/imageView.do")
	public ModelAndView viewImage(@RequestParam int board_num,@RequestParam int board_type) {
		BoardVO board = boardService.selectBoard(board_num);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("imageView");
		
		if(board_type==1) {//프로필사진
			mav.addObject("imageFile",board.getPhoto());
			mav.addObject("filename",board.getPhoto_name());
		}else if(board_type==2){//업로드 사진
			mav.addObject("imageFile",board.getUploadfile());
			mav.addObject("filename",board.getFilename());
		}
		
		return mav;
	}
	
	
	//파일 다운로드
	@RequestMapping("/board/file.do")
	public ModelAndView download(@RequestParam int board_num) {
		BoardVO board = boardService.selectBoard(board_num);
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("downloadView");
		mav.addObject("downloadFile",board.getUploadfile());
		mav.addObject("filename",board.getFilename());
		
		return mav;
	}
	
	//===============좋아요================//
	//부모글 좋아요 읽기
	@RequestMapping("/board/getFav.do")
	@ResponseBody
	public Map<String,Object> getFav(BoardFavVO fav, HttpSession session) {
		logger.debug("<<게시판 좋아요>> : " + fav);
		
		Map<String,Object> mapJson = new HashMap<String,Object>();
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user==null) {
			mapJson.put("status", "noFav");
		}else {
			//로그인된 아이디 셋팅
			fav.setMem_num(user.getMem_num());
			
			BoardFavVO boardFav = boardService.selectFav(fav);
			if(boardFav!=null) {
				mapJson.put("status", "yesFav");
			}else {
				mapJson.put("status", "noFav");
			}
		}
		mapJson.put("count", boardService.selectFavCount(fav.getBoard_num()));
		
		return mapJson; 
	}
	
	
	//좋아요 등록
	@RequestMapping("/board/writeFav.do")
	@ResponseBody
	public Map<String,Object> writeFav(BoardFavVO fav, HttpSession session){
		logger.debug("<<부모글 좋아요 등록>> : " + fav);
		Map<String,Object> mapJson = new HashMap<String,Object>();
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user==null) {
			mapJson.put("result", "logout");
		}else {
			//로그인된 회원번호 셋팅
			fav.setMem_num(user.getMem_num());
			logger.debug("<<부모글 좋아요 등록>> : " + fav);
			
			BoardFavVO boardFav = boardService.selectFav(fav);
			if(boardFav!=null) {
				//좋아요가 이미 등록되어 있으면 삭제
				boardService.deleteFav(boardFav.getFav_num());
				mapJson.put("result", "success");
				mapJson.put("status", "noFav");
			}else {
				//좋아요가 미등록이면 등록
				boardService.insertFav(fav);
				mapJson.put("result", "success");
				mapJson.put("status", "yesFav");
			}
			mapJson.put("count", boardService.selectFavCount(fav.getBoard_num()));
		}
		return mapJson;
	}
	
	
	//===============댓글================//
	//댓글등록
	@RequestMapping("/board/writeReply.do")
	@ResponseBody
	public Map<String,String> writeReply(BoardReplyVO vo, HttpSession session, HttpServletRequest request){
		logger.debug("<<댓글 등록>> : " + vo);
		
		Map<String,String> mapJson = new HashMap<String,String>();
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user==null) {
			mapJson.put("result", "logout");
		}else {
			//회원번호 등록
			vo.setMem_num(user.getMem_num());
			//ip 등록
			vo.setRe_ip(request.getRemoteAddr());
			//댓글 등록
			boardService.insertReply(vo);
			mapJson.put("result", "success");
		}
		return mapJson;
	}
	
	//댓글목록
	@RequestMapping("/board/listReply.do")
	@ResponseBody
	public Map<String,Object> getReply(@RequestParam(value="pageNum",defaultValue="1") 
	int currentPage, @RequestParam int board_num,HttpSession session){
		logger.debug("<<currentPage>> : " + currentPage);
		logger.debug("<<board_num>> : " + board_num);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("board_num",board_num);
		
		//총 글의 개수
		int count = boardService.selectRowCountReply(map);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(currentPage,count,rowCount,1,null);
		map.put("start", page.getStartRow());
		map.put("end", page.getEndRow());
		
		//목록 데이터 읽기
		List<BoardReplyVO> list = null;
		if(count > 0) {
			list = boardService.selectListReply(map);
		}else {
			list = Collections.emptyList();
		}
		
		Map<String,Object> mapJson = new HashMap<String,Object>();
		mapJson.put("count", count);
		mapJson.put("rowCount", rowCount);
		mapJson.put("list", list);
		
		//로그인한 회원정보 셋팅
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user!=null) {
			mapJson.put("user_num", user.getMem_num());
		}
		return mapJson;
	}
}
