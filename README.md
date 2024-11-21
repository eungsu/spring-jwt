# JWT(Json Web Token)
JWT(Json Web Token)는 **인증에 필요한 정보를 암호화시킨 JSON 토큰**을 말한다.  
JWT는 서버에 의해 **전자 서명(Digital Signature)된 토큰** 중 하나다.
> 전자서명이란 서명하고 싶은 메세지를 해시함수를 이용해 축약한 후 개인키로 암호화했을 대 나오는 값을 의미한다.

## JWT의 구조
JWT는 ```Header```.```Payload```.```Signature```로 구성된다.  
각 부분은 **Base64 URL-safe Encode**로 인코딩되어 표현된다.
> Base64 Encode는 바이너리 데이터를 텍스트 데이터로 바꾸는 인코딩 방식의 하나다. 바이너리 데이터를 ASCII(아스키코드)영역의 문자로만 이루어진 문자열로 바꾸는 인코딩 방식이다.

> Base64 URL-safe Encode는 일반적인 Base64 Encode에서 URL에서 오류없이 사용하도록 '+', '/'를 각각 '-', '_'로 표현한 것이다.

JWT 토큰 샘플
```
eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJob25nIiwiaWF0IjoxNzMyMDE5NTc4LCJleHAiOjE3MzIwMjA0Nzh9.K7frXFtUFuqEC4ujX-137pccznEDkboZ5RRKT-uZ7v1TyQ4NPJGq7jpMaNNZeKYvBAkoDEth4I99m9jQf5qdGA
```

### Header
JWT의 헤더는 토큰의 타입과 사용된 서명 알고리즘을 정의하는 JSON 객체다.
JWT 헤더는 아래와 같은 형식을 사용한다.

```
eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9
```

```json
{
    "alg": "HS512",
    "typ": "JWT"
}
```

### Payload
Payload에는 JWT에서 **클레임(Claim)**이라고 불리는 데이터가 포함되는 부분이다.  
클레임에는 인증에 필요한 사용자 정보나 권한 정보를 포함하고 있다.

#### 클레임의 유형
- 등록된 클레임(Registered Claims) : 표준 클레임으로 권장되는 이름을 가진다.
  - iss : 발행자(issuer)
  - sub : 사용자 식별자(subject)
  - aud : 대상(audience)
  - exp : 만료 시간(expiration time)
  - iat : 발행 시간(issued at)
  - nbf : 활성 시간(not before)
- 공개 클레임(Public Claims) : 사용자 정의 클레임으로 추가 정보를 포함한다.
- 비공개 클레임(Private Claims) : 특정 애플리케이션 간의 협약으로 사용한다.

```
eyJzdWIiOiJob25nIiwiaWF0IjoxNzMyMDE5NTc4LCJleHAiOjE3MzIwMjA0Nzh9
```

```json
{
    "sub": "hong",
    "iat": 1732019578,
    "exp": 1732020478
}
```

### Signature
서명을 토큰이 변조되지 않았음을 보장하며, Header와 Payload를 결합하고, 개인 키로 생성된다.

```
K7frXFtUFuqEC4ujX-137pccznEDkboZ5RRKT-uZ7v1TyQ4NPJGq7jpMaNNZeKYvBAkoDEth4I99m9jQf5qdGA
```

```
HMACSHA512(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  tvBOmQXZMS1U4je7lM7tI0vIbBPszz13NahgFFe8yx0gjz8HwqpPhUMbvRuATTvXmbvMFHVmbqc3tvl3Rn3JMA4JcnjsTei4
)
```

## JWT를 이용한 인증 과정
```mermaid
sequenceDiagram
    클라이언트->>서버: 1.username과 password로 로그인한다.
    서버-->>클라이언트: 2.사용자 인증 후 엑세스토큰을 발급한다.
    클라이언트-->서버: 3.엑세스토큰을 사용하여 API을 요청한다.
    서버-->>클라이언트: 4.엑세스토큰이 문제가 없으면 요청을 처리하고 응답을 보낸다.
```

