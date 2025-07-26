package com.example.jwtproject.global.filter;

import com.example.jwtproject.global.config.JwtUtil;
import com.example.jwtproject.global.exception.CustomException;
import com.example.jwtproject.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 인증 필터
 * - 요청 헤더에서 토큰 추출
 * - 토큰 유효성 검사
 * - 인증 객체 생성 및 SecurityContext에 등록
 */
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String token = jwtUtil.resolveToken(request);

		if (token != null) {
			try {
				if (!jwtUtil.validateToken(token)) {
					throw new CustomException(ErrorCode.INVALID_TOKEN);
				}

				Claims claims = jwtUtil.extractClaims(token);
				String username = claims.getSubject();
				String role = claims.get("role", String.class);

				UsernamePasswordAuthenticationToken auth =
					new UsernamePasswordAuthenticationToken(username, null,
						Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role)));

				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (Exception e) {
				throw new CustomException(ErrorCode.INVALID_TOKEN);
			}
		}

		filterChain.doFilter(request, response);
	}
}
