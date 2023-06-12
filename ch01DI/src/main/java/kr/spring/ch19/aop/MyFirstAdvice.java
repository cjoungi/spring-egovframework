package kr.spring.ch19.aop;

import org.aspectj.lang.ProceedingJoinPoint;

//공통기능이 구현되는 클래스
public class MyFirstAdvice {
	
	/*
	 * 구현 가능한 Advice(언제 공통기능을 핵심 로직에 적용할지 정의) 종류
	 * 종류                      설명
	 * Before Advice            대상 객체의 메서드 호출 전에 공통 기능을 실행
	 * After Returning Advice   대상 객체의 메서드가 예외 없이 실행한 이후에 공통 기능을 실행
	 * After Throwing Advice    대상 객체의 메서드를 실행하는 도중 예외가 발생한 경우에 공통 기능을 실행
	 * After Advice             대상 객체의 메서드를 실행하는 도중 예외가 발생했는지의 여부와 상관없이 메서드 실행 후 공통 기능을 실행
	 *                          (try ~ catch ~ finally의 finally 블럭과 비슷)
	 * Around Advice            대상 객체의 메서드 실행 전,후 또는 예외 발생 시점에 공통 기능을 실행
	 */
	
	public void before() {
		//메서드 시작 직전에 동작하는 어드바이스
		System.out.println("Hello Before! **메서드가 호출되기 전에 나온다!!");
	}
	
	public void afterReturning(String msg) {
		//메서드 호출이 예외를 내보내지 않고 종료했을 때 동작하는 어드바이스
		System.out.println("Hello AfterReturning! **메서드가 호출한 후에 나온다! 전달된 객체 : " + msg);
	}
	
	public void afterThrowing(Exception ex) {
		//메서드 호출이 예외를 던졌을 때 동작하는 어드바이스
		System.out.println("Hello AfterThrowing!! **예외가 생기면 나온다! 예외 : " + ex);
	}
	
	public void after() {
		//예이가 발생해도 실행됨
		//메서드 종료후에 동작하는 어드바이스
		System.out.println("Hello After! **메서드가 호출된 후에 나온다!");
	}
	
	//반환 타입을 지정하면 이 기능 이후에 실행되는 공통 기능에서 반환하는 데이터를 받을 수 있음
	//                   핵심기능을 수행하는 객체
	public String around(ProceedingJoinPoint joinPoint)throws Throwable {
		//메서드 호출 전후에 동작하는 어드바이스
		System.out.println("Hello Around before! **메서드가 호출되기 전에 나온다.");
		String s = null;
		
		//try~구조로 명시해야 예외가 발생해도 메서드 실행 후 공통기능을 수행
		try{
			//핵심 기능을 수행하는 메서드를 호출하고 데이터를 반환
			s = (String)joinPoint.proceed();//Product의 launch 메서드 호출
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			System.out.println("Hello Around after! ** 메서드가 호출된 후에 나온다! 반환된 객체 : " + s);
		}
		
		
		
		return s;
	}
}