1. 클라이언트 인증 요청 : 클라이언트가 username과 password를 서버로 보내고 인증올 요청한다.
2. JWT의 발급 : 서버는 클라이언트가 전달한 username과 password를 사용해서 사용자를 인증하고, 인증된 정보를 기반으로 JWT를 생성하고, 클라이언트에게 응답으로 보낸다.
3. JWT의 활용 : 클라이언트는 서버로 요청을 보낼 때 JWT를 같이 보낸다. 보통 클라이언트는 HTTP 요청 헤더에 ```  Authorization: Bearer JWT  ```로 포함하여 서버로 보낸다.
4. 서버의 검증 : 서버는 클라이언트로부터 받은 JWT의 서명을 확인하고, 페이로드를 디코딩하여 사용자정보를 알아내서 사용한다.

## JWT의 장점과 단점
### 장점
1. 무상태 인증 : 서버는 클라이언트 상태를 유지할 필요가 없으며, 토큰에 모든 정보가 포함된다.
2. 확장성 : 서버가 수평적으로 확장되더라도 인증 상태를 중앙에서 관리할 필요가 없다.
3. 유연성 : 다양한 클레임을 페이로드에 포함할 수 있어 여러 상황에 적합하다.
4. 보안성 : 전자 서명을 통해 토큰이 변조되지 않았음을 확인할 수 있다.
5. 다양한 플랫폼 지원 : JSON 형시과 HTTP 기반의 인증 방식이기 때문에 거의 모든 프로그래밍 언어와 플랫폼에서 쉽게 구현할 수 있다.

### 단점
1. 토큰 크기 : 토큰에 정보가 많아질 수록 토큰의 길이가 늘어나 네트워크에 부하를 줄 수 있다.
2. 만료 전 탈취 위험 : JWT는 만료 시간 전까지 유효하기 때문에, 이 기간 동안 탈취되면 위험할 수 있다.
3. 갱신 문제 : 한 번 발급된 JWT는 수정이 불가능 하기 때문에 만료 시간 이전에 무효화 시키기 어렵다.


## Spring JWT 샘플
이 애플리케이션은 spring boot, spring web, spring security, jjwt, h2, spring data jpa 등을 활용해서 토큰 인증방식의 REST API 애플리케이션이다.

주요 기능 : 회원가입, 로그인, 할일 등록, 할일 조회, 할일 수정, 할일 삭제   

### 프로젝트 정보
- spring boot : 3.1.1
- java : 17
- build : maven
- packaging : jar
- groupId : com.example
- artifactId : spring-jwt

#### 패키지 구조
- controller : 클라이언트의 요청을 처리하는 컨트롤러 클래스를 포함하는 패키지다.
  - AuthController.java : 사용자를 인증하고 JWT 토큰을 발행
  - TodoController.java : 할일 등록, 조회, 수정, 삭제 요청을 처리
  - UserController.java : 회원가입 요청을 처리
- model : 엔티티 정보를 표현하는 클래스를 포함하는 패키지다.
  - ERole.java : 권한정보
  - Todo.java : 할일 정보
  - User.java : 사용자 정보
- payload : 요청 데이터와 응답 데이터를 표현하는 클래스를 포함하는 패키지다.
  - auth : 인증에 필요한 정보를 표현하는 클래스를 포함하는 패키지다.
    + AuthRequest.java : 로그인 요청정보(username, password)
    + AuthResponse.java : JWT 토큰 응답정보(grantType, accessToken, refreshToken)
    + RefreshTokenRequest.java : 토큰 재발급 요청정보(refreshToken)
  - todo : 할일 등록, 조회, 수정에 필요한 정보를 표현하는 클래스를 포함하는 패키지다.
    + AddTodoRequest.java : 신규 할일 등록 요청정보
    + AddTodoResponse.java : 신규 할일 등록 응답정보
    + TodoResponse.java : 할일 조회 응답정보
    + UpdateTodoRequest.java : 할일 수정 요청정보
  - user : 회원가입에 필요한 정보를 표현하는 클래스를 포함하는 패키지다.
    + SignupUserRequest.java : 회원가입 요청정보
    + SignupUserResponse.java : 회원가입 응답정보
  - Response.java : 공통 응답정보
- repository : 데이터베이스 엑세스 작업을 표현하는 인터페이스를 포함하는 패키지다.
  - TodoRepository.java : 할일 정보 CRUD 제공
  - UserRepository.java : 사용자 정보 CRUD 제공
