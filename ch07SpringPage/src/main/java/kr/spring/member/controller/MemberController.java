package kr.spring.member.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.util.AuthCheckException;
import kr.spring.util.FileUtil;

@Controller
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	//자바빈 초기화
	@ModelAttribute
	public MemberVO initCommand() {
		return new MemberVO();
	}
	
	//==========회원가입==========//
	//아이디 중복 체크
	@RequestMapping("/member/confirmId.do")
	@ResponseBody
	public Map<String,String> process(@RequestParam String id){
		logger.debug("<<id>> : " + id);
		
		Map<String,String> mapAjax = new HashMap<String,String>();
		MemberVO member = memberService.selectCheckMember(id);
		if(member!=null) {
			//아이디 중복
			mapAjax.put("result", "idDuplicated");
		}else {
			if(!Pattern.matches("^[A-Za-z0-9]{4,12}$", id)) {
				//패턴 불일치
				mapAjax.put("result", "notMatchPattern");
			}else {
				//패턴 일치하면서 아이디 미중복
				mapAjax.put("result", "idNotFound");
			}
		}
		return mapAjax;
	}
	//회원등록 폼 호출
	@GetMapping("/member/registerUser.do")
	public String form() {
		return "memberRegister";//타일스 설정값
	}
	
	//회원가입 데이터 전송
	@PostMapping("/member/registerUser.do")
	public String submit(@Valid MemberVO memberVO,BindingResult result, Model model) {
		
		logger.debug("<<회원가입>> : " + memberVO);
		
		//유효성 체크 결과 오류가 있으면 폼 호출
		if(result.hasErrors()) {
			return form();
		}
		//회원가입
		memberService.insertMember(memberVO);
		
		model.addAttribute("accessMsg","회원가입이 완료되었습니다.");
		
		return "common/notice";
	}
	
	//회원로그인
	//로그인 폼 호출
	@GetMapping("/member/login.do")
	public String formLogin() {
		return "memberLogin";
	}
	
	//로그인 폼에 전송된 데이터 처리
	@PostMapping("/member/login.do")
	public String submitLogin(@Valid MemberVO memberVO,BindingResult result,
									HttpSession session,HttpServletResponse response) {
		logger.debug("<<회원로그인>> : " + memberVO);
		
		//유효성 체크 결과 오류가 있으면 폼을 호출
		//id와 passwd 필드만 체크
        if(result.hasFieldErrors("id") || result.hasFieldErrors("passwd")) {
        	return formLogin();
        }
		
		//로그인 체크
		MemberVO member = null;
		try {
			member = memberService.selectCheckMember(memberVO.getId());
			boolean check = false;
			
			if(member!=null) {
				//비밀번호 일치 여부 체크
				check=member.isCheckedPassword(memberVO.getPasswd());
			}
			if(check) {//인증 성공
				//자동로그인 체크
				boolean autoLogin = 
						memberVO.getAuto() != null && memberVO.getAuto().equals("on");
				if(autoLogin) {
					//자동로그인 체크를 한 경우
					String au_id = member.getAu_id();
					if(au_id==null) {
						//자동로그인 체크 식별값 생성
						au_id = UUID.randomUUID().toString();
						logger.debug("<<au_id>> : " + au_id);
						memberService.updateAu_id(au_id, memberVO.getId());
					}
					Cookie auto_cookie = new Cookie("au-log",au_id);
					auto_cookie.setMaxAge(60*60*24*7);//쿠키의 유효기간은 일주일
					auto_cookie.setPath("/");
					
					//생성한 쿠키를 클라이언트에 전송
					response.addCookie(auto_cookie);
				}
				
				//인증성공, 로그인처리
				session.setAttribute("user", member);
				
				logger.debug("<<인증성공>> : " + member.getId());
				
				if(member.getAuth()==9) {
					return "redirect:/main/admin.do";
				}else {
					return "redirect:/main/main.do";
				}
			}
			//인증 실패
			throw new AuthCheckException();
		}catch(AuthCheckException e) {
			//인증 실패로 로그인 폼 호출
			if(member!=null && member.getAuth()==1) {
				//정지회원 메시지 표시
				result.reject("noAuthority");
			}else {
				result.reject("invalidIdOrPassword");
			}
			logger.debug("<<인증실패>>");
			return formLogin();
		}
	}
	
	//회원 로그아웃
	@RequestMapping("/member/logout.do")
	public String processLogout(HttpSession session,HttpServletResponse response) {
		//로그아웃
		session.invalidate();
		
		//자동로그인 클라이언트 쿠키 처리(쿠키 삭제)
		Cookie auto_cookie = new Cookie("au-log","");
		auto_cookie.setMaxAge(0);//쿠키 유효시간 만료(쿠키 삭제)
		auto_cookie.setPath("/");
		
		//클라이언트에 쿠키 전송
		response.addCookie(auto_cookie);
		
		return "redirect:/main/main.do";
	}
	
	//회원상세정보
	@RequestMapping("/member/myPage.do")
	public String process(HttpSession session, Model model) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		//회원정보
		MemberVO member = memberService.selectMember(user.getMem_num());
		logger.debug("<<회원정보>> : " + member);
		model.addAttribute("member",member);
		
		return "myPage";
	}
	
	//프로필 사진 업로드
	@RequestMapping("/member/updateMyPhoto.do")
	@ResponseBody
	public Map<String,String> processProfile(MemberVO memberVO,HttpSession session){
		Map<String,String> mapAjax = new HashMap<String,String>();
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		if(user==null) {
			mapAjax.put("result", "logout");
		}else {
			memberVO.setMem_num(user.getMem_num());
			memberService.updateProfile(memberVO);
			mapAjax.put("result", "success");
		}
		return mapAjax;
	}
	
	//회원정보수정
	//수정 폼 호출
	@RequestMapping("/member/update.do")
	public String formUpdate(HttpSession session, Model model) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		MemberVO memberVO = memberService.selectMember(user.getMem_num());
		model.addAttribute("memberVO",memberVO);
		
		return "memberModify";
	}
	//수정 폼에서 전송된 데이터 호출
	@PostMapping("/member/update.do")
	public String submitUpdate(@Valid MemberVO memberVO,BindingResult result,HttpSession session) {
		logger.debug("<<회원정보수정 처리>> : " + memberVO);
		
		//유효성 체크 결과 오류가 있으면 폼 호출
		if(result.hasErrors()) {
			return "memberModify";
		}
		MemberVO user = (MemberVO)session.getAttribute("user");
		memberVO.setMem_num(user.getMem_num());
		
		//회원정보수정
		memberService.updateMember(memberVO);
		
		return "redirect:/member/myPage.do";
	}
	
	//비밀번호 변경
	//비밀번호 변경 폼 호출
	@GetMapping("/member/changePassword.do")
	public String formChangePasswd() {
		return "memberChangePassword";
	}
	
	//비밀번호 변경폼에서 전송된 데이터 처리
	@PostMapping("/member/changePassword.do")
	public String submitChangePasswd(@Valid MemberVO memberVO,BindingResult result,HttpSession session,
										Model model,HttpServletRequest request) {
		logger.debug("<<비밀번호변경 처리>> : " + memberVO);
		
		//유효성 체크 결과 오류가 있으면 폼 호출
		if(result.hasFieldErrors("now_passwd") || result.hasFieldErrors("passwd")) {
			return formChangePasswd();
		}
		MemberVO user = (MemberVO)session.getAttribute("user");
		memberVO.setMem_num(user.getMem_num());
		
		MemberVO db_member = memberService.selectMember(memberVO.getMem_num());
		//폼에서 전송한 현재 비밀번호와 DB에서 받아온 비밀번호 일치 여부 체크
		if(!db_member.getPasswd().equals(memberVO.getNow_passwd())) {
			result.rejectValue("now_passwd", "invalidPassword");
			return formChangePasswd();
		}
		//비밀번호 변경
		memberService.updatePassword(memberVO);
		
		//설정되어 있는 자동로그인 기능 해제(모든 브라우저에 설정된 자동로그인 해제)
		memberService.deleteAu_id(memberVO.getMem_num());
		
		//view에 표시할 메시지
		model.addAttribute("message","비밀번호변경 완료(*재접속시 설정되어 있는 자동로그인 기능 해제)");
		model.addAttribute("url", request.getContextPath() + "/member/myPage.do");
		
		return "common/resultView";
	}
	
	//회원정보삭제(회원탈퇴)
	//회원삭제 폼 호출
	@GetMapping("/member/delete.do")
	public String formDelete() {
		return "memberDelete";
	}
	//회원삭제 폼 호출
	@PostMapping("/member/delete.do")
	public String submitDelete(@Valid MemberVO memberVO,BindingResult result,
								HttpSession session,Model model) {
		logger.debug("<<회원탈퇴>> : " + memberVO);
		
		//유효성 체크 결과 오류가 있으면 폼 호출
		//id, passwd 필드의 에러만 체크
		if(result.hasFieldErrors("id") || result.hasFieldErrors("passwd")) {
			return formDelete();
		}
		MemberVO user = (MemberVO)session.getAttribute("user");
		MemberVO db_member = memberService.selectMember(user.getMem_num());
		boolean check = false;
		//비밀번호 일치 여부 체크
		try {
			if(db_member!=null && db_member.getId().equals(memberVO.getId())) {
				//비밀번호 일치 여부 체크
				check = db_member.isCheckedPassword(memberVO.getPasswd());
			}
			if(check) {//인증 성공
				memberService.deleteMember(user.getMem_num());
				//로그아웃
				session.invalidate();
				model.addAttribute("accessMsg","회원탈퇴를 완료했습니다.");
				
				return "common/notice";
			}
			//인증 실패
			//catch 블럭으로 이동
			throw new AuthCheckException();
			
		}catch(AuthCheckException e) {
			result.reject("invalidIdOrPassword");
			return formDelete();
		}
	}
	
	
	
	//프로필 사진 출력(로그인 전용)
	@RequestMapping("/member/photoView.do")
	public String getProfile(HttpSession session,HttpServletRequest request,Model model) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		logger.debug("<<photoView>> : " + user);
		
		if(user==null) {
			byte[] readbyte = FileUtil.getBytes(request.getServletContext().getRealPath("/image_bundle/face.png"));
			                    //속성명      속성값
			model.addAttribute("imageFile",readbyte);
			model.addAttribute("filename","face.png");
		}else {
			MemberVO memberVO = memberService.selectMember(user.getMem_num());
			viewProfile(memberVO, request, model);
		}
		return "imageView";
	}
	
	//프로필 사진 출력(회원번호 지정)
	@RequestMapping("/member/viewProfile.do")
	public String getProfileByMem_num(@RequestParam int mem_num,HttpSession session,
							HttpServletRequest request, Model model) {
		MemberVO memberVO = memberService.selectMember(mem_num);
		viewProfile(memberVO,request,model);
		
		return "imageView";
	}
	
	//프로필 사진 처리를 위한 공통 코드
	public void viewProfile(MemberVO memberVO,HttpServletRequest request,Model model) {
		if(memberVO == null || memberVO.getPhoto_name()==null) {
			byte[] readbyte = FileUtil.getBytes(request.getServletContext().getRealPath("/image_bundle/face.png"));
            					//속성명      속성값
			model.addAttribute("imageFile",readbyte);
			model.addAttribute("filename","face.png");
		}else {
			model.addAttribute("imageFile",memberVO.getPhoto());
			model.addAttribute("filename",memberVO.getPhoto_name());
;		}
	}
}
