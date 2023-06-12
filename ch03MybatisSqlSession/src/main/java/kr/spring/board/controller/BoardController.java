package kr.spring.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.spring.board.service.BoardService;
import kr.spring.board.vo.BoardVO;
import kr.spring.util.PagingUtil;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	//자바빈 초기화
	@ModelAttribute
	public BoardVO initCommand(){
		return new BoardVO();
	}
	//목록
	@RequestMapping("/list.do")
	public ModelAndView getList(@RequestParam(value="pageNum",defaultValue="1") int currentPage) {
		
		//총 레코드 수
		int count = boardService.getBoardCount();
		
		//페이지처리
		PagingUtil page = new PagingUtil(currentPage, count, 10, 10, "list.do");
		
		//목록호출
		List<BoardVO> list = null;
		if(count>0) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("start", page.getStartRow());
			map.put("end", page.getEndRow());
			
			list = boardService.getBoardList(map);
		}
		
		ModelAndView mav = new ModelAndView();
		//뷰이름 설정
		mav.setViewName("selectList");
		//데이터 저장
		mav.addObject("count",count);
		mav.addObject("list",list);
		mav.addObject("page",page.getPage());
		return mav;
	}
	
	//글쓰기 폼 호출
	@GetMapping("/insert.do")
	public String form() {
		return "insertForm";
	}
	
	//글쓰기 처리
	@PostMapping("/insert.do")
	public String submit(@Valid BoardVO vo,BindingResult result) {
		//유효성 체크 결과 오류가 있으면 폼 호출
		if(result.hasErrors()) {
			return form();
		}
		
		//글쓰기
		boardService.insertBoard(vo);
		return "redirect:/list.do";
	}
	
	//글상세
	@RequestMapping("/detail.do")
	public ModelAndView detail(@RequestParam int num) {
		BoardVO board = boardService.getBoard(num);
		return new ModelAndView("selectDetail","board",board);
	}
	
	//수정 폼 호출
	@GetMapping("/update.do")
	public String formUpdate(@RequestParam int num, Model model) {/*Model이 데이터 세팅 해줌*/
		model.addAttribute("boardVO",boardService.getBoard(num));
		
		return "updateForm";
	}
	
	//글수정처리
	@PostMapping("/update.do")
	public String submitUpdate(@Valid BoardVO vo,BindingResult result) {
		//유효성 체크 결과 오류가 있으면 폼 호출
		if(result.hasErrors()) {
			return "updateForm";
		}
		//비밀번호 일치 여부 체크
		//DB에 저장된 비밀번호 구하기
		BoardVO db_board = boardService.getBoard(vo.getNum());
		
		if(!db_board.getPasswd().equals(vo.getPasswd())) {
			//비밀번호 불일치
			result.rejectValue("passwd", "invalidPassword");
			return "updateForm";
		}
		//글수정
		boardService.updateBoard(vo);
		return "redirect:/list.do";
	}
	
	//글삭제 폼 호출
	@GetMapping("/delete.do")
	public String formDelete(BoardVO vo) {
		
		return "deleteForm";
	} 
	
	//글삭제 처리
	@PostMapping("/delete.do")
	public String submitDelete(@Valid BoardVO vo,BindingResult result) {
		//유효성 체크 결과 오류가 있으면 폼을 호출
		//VO의 나머지(제목,작성자,내용)는 무시하고 비밀번호가 전송되었는지 여부만 체크
		if(result.hasFieldErrors("passwd")) {
			return "deleteForm";
		}
		//비밀번호 일치 여부 체크
		BoardVO db_board = boardService.getBoard(vo.getNum());
		if(!db_board.getPasswd().equals(vo.getPasswd())) {
			//비밀번호 불일치
			result.rejectValue("passwd", "invalidPassword");
			return "deleteForm";
		}
		
		//글 삭제
		boardService.deleteBoard(vo.getNum());
		
		return "redirect:/list.do";
	}
}
