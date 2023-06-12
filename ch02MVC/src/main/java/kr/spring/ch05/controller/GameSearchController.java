package kr.spring.ch05.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.spring.ch05.service.SearchService;
import kr.spring.ch05.vo.SearchVO;

@Controller
public class GameSearchController {
	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/search/main.do")
	public String main() {
		return "search/main";
	}
	
	@RequestMapping("/search/game.do")
	public ModelAndView search(SearchVO vo) {
		
		String result = searchService.search(vo);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("search/game");//뷰이름
		mav.addObject("searchResult",result);
		
		return mav;
	}
}
