package com.example.jwtproject.domain.auth.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.jwtproject.domain.auth.model.User;

/**
 * 메모리 기반의 사용자 저장소 클래스
 * 실제 데이터베이스 없이 HashMap을 이용해 사용자 정보를 저장 및 조회
 */
@Repository
public class UserRepository {

	/**
	 * 사용자 정보를 저장하는 Map (username → User)
	 * - ConcurrentHashMap을 사용하여 멀티스레드 환경에서도 안전하게 동작
	 */
	private final Map<String, User> userStore = new ConcurrentHashMap<>();

	/**
	 * 사용자 저장
	 *
	 * @param user 저장할 사용자 객체
	 */
	public void save(User user) {
		userStore.put(user.getUsername(), user);
	}

	/**
	 * username으로 사용자 조회
	 *
	 * @param username 조회할 사용자 ID
	 * @return Optional<User>
	 */
	public Optional<User> findByUsername(String username) {
		return Optional.ofNullable(userStore.get(username));
	}

	/**
	 * 사용자 존재 여부 확인
	 *
	 * @param username 확인할 사용자 ID
	 * @return 존재하면 true, 없으면 false
	 */
	public boolean existsByUsername(String username) {
		return userStore.containsKey(username);
	}

	/**
	 * 모든 사용자 삭제 (테스트용)
	 */
	public void clear() {
		userStore.clear();
	}
}
