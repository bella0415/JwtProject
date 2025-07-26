package com.example.jwtproject.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security의 HTTP 보안 설정을 정의하는 클래스
 */
@Configuration
public class SecurityConfig {

	/**
	 * 인증/인가 설정 필터 체인
	 *
	 * @param http HttpSecurity 객체
	 * @return SecurityFilterChain
	 * @throws Exception 설정 실패 시 예외 발생
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/signup").permitAll()  // 회원가입은 누구나 가능
				.anyRequest().authenticated()           // 나머지는 인증 필요
			)
			.httpBasic(Customizer.withDefaults()); // 기본 인증 비활성화 가능 (임시)

		return http.build();
	}
}
