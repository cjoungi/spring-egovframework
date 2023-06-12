package kr.spring.ch07.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import kr.spring.ch07.service.LoginCheckException;
import kr.spring.ch07.service.LoginService;
import kr.spring.ch07.validator.LoginValidator;
import kr.spring.ch07.vo.LoginVO;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	//자바빈 초기화
	//속성명을 명시하지 않으면 LoginVO를 속성명으로 사용
	//LoginVO -> loginVO를 request에 저장 속성명으로 사용
	@ModelAttribute
	public LoginVO initCommand() {
		return new LoginVO();
	}
	
	@GetMapping("/login/login.do")
	public String form() {
		return "login/form";
	}
	
	@PostMapping("/login/login.do")
	public String submit(LoginVO vo, BindingResult result) {
		//유효성 체크
		new LoginValidator().validate(vo, result);
		//유효성 체크 결과 오류가 있으면 폼 호출
		if(result.hasErrors()) {
			return form();
		}
		try {
			loginService.checkLogin(vo);
			//로그인 성공
			return "redirect:/index.jsp";
		}catch(LoginCheckException e) {
			//로그인 실패
			//에러문구처리
			              //에러코드
			result.reject("invalidIdOrPassword");
			return form();
		}
	}
}
