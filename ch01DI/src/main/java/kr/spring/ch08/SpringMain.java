package kr.spring.ch08;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {
	public static void main(String[] args) {
		//applicationContext2.xml 설정파일을 읽어들여 스프링 컨테이너 생성
		AbstractApplicationContext context = 
				new ClassPathXmlApplicationContext("applicationContext2.xml");
		
		//DI 프로퍼티 설정 방식
		WorkController work = (WorkController)context.getBean("work");

	System.out.println(work);
	context.close();
	}
}
