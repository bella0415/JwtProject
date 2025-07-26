package com.example.jwtproject.domain.auth.controller;

import com.example.jwtproject.domain.auth.dto.request.SignupRequestDto;
import com.example.jwtproject.domain.auth.dto.response.SignupResponseDto;
import com.example.jwtproject.domain.auth.model.User;
import com.example.jwtproject.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 API를 처리하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	/**
	 * 회원가입 API
	 *
	 * @param request 회원가입 요청 DTO
	 * @return 회원가입 결과 응답 DTO
	 */
	@PostMapping
	public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto request) {
		User user = authService.signup(
			request.getUsername(),
			request.getPassword(),
			request.getNickname()
		);

		SignupResponseDto response = new SignupResponseDto(
			user.getUsername(),
			user.getNickname(),
			user.getRoles()
		);

		return ResponseEntity.ok(response);
	}
}
