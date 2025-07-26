package com.example.jwtproject.domain.auth.model;

import java.util.ArrayList;
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
	 * 사용자 역할 목록 (USER, ADMIN)
	 */
	private final List<Role> roles = new ArrayList<>();

	/**
	 * User 생성자 - 기본값
	 *
	 * @param username 사용자 ID
	 * @param password 암호화된 비밀번호
	 * @param nickname 닉네임
	 */
	public User(String username, String password, String nickname) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.roles.add(Role.USER); // 기본 권한
	}

	/**
	 * 사용자에게 관리자(ADMIN) 권한을 부여
	 */
	public void grantAdminRole() {
		if (!this.roles.contains(Role.ADMIN)) {
			this.roles.add(Role.ADMIN);
		}
	}

	/**
	 * 사용자가 특정 권한을 보유하고 있는지 확인
	 *
	 * @param role 확인할 역할
	 * @return 보유 여부
	 */
	public boolean hasRole(Role role) {
		return roles.contains(role);
	}
}
