package kr.spring.ch15;

import org.springframework.beans.factory.annotation.Autowired;

/*
 * @Autowired 어노테이션을 이용한 의존 관계 자동 지정
 * 생성자, 필드, 메서드에 지정가능
 * 메서드에 저장할 때 setXXX뿐만 아니라 일반메서드에서도 적용 가능
 * 
 *  @Autowired 어노테이션은 타입을 이용해서 자동적으로 프로퍼티 값을 설정하기 때문에 해당 타입의 빈 객체가 존재하지 않거나
 *  또는 빈 객체가 두개 이상 존재 할 경우 스프링은 @Autowired 어노테이션이 적용된 빈 객체를 생성할 때 예외를 발생
 *  
 *  @Autowired(requires=false)로 지정하면 해당 타입의 빈 객체가 존재하지 않더라도 스프링은 예외를 발생하지 않음
 */
public class SystemMonitor {
	private int periodTime;
	private SmsSender sender;
	
	
	public void setPeriodTime(int periodTime) {
		this.periodTime = periodTime;
	}
	
	@Autowired
	public void setSender(SmsSender sender) {
		this.sender = sender;
	}
	@Override
	public String toString() {
		return "SystemMonitor [periodTime=" + periodTime + ", sender=" + sender + "]";
	}
}
