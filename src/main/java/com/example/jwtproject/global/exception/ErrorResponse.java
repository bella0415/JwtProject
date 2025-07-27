package com.example.jwtproject.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 예외 발생 시 클라이언트에게 반환되는 에러 응답 객체
 * 에러 코드와 메시지를 JSON 형태로 감쌈
 */
@Getter
@AllArgsConstructor
@Schema(name = "ErrorResponse", description = "에러 응답 객체")
public class ErrorResponse {

	/**
	 * 에러 식별 코드 (ex: USER_ALREADY_EXISTS)
	 */
	@Schema(description = "에러 코드", example = "USER_ALREADY_EXISTS")
	private final String code;

	/**
	 * 에러 메시지
	 */
	@Schema(description = "에러 메시지", example = "이미 가입된 사용자입니다.")
	private final String message;
}
