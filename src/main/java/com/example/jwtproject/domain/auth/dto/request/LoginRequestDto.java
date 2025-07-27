package com.example.jwtproject.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 로그인 요청 시 클라이언트가 전달하는 요청 정보를 담는 DTO
 */
@Getter
public class LoginRequestDto {

	/**
	 * 사용자 아이디 (username)
	 */
	@NotBlank(message = "아이디는 필수 입력값입니다.")
	@Schema(description = "사용자 ID", example = "user123")
	private String username;

	/**
	 * 비밀번호
	 */
	@NotBlank(message = "비밀번호는 필수 입력값입니다.")
	@Schema(description = "비밀번호", example = "password123")
	private String password;

	// 테스트 및 서비스용 생성자 추가
	public LoginRequestDto(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
