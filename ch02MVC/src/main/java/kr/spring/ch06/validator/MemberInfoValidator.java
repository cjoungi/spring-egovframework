package kr.spring.ch06.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.spring.ch06.vo.MemberInfo;

public class MemberInfoValidator implements Validator{

	//Validator가 검증할 수 있는 타입인지를 검사
	@Override
	public boolean supports(Class<?> clazz) {
		return MemberInfo.class.isAssignableFrom(clazz);
	}

	//
	//검증 결과 문제가 있을 경우 Errors 객체에 어떤 문제인지에 대한 정보를 저장
	@Override
	public void validate(Object target, Errors errors) {
		MemberInfo memberInfo = (MemberInfo)target;
		
		//데이터가 전송되지 않았으면 에러로 판단해서 Errors 객체에 에러 정보 저장
		if(memberInfo.getId()==null || memberInfo.getId().trim().isEmpty()){
		                      //필드   에러코드
			errors.rejectValue("id", "required");
		}
		
		if(memberInfo.getName()==null || memberInfo.getName().trim().isEmpty()) {
			errors.rejectValue("name", "required");
		}
		
		if(memberInfo.getZipcode()==null || memberInfo.getZipcode().trim().isEmpty()) {
			errors.rejectValue("zipcode", "required");
		}
		
		if(memberInfo.getAddress1()==null || memberInfo.getAddress1().trim().isEmpty()) {
			errors.rejectValue("address1", "required");
		}
		
		if(memberInfo.getAddress2()==null || memberInfo.getAddress2().trim().isEmpty()) {
			errors.rejectValue("address2", "required");
		}
	}

}
