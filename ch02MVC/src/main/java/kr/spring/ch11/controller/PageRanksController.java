package kr.spring.ch11.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageRanksController {
	@RequestMapping("/pageRanksExcel.do")
	public ModelAndView handleRequest() {
		List<PageRank> pageRanks = new ArrayList<PageRank>();
		pageRanks.add(new PageRank(1,"/bbs/m/list.do"));
		pageRanks.add(new PageRank(2,"/bbs/m/detail.do"));
		pageRanks.add(new PageRank(3,"/bbs/m/write.do"));
								//뷰이름       속성명       속성값
		return new ModelAndView("pageRanks","pageRanks",pageRanks);
	}
}