- security : 시큐리티 및 jwt 관련 클래스를 포함하는 패키지다.
  - JwtAuthenticationFilter.java : JWT를 활용해 사용자 인증을 처리하는 사용자정의 필터다.
  - JwtUtils.java : JWT 토큰을 생성, 검증, 정보 추출 기능을 제공하는 유틸리티 클래스다.
  - SecurityConfig.java : 스프링 시큐리티 설정 클래스다.
- service : 업무로직을 제공하는 클래스를 포함하는 패키지다.
  - AuthSerivce.java : 인증 서비스 제공 
  - TodoService.java : 할일 등록, 수정, 조회, 삭제 서비스 제공
  - UserService.java : 회원가입 서비스 제공
- AppConfig.java : 애플리케이션 실행에 필요한 빈을 등록시키는 클래스
- SpringJwtApplication.java : 스프링 부트 애플리케이션의 핵심 클래스

## JWT 로그인 요청

아이디/비밀번호를 전달하고, 엑세스 토큰을 발급받는다.

```mermaid
sequenceDiagram
  participant A as Client
  participant F as JwtAuthenticationFilter
  participant U as JwtUtil
  participant C as AuthController
  participant S as AuthService
  A->>C: 1. 로그인 요청<br>(with username/passsword)
  C->>S: 2. 사용자 인증
  S-->>U: 3. AccessToken 생성 요청
  U->>S: 4. AccessToken 반환
  S-->>C: 5. AccessToken 발급
  C-->>A: 6. AccessToken 응답
```

1. 요청 메세지에 아이디/비밀번호를 포함시켜 서버로 로그인 요청을 보낸다.
2. AuthRequest객체로 아이디/비밀번호를 받아서 AuthService.login(AuthRequest)를 호출한다.
3. AuthRequest로 전달받은 아이디/비밀번호로 사용자를 인증 작업을 수행하고, AccessToken을 생성을 요청한다.
4. JwtUtil은 AccessToken을 생성해서 반환한다.
5. AuthResponse객체에 AccessToken을 담아서 반환한다.
6. 응답을 보낸다.

## JWT 인증 
```mermaid
sequenceDiagram
  participant A as Client
  box  인증(Authentication) 작업
  participant S as SecurityContext
  participant F as JwtAuthenticationFilter
  participant U as JwtUtil
  end
  
  box  인가(Authorization) 작업
  participant AF as AuthorizationFilter
  participant D as AuthorizationManager
  end

  participant C as Controller

  A->>F: 1. AccessToken 전달
  F->>U: 2. AccessToken 검증 요청
  U-->>F: 3. AccessToken 검증 결과 반환
  F->>U: 4. AccessToken에서 사용자정보(아이디, 권한) 추출
  U-->>F: 5. 사용자장보(아이디, 권한) 반환
  F-->>S: 6. 사용자정보로 Authentication 생성<br>SecurityContext에 저장 

  AF-->>S: 7. 사용자 인증정보를 참조하기 위해서 Authentication객체 요청
  AF->>D: 8. Authentication객체를 전달해서 인가작업  위임
  D->>C: 9. 접근권한이 있으면 사용자의 요청 처리
  C-->>A: 10. 응답데이터 전달
```

1. 클라이언트는 요청을 할 때마다 AccessToken을 요청헤더에 포함시켜 보낸다.
2. JwtAuthenctionFiler는 요청헤더에서 AccessToken를 가져온다. JwtUtil에 AccessToken의 유효성 검증을 요청한다.
3. JwtUtil은 검증결과를 반환한다.
4. JwtAuthenticationFilter은 JwtUtil에 사용자정보 추출을 요청한다.
5. JwtUtil은 사용자아이디, 권한정보를 추출해서 반환한다.
6. JwtAuthenticationFilter는 AccessToken에서 추출한 사용자 정보(아아디와 권한)로 Authentication객체를 생성하고, SecurityContext에 저장한다.
7. AuthorizationFilter는 인가작업 수행에 필요한 사용자정보가 포함된 Authentication객체를 가져온다.
8. AuthoriztionFilter는 AuthorizationManager에 Authorization을 전달해서 인가 작업을 위임한다.
9. AuthorizationManager는 접근권한을 가지고 있는지 검사하고, 접근권한이 있으면 사용자의 요청을 Spring MVC로 보낸다.
10. Spring MVC의 컨트롤러는 사용자의 요청을 처리하고 응답을 클라이언트로 보낸다.
