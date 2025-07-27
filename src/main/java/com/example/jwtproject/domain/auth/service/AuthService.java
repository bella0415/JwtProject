package com.example.jwtproject.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwtproject.domain.auth.dto.request.LoginRequestDto;
import com.example.jwtproject.domain.auth.dto.request.SignupRequestDto;
import com.example.jwtproject.domain.auth.dto.response.LoginResponseDto;
import com.example.jwtproject.domain.auth.dto.response.SignupResponseDto;
import com.example.jwtproject.domain.auth.model.Role;
import com.example.jwtproject.domain.auth.model.User;
import com.example.jwtproject.domain.auth.repository.UserRepository;
import com.example.jwtproject.global.config.JwtUtil;
import com.example.jwtproject.global.exception.CustomException;
import com.example.jwtproject.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

/**
 * 사용자 인증 및 권한 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	/**
	 * 회원가입 처리
	 *
	 * @param request 회원가입 요청 DTO
	 * @return 저장된 사용자 객체
	 * @throws CustomException 중복 사용자일 경우 예외 발생
	 */
	public User signup(SignupRequestDto request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
		}

		String encryptedPassword = passwordEncoder.encode(request.getPassword());

		Role role = Role.from(request.getRole());

		User newUser = new User(
			request.getUsername(),
			encryptedPassword,
			request.getNickname(),
			role
		);

		return userRepository.save(newUser);
	}

	/**
	 * 로그인 처리 및 JWT 발급
	 *
	 * @param request 로그인 요청 DTO
	 * @return 로그인 응답 DTO (Access/Refresh Token 포함)
	 * @throws CustomException 사용자 정보 불일치 시 예외 발생
	 */
	public LoginResponseDto login(LoginRequestDto request) {
		User user = userRepository.findByUsername(request.getUsername())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
		}

		String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole().name());
		String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

		return new LoginResponseDto(user.getUsername(), accessToken, refreshToken);
	}

	/**
	 * 관리자 권한 부여
	 *
	 * @param username 권한을 부여할 사용자 ID
	 * @return 수정된 사용자 정보 DTO
	 * @throws CustomException 사용자가 존재하지 않을 경우 예외 발생
	 */
	public SignupResponseDto grantAdminRole(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		user.grantAdminRole();
		return SignupResponseDto.from(user);
	}
}
