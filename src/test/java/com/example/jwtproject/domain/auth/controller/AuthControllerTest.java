package com.example.jwtproject.domain.auth.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.jwtproject.domain.auth.dto.request.LoginRequestDto;
import com.example.jwtproject.domain.auth.dto.request.SignupRequestDto;
import com.example.jwtproject.domain.auth.dto.response.LoginResponseDto;
import com.example.jwtproject.domain.auth.dto.response.SignupResponseDto;
import com.example.jwtproject.domain.auth.model.Role;
import com.example.jwtproject.domain.auth.model.User;
import com.example.jwtproject.domain.auth.service.AuthService;
import com.example.jwtproject.global.exception.CustomException;
import com.example.jwtproject.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AuthController의 API에 대한 단위 테스트 클래스
 * 회원가입, 로그인, 관리자 권한 부여 API에 대해 정상/예외 케이스를 검증
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthService authService;

	@Nested
	@DisplayName("성공 케이스")
	class SuccessCases {

		@Test
		@DisplayName("회원가입 성공")
		void signup_success() throws Exception {
			SignupRequestDto request = new SignupRequestDto("testuser", "pass", "닉네임", "USER");
			User user = new User("testuser", "encodedPass", "닉네임", Role.USER);
			given(authService.signup(any())).willReturn(user);

			mockMvc.perform(post("/signup")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value("testuser"))
				.andExpect(jsonPath("$.role").value("USER"));
		}

		@Test
		@DisplayName("로그인 성공")
		void login_success() throws Exception {
			LoginRequestDto request = new LoginRequestDto("testuser", "pass");
			LoginResponseDto response = new LoginResponseDto("testuser", "access-token", "refresh-token");
			given(authService.login(any())).willReturn(response);

			mockMvc.perform(post("/login")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").value("access-token"))
				.andExpect(jsonPath("$.refreshToken").value("refresh-token"));
		}

		@Test
		@WithMockUser(authorities = "ROLE_ADMIN")
		@DisplayName("관리자 권한 부여 성공")
		void grantAdminRole_success() throws Exception {
			String username = "user1";
			SignupResponseDto response = new SignupResponseDto("user1", "닉", "ADMIN");
			given(authService.grantAdminRole(username)).willReturn(response);

			mockMvc.perform(patch("/admin/users/" + username + "/roles"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("user1"))
				.andExpect(jsonPath("$.role").value("ADMIN"));
		}

		@Test
		@WithMockUser(authorities = "ROLE_ADMIN")
		@DisplayName("관리자 전용 페이지 접근 성공")
		void adminPage_success() throws Exception {
			mockMvc.perform(get("/admin/only"))
				.andExpect(status().isOk())
				.andExpect(content().string("관리자만 볼 수 있는 페이지입니다."));
		}
	}

	@Nested
	@DisplayName("실패 케이스")
	class FailureCases {

		@Test
		@DisplayName("중복 아이디로 회원가입 실패")
		void signup_fail_duplicate() throws Exception {
			SignupRequestDto request = new SignupRequestDto("testuser", "pass", "닉네임", "USER");
			given(authService.signup(any())).willThrow(new CustomException(ErrorCode.USER_ALREADY_EXISTS));

			mockMvc.perform(post("/signup")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.code").value("USER_ALREADY_EXISTS"));
		}

		@Test
		@DisplayName("존재하지 않는 사용자 로그인 실패")
		void login_fail_userNotFound() throws Exception {
			LoginRequestDto request = new LoginRequestDto("unknown", "pass");
			given(authService.login(any())).willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

			mockMvc.perform(post("/login")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
		}

		@Test
		@DisplayName("비밀번호 오류 로그인 실패")
		void login_fail_wrongPassword() throws Exception {
			LoginRequestDto request = new LoginRequestDto("testuser", "wrongpass");
			given(authService.login(any())).willThrow(new CustomException(ErrorCode.INVALID_CREDENTIALS));

			mockMvc.perform(post("/login")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
		}

		@Test
		@WithMockUser(authorities = "ROLE_USER")
		@DisplayName("USER 권한으로 관리자 권한 부여 시도 → 실패")
		void grantAdminRole_fail_forbidden() throws Exception {
			mockMvc.perform(patch("/admin/users/user1/roles"))
				.andExpect(status().isForbidden());
		}

		@Test
		@WithMockUser(authorities = "ROLE_ADMIN")
		@DisplayName("존재하지 않는 사용자 권한 부여 실패")
		void grantAdminRole_fail_userNotFound() throws Exception {
			given(authService.grantAdminRole("nouser")).willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

			mockMvc.perform(patch("/admin/users/nouser/roles"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
		}

	}
}
