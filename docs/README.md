## 스프링 Security

이 글은 자바 웹 애플리케이션을 만들 때 사용할 수 있는 스프링 Security에 대해 간단히 알아보려는 목적으로 작성되었습니다. 여기서는 스프링 부트를 사용하지 않고 스프링 프레임워크 5를 사용합니다. 예제의 소스는 [여기](https://github.com/boyd-dev/demo-security/tree/main/example/demog-mvc)에 있습니다. oauth2Login 소스는 [여기](https://github.com/boyd-dev/demo-security/tree/main/example/demog-oauth2)를 참조하세요.

예제 환경은 다음과 같습니다.

- JDK 17
- Spring MVC 5.3.32
- Spring Security 5.8.10
- Servlet 4
- Thymeleaf 3
- Tomcat 9.0.87
- Hibernate 5.3.36.Final
- MySQL 8.0
- Junit 5.9.3
- Gradle 8.6
- IDE - STS 4.20.1

목차

1. [개요](01/README.md)
2. [라이브러리 설정](02/README.md)
3. [스프링 시큐리티 설정](03/README.md)
4. [아이디 패스워드 인증](04/README.md)
5. [OAuth 2.0 인증(I)](05/README.md)
6. [OAuth 2.0 인증(II)](06/README.md)

기타  
- [OAuth2 인증에 관한 오해](99/README.md)

참고  
[Spring Security 5.8 Guide](https://docs.spring.io/spring-security/reference/5.8/)  
[Spring Security 5.8 API docs](https://docs.spring.io/spring-security/site/docs/5.8.x/api/)  
[Spring Security 5.8 Github](https://github.com/spring-projects/spring-security/tree/5.8.x)

