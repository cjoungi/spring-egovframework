package kr.spring.ch09.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import kr.spring.ch09.vo.MemberVO;

@Controller
public class MemberWriteController {
	//자바빈 초기화
	@ModelAttribute("command")
	public MemberVO initCommand() {
		return new MemberVO();
	}
	
	@GetMapping("/member/write.do")
	public String form() {
		return "member/write";
	}
	
	//전송된 데이터 처리
	@PostMapping("/member/write.do")
	//어노테이션을 이용한 유효성 체크시 호출 메서드에서 자바빈을 전달받을 때 @Valid를 명시해야 유효성 체크가 실행됨
	public String submit(@ModelAttribute("command") @Valid MemberVO vo, BindingResult result) {
		//유효성 체크 결과 오류가 발생하면 BindingResult에 오류 정보를 저장
		//유효성 체크 결과 오류가 있으면 폼 호출
		if(result.hasErrors()) {
			return "member/write";
		}
		//유효성 체크 결과 오류가 없을 경우
		return "redirect:index.jsp";
	}
}
