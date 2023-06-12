package kr.spring.ch08.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import kr.spring.ch08.validator.SubmitReportValidator;
import kr.spring.ch08.vo.SubmitReportVO;

@Controller
public class SubmitReportController {
	//파일 업로드 경로 읽기
	@Value("${file_path}")
	private String path;
	
	//자바빈 초기화
	@ModelAttribute("report")
	public SubmitReportVO initCommand() {
		return new SubmitReportVO();
	}
	
	//폼 호출
	@GetMapping("/report/submitReport.do")
	public String form() {
		return "report/submitReportForm";
	}
	
	//전송된 데이터 처리
	@PostMapping("/report/submitReport.do")
	public String submit(@ModelAttribute("report") SubmitReportVO vo, BindingResult result)throws IOException {
		//유효성 체크
		new SubmitReportValidator().validate(vo, result);
		//유효성 체크 결과 오류가 있으면 폼 호출
		if(result.hasErrors()) {
			return form();
		}
		//정상적으로 데이터가 전송
		//파일이 저장될 절대 경로를 생성
		File file = new File(path + "/" + vo.getReportFile().getOriginalFilename());
		//파일이 생성될 경로에 파일을 복사함
		vo.getReportFile().transferTo(file);
		
		System.out.println("주제 : " + vo.getSubject());
		System.out.println("업로드한 파일 : " + vo.getReportFile().getOriginalFilename());
		System.out.println("파일 용량 : " + vo.getReportFile().getSize());
		
		return "report/submittedReport";
	}
}
