package kr.spring.ch20.aop.annot;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import kr.spring.product.Product;

public class SpringMain {
	public static void main(String[] args) {
		//applicationContext.xml 설정파일을 읽어들여 스프링 컨테이너 생성
		AbstractApplicationContext context = 
			new ClassPathXmlApplicationContext("applicationContextAOPAnnot.xml");
		
		//핵심 기능이 구현된 메서드 호출
		Product p = (Product)context.getBean("product");
		p.launch();
		
		context.close();
	}
}
