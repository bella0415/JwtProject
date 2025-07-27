package com.example.jwtproject.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 핸들러 클래스
 * 모든 CustomException을 받아 일관된 JSON 에러 응답을 반환
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * CustomException 처리 메서드
	 *
	 * @param ex 발생한 예외 객체
	 * @return 에러 코드와 메시지를 포함한 JSON 응답
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
		ErrorCode code = ex.getErrorCode();
		ErrorResponse response = new ErrorResponse(code.getCode(), code.getMessage());
		return new ResponseEntity<>(response, code.getStatus());
	}
}
