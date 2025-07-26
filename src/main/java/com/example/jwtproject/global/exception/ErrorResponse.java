package com.example.jwtproject.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 예외 발생 시 클라이언트에게 반환되는 에러 응답 객체
 * 에러 코드와 메시지를 JSON 형태로 감쌈
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {

	/**
	 * 에러 식별 코드 (ex: USER_ALREADY_EXISTS)
	 */
	private final String code;

	/**
	 * 에러 메시지
	 */
	private final String message;
}
