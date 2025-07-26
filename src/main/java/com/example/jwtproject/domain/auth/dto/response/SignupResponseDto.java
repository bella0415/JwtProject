package com.example.jwtproject.domain.auth.dto.response;

import java.util.List;

import com.example.jwtproject.domain.auth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원가입 성공 시 반환되는 응답 DTO 클래스
 */
@Getter
@AllArgsConstructor
public class SignupResponseDto {

	/**
	 * 사용자 아이디
	 */
	private final String username;

	/**
	 * 사용자 닉네임
	 */
	private final String nickname;

	/**
	 * 사용자 역할 목록
	 */
	private final List<Role> roles;
}
