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


