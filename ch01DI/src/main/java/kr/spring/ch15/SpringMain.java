package kr.spring.ch15;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {
	public static void main(String[] args) {
		//스프링 컨테이너를 생성
		AbstractApplicationContext context = 
				new ClassPathXmlApplicationContext("applicationContext3.xml");
		
		//@Autowired 어노테이션을 이용한 의존 관계 자동 설정
		SystemMonitor monitor = (SystemMonitor)context.getBean("monitor");
		
		System.out.println(monitor);
		
		context.close();
	}
}
