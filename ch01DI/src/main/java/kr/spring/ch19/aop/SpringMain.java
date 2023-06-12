package kr.spring.ch19.aop;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import kr.spring.product.Product;

public class SpringMain {
	public static void main(String[] args) {
		//스프링 컨테이너를 생성
		AbstractApplicationContext context = 
			new ClassPathXmlApplicationContext("applicationContextAOP.xml");
		
		//AOP 설정을 이용해서 공통 기능을 핵심 기능 실행시 적용하기
		Product p = (Product)context.getBean("product");
		p.launch();
		
		context.close();
	}
}
