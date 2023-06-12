package kr.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import kr.spring.interceptor.AdminCheckInterceptor;
import kr.spring.interceptor.AutoLoginCheckInterceptor;
import kr.spring.interceptor.LoginCheckInterceptor;
import kr.spring.interceptor.WriterCheckInterceptor;

//자바코드 기반 설정 클래스
@Configuration
public class AppConfig implements WebMvcConfigurer{
	//객체 저장
	private AutoLoginCheckInterceptor autoLogin;
	private LoginCheckInterceptor logincheck;
	private AdminCheckInterceptor admincheck;
	private WriterCheckInterceptor writercheck;
	
	@Bean
	public AutoLoginCheckInterceptor interceptor() {
		autoLogin = new AutoLoginCheckInterceptor();
		return autoLogin;
	}
	
	@Bean
	public LoginCheckInterceptor interceptor2() {
		logincheck = new LoginCheckInterceptor();
		return logincheck;
	}
	
	@Bean
	public AdminCheckInterceptor interceptor3() {
		admincheck = new AdminCheckInterceptor();
		return admincheck;
	}
	
	@Bean
	public WriterCheckInterceptor interceptor4() {
		writercheck = new WriterCheckInterceptor();
		return writercheck;
	}
	
	//인터셉터 등록
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		//AutoLoginCheckInterceptor 설정
		registry.addInterceptor(autoLogin).addPathPatterns("/**")//루트 밑의 모든 경로에서 자동 로그인 체크
											.excludePathPatterns("/member/login.do")
											.excludePathPatterns("/member/logout.do");
		
		
		//LoginCheckInterceptor 설정
		registry.addInterceptor(logincheck).addPathPatterns("/member/myPage.do")
											 .addPathPatterns("/member/update.do")
											 .addPathPatterns("/member/changePassword.do")
											 .addPathPatterns("/member/delete.do")
										     .addPathPatterns("/board/write.do")
										     .addPathPatterns("/board/update.do")
										     .addPathPatterns("/board/delete.do");
		
		//AdminCheckInterceptor 설정
		registry.addInterceptor(admincheck).addPathPatterns("/main/admin.do")
											.addPathPatterns("/member/admin_list.do")
											.addPathPatterns("/member/admin_update.do");
		
		//WriterCheckInterceptor 설정
		registry.addInterceptor(writercheck).addPathPatterns("/board/update.do")
											.addPathPatterns("/board/delete.do");
	}
	
	//타일스 설정
	@Bean
	public TilesConfigurer tilesConfigurer() {
		final TilesConfigurer configurer = new TilesConfigurer();
		//해당 경로에 xml 설정 파일을 넣음
		configurer.setDefinitions(new String[] {
				"/WEB-INF/tiles-def/main.xml","/WEB-INF/tiles-def/member.xml","/WEB-INF/tiles-def/board.xml"
		});
		configurer.setCheckRefresh(true);
		return configurer;
	}
	@Bean
	public TilesViewResolver tilesViewResolver() {
		final TilesViewResolver tilesViewResolver = new TilesViewResolver();
		tilesViewResolver.setViewClass(TilesView.class);
		
		return tilesViewResolver;
	}
}
