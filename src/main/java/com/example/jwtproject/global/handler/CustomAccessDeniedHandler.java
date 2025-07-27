package com.example.jwtproject.global.handler;

import com.example.jwtproject.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 권한 부족(403 Forbidden) 예외 발생 시 JSON 응답을 반환하는 핸들러
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
		throws IOException, ServletException {

		ErrorCode errorCode = ErrorCode.ACCESS_DENIED;

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
