package kr.spring.ch12;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import kr.spring.ch11.ProtocolHandlerFactory;

public class SpringMain {
	public static void main(String[] args) {
			//applicationContext.xml 설정파일을 읽어들여 스프링 컨테이너 생성
			AbstractApplicationContext context = 
				new ClassPathXmlApplicationContext("applicationContext2.xml");
			
			//Map 타입 프로퍼티 설정
			BookClient book = 
					(BookClient)context.getBean("bookClient");
			
			System.out.println(book);
			
			context.close();
	}
}
