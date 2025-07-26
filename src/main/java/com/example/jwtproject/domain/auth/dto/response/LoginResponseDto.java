package com.example.jwtproject.domain.auth.dto.response;

import lombok.Getter;

/**
 * 로그인 성공 시 클라이언트에게 반환되는 응답 DTO
 * 액세스 토큰 및 리프레시 토큰 정보를 포함
 */
@Getter
public class LoginResponseDto {

	/**
	 * 사용자 ID (username)
	 */
	private final String username;

	/**
	 * Access Token (JWT)
	 */
	private final String accessToken;

	/**
	 * Refresh Token
	 */
	private final String refreshToken;

	/**
	 * 생성자
	 *
	 * @param username 사용자 이름
	 * @param accessToken 액세스 토큰
	 * @param refreshToken 리프레시 토큰
	 */
	public LoginResponseDto(String username, String accessToken, String refreshToken) {
		this.username = username;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
