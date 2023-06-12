package kr.spring.ch12.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageReportController{
	@RequestMapping("/pageJsonReport.do")
	//JSON 문자열 생성
	@ResponseBody
	public List<PageRank> jsonReport(){
		List<PageRank> pageRanks = new ArrayList<PageRank>();
		pageRanks.add(new PageRank(1,"/board/list.do"));
		pageRanks.add(new PageRank(2,"/board/detail.do"));
		pageRanks.add(new PageRank(3,"/board/write.do"));
		pageRanks.add(new PageRank(4,"/board/update.do"));
		
		return pageRanks;
	}
}
