package kr.spring.tiles.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TilesController {
	@RequestMapping("/main.do")
	public String main() {
		return "main";//tilesdef.xml의 definition의 name 식별걊을 명시
	}
	
	@RequestMapping("/company.do")
	public String company() {
		return "company";//tilesdef.xml의 definition의 name 식별걊을 명시
	}
	
	@RequestMapping("/product.do")
	public String product() {
		return "product";//tilesdef.xml의 definition의 name 식별걊을 명시
	}
}
