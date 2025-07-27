package com.example.jwtproject.domain.auth.model;

import com.example.jwtproject.global.exception.CustomException;
import com.example.jwtproject.global.exception.ErrorCode;

/**
 * 사용자의 권한(Role)을 정의하는 enum 클래스
 * - USER: 일반 사용자
 * - ADMIN: 관리자 권한
 */
public enum Role {
	USER, ADMIN;

	public static Role from(String value) {
		try {
			return Role.valueOf(value.toUpperCase().trim());
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new CustomException(ErrorCode.INVALID_ROLE);
		}
	}
}
