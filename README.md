# 📌 프로젝트명  
**JWT 인증 시스템 백엔드 API**

> Spring Security와 JWT 기반 인증/인가 구현, 사용자/관리자 권한 구분, Swagger 문서화까지 포함한 실습형 프로젝트

---

## 📄 프로젝트 소개

- 이 프로젝트는 **JWT(Json Web Token)** 을 활용한 인증/인가 시스템을 구현한 백엔드 서버입니다.
- Spring Security를 기반으로 인증 처리 로직을 구성하였고, 사용자는 회원가입과 로그인을 통해 **Access Token / Refresh Token**을 발급받습니다.
- 또한 관리자는 별도 API를 통해 **다른 사용자에게 관리자 권한을 부여**할 수 있으며, 각 권한에 따라 접근 제어가 이루어집니다.
- 전역 예외 처리 구조 및 Swagger(OpenAPI 3) 문서를 적용해 **일관된 API 응답 구조**와 **개발자 친화적인 문서화**도 함께 제공합니다.

---

## 🛠 기술 스택

| 구분         | 사용 기술                             |
|--------------|----------------------------------------|
| Language     | Java 17                                |
| Framework    | Spring Boot 3.x, Spring Security       |
| Build Tool   | Gradle                                 |
| Database     | H2 (In-Memory)                         |
| JWT Library  | jjwt (io.jsonwebtoken)                 |
| 문서화 도구  | Swagger (springdoc-openapi v2)         |
| 테스트 도구  | JUnit 5                                |

---

## 🧩 주요 기능

| 기능                 | 설명                                                                 |
|----------------------|----------------------------------------------------------------------|
| ✅ 회원가입           | 사용자 정보를 입력받아 계정을 생성합니다. 중복된 아이디는 생성 불가합니다.     |
| ✅ 로그인             | 로그인 시 사용자 정보를 검증하고 Access / Refresh Token을 발급합니다.        |
| ✅ 관리자 권한 부여   | 관리자가 특정 사용자에게 `ADMIN` 권한을 부여할 수 있습니다.                  |
| ✅ 인증 필터 적용     | JWT 인증 필터를 통해 요청마다 토큰을 검증하고 SecurityContext에 유저 정보를 등록합니다. |
| ✅ 권한 기반 접근 제어 | `@PreAuthorize("hasRole('ADMIN')")` 기반 권한 체크 수행                       |
| ✅ 예외처리 일관화    | `CustomException` 및 `GlobalExceptionHandler`를 통해 오류 메시지/코드 통일     |
| ✅ Swagger 문서화     | `/swagger-ui/index.html` 경로에서 명세 확인 및 테스트 가능                   |

---

## 📁 디렉토리 구조

```bash
src
├── domain.auth               # 사용자(User), 권한(Role) 엔티티 및 인증 관련 로직
│   ├── controller            # 인증 관련 API 컨트롤러
│   ├── service               # 인증 비즈니스 로직 처리
│   ├── dto
│   │   ├── request           # 요청 DTO 클래스
│   │   └── response          # 응답 DTO 클래스
│   ├── model                 # User, Role 등 도메인 모델
│   └── repository            # 사용자 저장소 인터페이스
├── global.config             # JWT 설정, Swagger 설정 등 전역 설정
├── global.filter             # JWT 인증 필터 (OncePerRequestFilter 상속)
├── global.exception          # CustomException, ErrorCode, ErrorResponse, 예외 핸들러
└── ...
```

---

## 🔑 인증 방식 설명

### ✅ JWT 인증 흐름

1. 회원가입 → DB에 사용자 저장  
2. 로그인 성공 → Access Token / Refresh Token 발급  
3. 클라이언트는 모든 요청에 `Authorization: Bearer <AccessToken>` 헤더 포함  
4. 서버는 필터에서 JWT 토큰을 검증하고, 유저 정보를 `SecurityContext`에 저장  
5. 권한이 필요한 API에 대해 `@PreAuthorize` 체크 수행  

---

## ❗ 예외 응답 예시

```json
{
  "code": "INVALID_CREDENTIALS",
  "message": "아이디 또는 비밀번호가 올바르지 않습니다."
}
```
## 📑 API 명세 (Swagger)

- 접속 주소: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 주요 엔드포인트

| HTTP  | 경로                                  | 설명                         | 권한   |
|-------|---------------------------------------|------------------------------|--------|
| POST  | `/signup`                             | 회원가입                     | 모두   |
| POST  | `/login`                              | 로그인 (JWT 발급)            | 모두   |
| PATCH | `/admin/users/{username}/roles`       | 관리자 권한 부여             | ADMIN |
| GET   | `/admin/only`                         | 관리자 테스트 전용 API       | ADMIN |

---

## 🧪 테스트 및 예외 처리

- ✅ 회원가입 중복 테스트
- ✅ 로그인 실패 (비밀번호 오류) 테스트
- ✅ 관리자 권한 없는 사용자 접근 시 `403 FORBIDDEN` 응답 확인
- ✅ 잘못된 JWT 토큰 사용 시 `401 INVALID_TOKEN` 응답 확인

---

## 🙋‍♀️ 개발자 정보

| 이름 | 역할                                |
|------|-------------------------------------|
| 정이슬 | 백엔드 전반 개발 (인증, 예외처리, 문서화 등) |

---

## ✨ 향후 개선 방향

- 🔄 Refresh Token을 Redis에 저장하여 토큰 무효화 관리
- 🔄 토큰 재발급(재로그인 없이) 기능 추가
- 🧪 JUnit 기반 전체 테스트 및 Swagger 문서 테스트 자동화
- 🚀 EC2 + Docker 기반 배포 연동

