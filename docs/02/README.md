## 라이브러리 설정
이번에도 그레이들 dependencies에 필요한 라이브러리들을 추가하는 것으로부터 시작해보겠습니다. [스프링 MVC build.gradle](https://github.com/boyd-dev/demo-mvc/blob/main/example/demog-mvc/build.gradle)과 동일하며 시큐리티와 관련된 다음 2개의 라이브러리를 더 추가합니다. `springSecurityVersion`은 5.8.10으로 하겠습니다.
```
implementation "org.springframework.security:spring-security-web:$springSecurityVersion"
implementation "org.springframework.security:spring-security-config:$springSecurityVersion"

```
다른 라이브러리도 마찬가지겠지만 스프링 시큐리티 버전에 따라 보안 취약점(vulnerability)이 있을 수 있으므로 어떤 내용인지 확인하고 주의가 필요합니다. 라이브러리 레포지토리인 [메이븐 센트럴](https://mvnrepository.com/artifact/org.springframework.security/spring-security-web/5.8.10)에 이러한 정보들이 나와 있습니다.

[처음](../README.md) | [다음](../03/README.md)