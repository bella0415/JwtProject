package com.example.jwtproject.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 회원가입 요청 정보를 담는 DTO 클래스
 */
@Getter
public class SignupRequestDto {

	/**
	 * 사용자 아이디 (고유값)
	 */
	@NotBlank(message = "아이디는 필수 입력값입니다.")
	private String username;

	/**
	 * 사용자 비밀번호
	 */
	@NotBlank(message = "비밀번호는 필수 입력값입니다.")
	private String password;

	/**
	 * 사용자 닉네임
	 */
	@NotBlank(message = "닉네임은 필수 입력값입니다.")
	private String nickname;

	/**
	 * 사용자 권한 (USER 또는 ADMIN)
	 * 기본값은 USER
	 */
	private String role = "USER"; // 입력 안 해도 USER로 자동 세팅됨

	// 테스트 및 서비스용 생성자 추가
	public SignupRequestDto(String username, String password, String nickname, String role) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}
}
