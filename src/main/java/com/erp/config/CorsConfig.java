package com.erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CorsConfig : CORS(Cross-Origin Resource Sharing) 설정을 정의
// WebMvcConfigurer :
// Spring MVC에서 웹 애플리케이션의 설정을 커스터마이즈하기 위한 인터페이스. 이 인터페이스를 구현하면 다양한 웹 관련 설정을 추가할 수 있다.
@Configuration
public class CorsConfig implements WebMvcConfigurer {
	
	// addCorsMappings  : CORS 정책을 지정하여, 어떤 도메인에서 오는 요청을 허용할지, 어떤 HTTP 메서드와 헤더를 사용할 수 있을지를 설정
	@Override
	public void addCorsMappings (CorsRegistry corsRegistry) {
		corsRegistry
		.addMapping("/**") // 모든 경로에 대해 CORS 설정을 적용
		//.allowedOrigins("*") // 모든 도메인 허용 (보안상 주의 필요)
		.allowedOrigins("http://localhost:3000", "http://localhost:3001") // React의 로컬 서버 주소
        .allowedMethods("*") // 허용할 HTTP 메서드, 현재 모든 HTTP 메서드 허용
        .allowedHeaders("*") // 허용할 헤더, 현재 모든 헤더 허용
        .allowCredentials(true); // 인증 정보 포함 요청 허용 (쿠키 등)
	}
}
