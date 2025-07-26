package com.example.jwtproject.domain.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.jwtproject.domain.auth.model.User;
import com.example.jwtproject.domain.auth.repository.UserRepository;
import com.example.jwtproject.global.exception.CustomException;
import com.example.jwtproject.global.exception.ErrorCode;

/**
 * 사용자 인증 및 권한 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
public class AuthService {

	private final UserRepository userRepository;

	/**
	 * AuthService 생성자
	 *
	 * @param userRepository 사용자 저장소 의존성 주입
	 */
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * 새로운 사용자 회원가입 처리
	 *
	 * @param username 사용자 ID
	 * @param password 비밀번호 (암호화된 값)
	 * @param nickname 닉네임
	 * @return 저장된 사용자 객체
	 * @throws CustomException 중복 사용자일 경우
	 */
	public User signup(String username, String password, String nickname) {
		if (userRepository.existsByUsername(username)) {
			throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
		}

		User newUser = new User(username, password, nickname);
		userRepository.save(newUser);
		return newUser;
	}

	/**
	 * 로그인 처리
	 *
	 * @param username 사용자 ID
	 * @param password 입력한 비밀번호
	 * @return 인증된 사용자 객체
	 * @throws CustomException 사용자 정보가 일치하지 않을 경우
	 */
	public User login(String username, String password) {
		Optional<User> userOpt = userRepository.findByUsername(username);

		if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
			throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
		}

		return userOpt.get();
	}

	/**
	 * 관리자 권한 부여
	 *
	 * @param username 권한을 부여할 사용자 ID
	 * @throws CustomException 사용자가 존재하지 않을 경우
	 */
	public void grantAdminRole(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		user.grantAdminRole();
	}
}
