package com.example.jwtproject.global.handler;

import com.example.jwtproject.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 인증 실패(401 Unauthorized) 예외 발생 시 JSON 응답을 반환하는 핸들러
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
		throws IOException, ServletException {

		ErrorCode errorCode = ErrorCode.INVALID_TOKEN;

		response.setStatus(errorCode.getStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		Map<String, Object> body = new HashMap<>();
		Map<String, String> error = new HashMap<>();
		error.put("code", errorCode.getCode());
		error.put("message", errorCode.getMessage());
		body.put("error", error);

		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(body));
	}
}
