package kr.spring.ch02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {
	/*
	 * @RequestParam 어노테이션은 HTTP 요청 파라미터를 메서드의 파라미터로 전달
	 * [형식]
	 * 1. @RequestParam(파라미터네임) 메서드의 인자(파라미터)
	 * 2. 요청파라미터명과 호출 메서드의 인자명이 같으면 @RequestParam 메서드의 인자명
	 * 3. @RequestParam 생략 가능(요청파라미터명과 인자명을 동일하게 표시할 경우)
	 *    query를 필수적으로 사용하지 않아도 오류가 발생하지 않음
	 * [주의]
	 * 메서드에 @RequestParam을 명시하면 query는 필수적으로 전달해야함. 미전달시 오류 발생
	 * 만약 query를 필수적으로 사용하지 않는다면 @RequestParam(value="query",required=false)
	 * 전송되는 데이터가 숫자일 경우, 데이터가 전송되지 않으면 default 값 
	 */
	
	//요청 URL과 실행 메서드 연결
	@RequestMapping("/search/internal.do")
	public ModelAndView searchInternal(String query) {
		System.out.println("query = " + query);
		                       //뷰이름
		return new ModelAndView("search/internal"); 
	}
	
	@RequestMapping("/search/external.do")
	public ModelAndView searchExternal(@RequestParam String query, @RequestParam(value="p",defaultValue="1") int pageNumber) {
		System.out.println("query="+query);
		System.out.println("p="+pageNumber);
		                       //뷰이름
		
		return new ModelAndView("search/external");
	}
}
