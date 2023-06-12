package kr.spring.ch06.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import kr.spring.ch06.validator.MemberInfoValidator;
import kr.spring.ch06.vo.MemberInfo;

@Controller
public class CreateAccountController {
	
	//유효성 체크를 할 경우 자바빈(VO) 초기화 필수
	//@ModelAttribute(속성명)
	//자바빈 초기화를 하면 request에 저장됨
	@ModelAttribute("command")
	public MemberInfo initCommand() {
		return new MemberInfo();
	}
	
	@GetMapping("/account/create.do")
	public String form() {
		return "account/creationForm";
	}
	
	@PostMapping("/account/create.do")
	public String submit(@ModelAttribute("command") MemberInfo memberInfo,BindingResult result) {
		//전송된 데이터 유효성 체크
		new MemberInfoValidator().validate(memberInfo,result);
		//유효성 체크 결과 오류가 있으면 오류 정보를 BindingResult 객체에 저장
		if(result.hasErrors()) {
			//체크 결과 하나라도 오류가 있으면 true
			return "account/creationForm";
		}
		
		return "account/created";
	}
	
}
