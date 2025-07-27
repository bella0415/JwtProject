package com.example.jwtproject.domain.auth.dto.response;

import com.example.jwtproject.domain.auth.model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 회원가입 및 관리자 권한 부여 응답 DTO
 */
@Getter
public class SignupResponseDto {

	/**
	 * 사용자 아이디
	 */
	@Schema(description = "사용자 ID", example = "user123")
	private final String username;

	/**
	 * 사용자 닉네임
	 */
	@Schema(description = "닉네임", example = "슬이")
	private final String nickname;

	/**
	 * 사용자 권한 (예: USER, ADMIN)
	 */
	@Schema(description = "사용자 역할", example = "USER/ADMIN")
	private final String role;

	/**
	 * SignupResponseDto 생성자
	 *
	 * @param username 사용자 ID
	 * @param nickname 사용자 닉네임
	 * @param role     사용자 역할 (문자열 형태)
	 */
	public SignupResponseDto(String username, String nickname, String role) {
		this.username = username;
		this.nickname = nickname;
		this.role = role;
	}

	/**
	 * User 객체로부터 SignupResponseDto 생성
	 *
	 * @param user 사용자 엔티티
	 * @return SignupResponseDto 객체
	 */
	public static SignupResponseDto from(User user) {
		return new SignupResponseDto(
			user.getUsername(),
			user.getNickname(),
			user.getRole().name() // Enum → 문자열
		);
	}
}
