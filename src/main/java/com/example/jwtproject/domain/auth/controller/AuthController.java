package com.example.jwtproject.domain.auth.controller;

import com.example.jwtproject.domain.auth.dto.request.LoginRequestDto;
import com.example.jwtproject.domain.auth.dto.request.SignupRequestDto;
import com.example.jwtproject.domain.auth.dto.response.LoginResponseDto;
import com.example.jwtproject.domain.auth.dto.response.SignupResponseDto;
import com.example.jwtproject.domain.auth.model.User;
import com.example.jwtproject.domain.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 인증 및 권한 관련 요청을 처리하는 컨트롤러
 */
@Tag(name = "Auth", description = "인증 및 권한 관리 API")
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "회원가입", description = "사용자 정보를 입력받아 회원가입을 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "회원가입 성공",
			content = @Content(schema = @Schema(implementation = SignupResponseDto.class))),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 사용자",
			content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse")))
	})
	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signup(
		@RequestBody @Parameter(description = "회원가입 요청 DTO", required = true)
		SignupRequestDto request) {
		User newUser = authService.signup(request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SignupResponseDto.from(newUser));
	}

	@Operation(summary = "로그인", description = "사용자 정보를 검증하고 Access / Refresh 토큰을 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공",
			content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 불일치",
			content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse")))
	})
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(
		@RequestBody @Parameter(description = "로그인 요청 DTO", required = true)
		LoginRequestDto request) {
		LoginResponseDto responseDto = authService.login(request);
		return ResponseEntity.ok(responseDto);
	}

	@Operation(summary = "관리자 권한 부여", description = "ADMIN 권한을 가진 사용자가 다른 사용자에게 관리자 권한을 부여합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "권한 부여 성공",
			content = @Content(schema = @Schema(implementation = SignupResponseDto.class))),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음",
			content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse"))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
			content = @Content(schema = @Schema(ref = "#/components/schemas/ErrorResponse")))
	})
	@PatchMapping("/admin/users/{username}/roles")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<SignupResponseDto> grantAdminRole(
		@PathVariable @Parameter(description = "관리자 권한을 부여할 사용자 ID") String username) {
		SignupResponseDto updatedUser = authService.grantAdminRole(username);
		return ResponseEntity.ok(updatedUser);
	}

	@Operation(summary = "관리자 전용 테스트 API", description = "ADMIN 권한이 있어야 접근 가능한 테스트 엔드포인트입니다.")
	@ApiResponse(responseCode = "200", description = "접근 성공")
	@GetMapping("/admin/only")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> adminOnly() {
		return ResponseEntity.ok("관리자만 볼 수 있는 페이지입니다.");
	}
}
