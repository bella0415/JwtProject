package com.example.jwtproject.global.exception;

import lombok.Getter;

/**
 * 모든 커스텀 예외의 최상위 클래스
 * 각 예외는 ErrorCode를 통해 상태코드, 에러코드, 메시지를 전달
 */
@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

	/**
	 * 에러코드를 기반으로 예외 객체를 생성
	 *
	 * @param errorCode 발생한 에러의 코드 및 메시지를 담고 있는 ErrorCode
	 */
	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
