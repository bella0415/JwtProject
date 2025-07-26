package com.example.jwtproject.domain.auth.model;

import java.util.List;

import lombok.Getter;

/**
 * 사용자 정보를 나타내는 도메인 클래스
 * DB 없이 메모리 기반 저장소에 저장
 */
@Getter
public class User {

	/**
	 * 사용자 고유 ID (username)
	 */
	private final String username;

	/**
	 * 사용자 비밀번호 (암호화된 값)
	 */
	private final String password;

	/**
	 * 사용자 닉네임
	 */
	private final String nickname;

	/**
	 * 사용자 역할 (USER 또는 ADMIN)
	 */
	private Role role;

	/**
	 * 사용자 생성자 (역할 포함)
	 *
	 * @param username 사용자 ID
	 * @param password 암호화된 비밀번호
	 * @param nickname 닉네임
	 * @param role     사용자 권한
	 */
	public User(String username, String password, String nickname, Role role) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}

	/**
	 * 사용자에게 관리자 권한을 부여한다.
	 */
	public void grantAdminRole() {
		this.role = Role.ADMIN;
	}
}
