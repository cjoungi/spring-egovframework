package kr.spring.ch03.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kr.spring.ch03.service.ArticleService;
import kr.spring.ch03.vo.NewArticleVO;

@Controller
public class NewArticleController {
	@Autowired
	private ArticleService articleService;
	
	//@Autowired 어노테이션을 사용할 경우 setXXX 메서드 생략 가능
	/*
	public void seArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	*/
	
	//요청 URL과 실행 메서드 연결
	//Get 요청이 들어올 때 호출
	@GetMapping("/article/newArticle.do")
	public String form() {
		        //뷰이름
		return "article/newArticleForm";
	}
	
	/*
	 * @ModelAttribute 어노테이션을 이용해서 전송된 데이터를 자바빈에 담기
	 * 1. 자바빈 생성
	 * 2. 전송된 데이터를 자바빈에 저장
	 * 3. View에서 사용할 수 있도록 request에 자바빈 저장
	 * [형식]
	 * 1. @ModelAttribute(속성명) NewArticleVO vo
	 *    지정한 속성명으로 JSP에서 request에 접근해서 자바빈 호출 가능
	 * 2. @ModelAttribute 생략
	 *    호출 메서드에 인자명만 명시
	 *    ex) NewArticleVO vo와 같이 인자명만 명시
	 *    request에 저장되는 속성명은 newArticleVO로 저장됨
	 */
	
	//Post 요청이 들어올 때 호출
	@PostMapping("/article/newArticle.do")
	public String submit(NewArticleVO vo) {
		
		articleService.writeArticle(vo);
		              //뷰이름
		return "article/newArticleSubmitted";
	}
}
