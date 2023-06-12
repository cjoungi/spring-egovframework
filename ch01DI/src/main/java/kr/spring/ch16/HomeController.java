package kr.spring.ch16;

import javax.annotation.Resource;

public class HomeController {
	//빈의 이름과 프로퍼티명이 일치하면 의존 관계 설정
	@Resource
	private Camera camera1;

	//@Resource(name="빈 객체의 이름") 빈 객체의 이름 지정 가능
	@Resource(name="")
	private Camera camera2;
	
	
	public void setCamera2(Camera camera2) {
		this.camera2 = camera2;
	}

	public void setCamera1(Camera camera1) {
		this.camera1 = camera1;
	}

	@Override
	public String toString() {
		return "HomeController [camera1=" + camera1 + ", camera2=" + camera2 + "]";
	}
}
