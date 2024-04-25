## OAUTH 2.0
["OAuth 2.0 Authorization Framework"](https://datatracker.ietf.org/doc/html/rfc6749)은 사용자의 정보를 제 3자(다른 애플리케이션)에게 제공하기 위해 해당 정보의 소유자로부터 그 사용을 안전하게 "허가(authorization)"받는 규칙을 정하는 프로토콜입니다.

흔히 OAuth 2를 "소셜 로그인 기능"이라고 하는데, 구글이나 네이버 같은 서비스가 가지고 있는 사용자 정보를 다른 애플리케이션에서 접근하여 자신의 로그인을 위해 그것을 활용할 수 있기 때문입니다. 여기서 유념할 것은 OAuth 2는 사실 "authorization"에 관한 프로토콜이고 로그인과 같은 "authentication"을 규정하는 것은 아니라는 점입니다. 이 두 가지를 항상 명확히 구분할 필요는 없지만 차이가 있습니다.  

예를 들어 구글의 사용자 정보를 내가 만든 웹 애플리케이션이 이용하기 위해서는 사용자로부터 허가를 받아야 하는데, 이때 발급되는 "access token"이 어떤 식으로든 유출되면 사용자 정보에 누구나 접근할 수 있는 위험이 있습니다. OAuth 2 자체는 access token이 정상적인 인증을 거쳐 발급된 것인지 여부에 상관없이 그것을 접근 권한으로 판단합니다. OAuth 2 과정에서 발급되는 토큰들을 보안적으로 어떻게 관리하고, 또 획득한 정보를 어디에 저장하고 언제 폐기할지는 순전히 개발자들에게 달려 있습니다(그래서 여러 가지 고민을 하는 경우가 많습니다).

스프링 시큐리티는 OAuth 2를 지원하는 다양한 인터페이스와 구현체들을 제공합니다. 단순히 로그인으로 이용하는 것을 포함해서 사용자 정보를 제 3자에게 제공하는 서버를 만들 수도 있습니다.

이 예제에서는 로그인 기능을 위해 스프링 시큐리티를 이용해보려고 합니다. 스프링 시큐리티 공식 문서에는 "OAuth2 Log In"과 "OAuth2 Client"가 [구분](https://docs.spring.io/spring-security/reference/5.8/servlet/oauth2/index.html)되어 있습니다. 앞서 말한 것처럼 내가 만드는 애플리케이션에서 단지 인증의 용도로 이용할 것인지 아니면 구글이나 네이버가 제공하는 사용자 정보를 OAuth 2 프로토콜에 맞추어 이용할 것인지 구분하는 것으로 볼 수 있습니다. 여기서는 "OAuth2 Log In"을 중심으로 설명합니다. 공식 문서에서 설명한 것처럼 로그인 기능으로 활용하는 경우 OAuth 2 스펙의 "Authorization Code Grant"의 흐름을 준수합니다.

>OAuth 2.0 Login is implemented by using the Authorization Code Grant, as specified in the OAuth 2.0 Authorization Framework and OpenID Connect Core 1.0.


## OAuth2 로그인
로그인 기능으로 활용할 때 OAuth 2의 "Authorization Code Grant" 흐름에 따라 진행됩니다. OAuth 2 공식 문서의 그림을 그대로 옮겨 보겠습니다. 

![fig03](../img/fig03.png)

Resource Owner는 사용자를 말합니다. 사용자는 구글이나 네이버처럼 사용자 정보를 가진 Authorization Server에 인증을 하면서 Authorization Code를 받습니다. 이 코드를 제 3자 애플리케이션인 Client에게 주면 Client는 이 코드를 가지고 Access Token을 발급 받게됩니다. Access Token을 가지면 Resource Owner의 사용자 정보에 접근할 수 있게 되는 것입니다. 이러한 "Authorization Code Grant"에서는 <b>Refresh Token이 필수가 아닙니다.</b>  

좀더 자세한 설명은 [여기](https://datatracker.ietf.org/doc/html/rfc6749#section-4.1)를 참고하세요. 소셜 로그인은 보기에는 간단하지만 뒤에서 일어나는 일은 다소 복잡합니다. 스프링 시큐리티는 이러한 과정을 단순화시키는 역할을 합니다. 

## 라이브러리 
과거 스프링에서는 "Spring Security OAuth2" 개발은 시큐리티와 별개로 진행되었습니다. 그래서 스프링 시큐리티 4.x는 [`spring-security-oauth2`](https://mvnrepository.com/artifact/org.springframework.security.oauth/spring-security-oauth2/2.5.2.RELEASE) 디펜던시를 추가해야 했습니다. 하지만 스프링 시큐리티 5부터 시큐리티에 통합되었기 때문에 이것이 필요하지 않습니다. 다만 "OAuth2 Client"의 기능 때문에 `spring-security-oauth2-client` 라이브러리가 있어야 합니다. 그리고 json 컨버터로 사용할 `jackson-databind`도 추가합니다(토큰과 사용자 정보가 json 형태로 전송되기 때문에).

```
implementation "org.springframework.security:spring-security-oauth2-client:$springSecurityVersion"
implementation 'com.fasterxml.jackson.core:jackson-databind:2.16.1'
```
인터넷에서 찾게 되는 시큐리티 관련 자료들이 과거와 현재의 디펜던시, 설정, 버전별 차이점, 또는 다양성 등으로 뒤섞여 있어서 혼란스러운 점이 많습니다. 여기서는 시큐리티(v5.8) 공식 문서를 기준으로 설명합니다.


[처음](../README.md) | [다음](../06/README.md)
