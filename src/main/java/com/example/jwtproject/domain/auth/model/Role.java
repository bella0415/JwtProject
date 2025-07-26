package com.example.jwtproject.domain.auth.model;

/**
 * 사용자의 권한(Role)을 정의하는 enum 클래스
 * - USER: 일반 사용자
 * - ADMIN: 관리자 권한
 */
public enum Role {

	/**
	 * 일반 사용자 권한
	 */
	USER,

	/**
	 * 관리자 권한
	 */
	ADMIN
}
