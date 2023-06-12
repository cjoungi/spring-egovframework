package kr.spring.ch09;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {
	public static void main(String[] args) {
		//applicationContext2.xml 설정파일을 읽어들여 스프링 컨테이너 생성
		AbstractApplicationContext context = 
				new ClassPathXmlApplicationContext("applicationContext2.xml");

		//XML 네임프로퍼티를 이용한 프로퍼티 설정
		UploadController upload = (UploadController)context.getBean("upload");

		System.out.println(upload);
		context.close();
	}
}
