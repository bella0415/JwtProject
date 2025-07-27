package com.example.jwtproject.global.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

/**
 * JWT 생성, 파싱, 검증 등을 처리하는 유틸리티 클래스
 */
@Slf4j
@Component
public class JwtUtil {

	private static final String BEARER_PREFIX = "Bearer ";
	private static Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Value("${jwt.accessToken.time}")
	private long accessTokenExpiration;

	@Value("${jwt.refreshToken.time}")
	private long refreshTokenExpiration;

	/**
	 * secretKey를 Base64 디코딩하여 서명 키 초기화
	 */
	@PostConstruct
	public void init() {
		byte[] keyBytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * AccessToken 생성
	 *
	 * @param username 사용자 아이디
	 * @param role     사용자 역할
	 * @return 생성된 JWT AccessToken
	 */
	public String createAccessToken(String username, String role) {
		return createToken(username, role, accessTokenExpiration);
	}

	/**
	 * RefreshToken 생성
	 *
	 * @param username 사용자 아이디
	 * @return 생성된 JWT RefreshToken
	 */
	public String createRefreshToken(String username) {
		return createToken(username, null, refreshTokenExpiration);
	}

	/**
	 * JWT 생성 공통 메서드
	 *
	 * @param username 사용자 아이디
	 * @param role     역할 (null 허용)
	 * @param expirationTime 만료 시간(ms)
	 * @return 생성된 JWT
	 */
	private String createToken(String username, String role, long expirationTime) {
		return Jwts.builder()
			.setSubject(username)
			.claim("role", role) // null인 경우 claim에 포함되지 않음
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	/**
	 * JWT 유효성 검증 (실패 시 예외 발생)
	 *
	 * @param token JWT 토큰
	 * @throws JwtException 검증 실패 시 발생
	 */
	public void validateToken(String token) {
		Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
	}

	/**
	 * JWT에서 Claims 추출
	 *
	 * @param token JWT 토큰
	 * @return Claims 객체
	 */
	public Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	/**
	 * HTTP 요청 헤더에서 JWT 추출
	 *
	 * @param request HTTP 요청
	 * @return JWT 토큰 문자열을 Optional로 래핑
	 */
	public Optional<String> resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
			return Optional.of(bearer.substring(BEARER_PREFIX.length()));
		}
		return Optional.empty();
	}

	/**
	 * 테스트용 더미 토큰 생성 메서드
	 * 테스트코드에서 토큰 발급을 위한 임시 토큰 생성 메서드
	 *
	 * @param username 사용자 아이디
	 * @param role     사용자 역할
	 * @return 더미 토큰 문자열
	 */
	public String createDummyToken(String username, String role) {
		return "Bearer dummy-token-for-" + username + "-" + role;
	}
}
