package com.example.jwtproject.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.jwtproject.domain.auth.dto.request.LoginRequestDto;
import com.example.jwtproject.domain.auth.dto.request.SignupRequestDto;
import com.example.jwtproject.domain.auth.dto.response.LoginResponseDto;
import com.example.jwtproject.domain.auth.dto.response.SignupResponseDto;
import com.example.jwtproject.domain.auth.model.User;
import com.example.jwtproject.domain.auth.service.AuthService;

/**
 * 사용자 인증 및 권한 관련 요청을 처리하는 컨트롤러
 */
@RestController
public class AuthController {

	private final AuthService authService;

	/**
	 * AuthController 생성자
	 *
	 * @param authService 사용자 인증 서비스
	 */
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	/**
	 * 회원가입 요청을 처리한다.
	 *
	 * @param request 회원가입 요청 DTO (username, password, nickname, role 포함)
	 * @return 가입된 사용자 정보 DTO
	 */
	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto request) {
		User newUser = authService.signup(request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SignupResponseDto.from(newUser));
	}

	/**
	 * 로그인 요청을 처리하고 JWT 토큰을 반환한다.
	 *
	 * @param request 로그인 요청 DTO (username, password 포함)
	 * @return JWT Access Token 및 Refresh Token 포함 응답 DTO
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
		LoginResponseDto responseDto = authService.login(request);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 관리자 권한을 사용자에게 부여한다.
	 * ADMIN 권한이 있는 사용자만 접근 가능
	 *
	 * @param username 권한을 부여할 대상 사용자의 ID
	 * @return 권한이 부여된 사용자 정보 DTO
	 */
	@PatchMapping("/admin/users/{username}/roles")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<SignupResponseDto> grantAdminRole(@PathVariable String username) {
		SignupResponseDto updatedUser = authService.grantAdminRole(username);
		return ResponseEntity.ok(updatedUser);
	}

	/**
	 * 관리자 전용 테스트 엔드포인트
	 *
	 * @return 관리자만 접근 가능한 메시지
	 */
	@GetMapping("/admin/only")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> adminOnly() {
		return ResponseEntity.ok("관리자만 볼 수 있는 페이지입니다.");
	}
}
