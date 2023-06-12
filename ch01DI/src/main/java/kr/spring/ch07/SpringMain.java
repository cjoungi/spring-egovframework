package kr.spring.ch07;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {
	public static void main(String[] args) {
		//applicationContext.xml 설정파일을 읽어들여 스프링 컨테이너 생성
		AbstractApplicationContext context = 
			new ClassPathXmlApplicationContext("applicationContext2.xml");

		//DI 프로퍼티 설정 방식
		RegisterService service = (RegisterService)context.getBean("registerService");
		
		service.write();
		context.close();
	}
}
