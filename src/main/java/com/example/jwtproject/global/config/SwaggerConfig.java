package com.example.jwtproject.global.config;

import com.example.jwtproject.global.exception.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger(OpenAPI) 문서화 설정 클래스
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI jwtProjectOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("JWT 인증 프로젝트 API 문서")
				.description("회원가입, 로그인, 권한 부여 등 인증 관련 API 문서입니다.")
				.version("v2.3.0"))
			.components(new Components()
				.addSchemas("ErrorResponse", new Schema<ErrorResponse>()));
	}
}
