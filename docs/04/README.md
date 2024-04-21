## 아이디 패스워드 인증
아이디와 패스워드는 가장 일반적이고 보편적인 인증 방식입니다. "로그인"이란 데이터베이스에 저장된 사용자 정보로부터 입력된 사용자를 식별하고 패스워드를 확인하여 인증하는 방식을 떠올리는 경우가 많습니다.  

스프링 시큐리티는 이렇게 사용자가 미리 입력한 정보를 바탕으로 인증하는 방식으로 [아이디 패스워드](https://docs.spring.io/spring-security/reference/5.8/servlet/authentication/passwords/index.html#servlet-authentication-unpwd) 인증을 지원합니다.  

인증을 관리하는 인터페이스로 `AuthenticationManager` 인터페이스가 있습니다. 말 그대로 "인증 관리자"입니다. 그런데 인증이라는 것도 다양한 방식이 있을 수 있으므로 다양한 인증 방식들을 제공하는 여러 `AuthenticationProvider`들이 존재할 수 있습니다. `AuthenticationProvider`를 구현한 [클래스](https://docs.spring.io/spring-security/site/docs/5.8.x/api/org/springframework/security/authentication/AuthenticationProvider.html)들을 보면 시큐리티가 지원하는 인증 방식들을 알 수 있습니다. 

시큐리티는 `AuthenticationManager` 구현체로 `ProviderManager`를 가지고 있습니다. 이 클래스는 생성자 인자로 `AuthenticationProvider`를 받습니다. 데이터베이스(JDBC)로부터 사용자 정보를 확인할 수 있도록 마련된 `AuthenticationProvider`의 구현체는 `DaoAuthenticationProvider`입니다. 따라서 아래와 유사한 예제 [코드](https://docs.spring.io/spring-security/reference/5.8/servlet/authentication/passwords/index.html#customize-global-authentication-manager)를 볼 수 있습니다.

```
@Bean
public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
}
```
위의 코드처럼 `ProviderManager`를 리턴하여 인증 관리자를 만들 수 있게 되는 것입니다.  `DaoAuthenticationProvider`는 실제로 데이터베이스로부터 사용자정보를 가져오는 `UserDetailsService`와 패스워드 인코딩에 필요한 `PasswordEncoder`를 주입받게 됩니다.  

이러한 맥락에서 보면 결국 구현할 것은 `UserDetailsService`와 `PasswordEncoder`인데 시큐리티에서 디폴트로 제공되는 구현체들이 있기 때문에 이것들을 잘 참고하여 커스텀한 인증 관리자를 만들 수 있을 것입니다. 이렇게 만든 `AuthenticationManager`는 `HttpSecurity`의 `authenticationManager`에 설정해 줄 수 있습니다.  

여기서는 가장 간단하게 시큐리티에 있는 `JdbcUserDetailsManager` 클래스를 사용해보겠습니다. 이 클래스는 시큐리티의 디폴트 설정을 그대로 이용하는 `UserDetailsService`입니다. 아래와 같은 빈을 설정해주기만 하면 되겠습니다.

```
@Bean
public UserDetailsService jdbcUserDetailsService() {

		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
		// TODO
		return userDetailsManager;
}
```
데이터베이스 사용자 테이블로부터 사용자 정보를 조회하기 때문에 `DataSource`가 필요합니다. 사용자 테이블 스키마는 스프링 시큐리티가 제공하는 [ddl](https://docs.spring.io/spring-security/reference/5.8/servlet/authentication/passwords/jdbc.html#servlet-authentication-jdbc-schema)을 참고하여 만듭니다. 

패스워드 인코더 역시 기본으로 제공되는 [`DelegatingPasswordEncoder`](https://docs.spring.io/spring-security/site/docs/5.8.x/api/org/springframework/security/crypto/password/DelegatingPasswordEncoder.html)를 사용합니다. 이렇게 기본만을 사용하는 경우 작성하는 코드는 아래와 같은 빈 하나입니다.

```
@Bean
public UserDetailsService jdbcUserDetailsService() {		

		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
		userDetailsManager.setUsersByUsernameQuery("SELECT USER_ID,USER_PASS,TRUE FROM MEMBERS WHERE USER_ID = ?");
		userDetailsManager.setAuthoritiesByUsernameQuery("SELECT USER_ID,ROLE_ID FROM MEMBERS_ROLES WHERE USER_ID = ?");
		
		return userDetailsManager;
}
```
데이터베이스에는 미리 사용자 테이블과 권한(role) 테이블, 데이터를 넣어주어야 합니다. 편의상 패스워드는 인코딩 없이 평문이 그대로 들어가는 방식으로 하겠습니다. 그리고 스프링이 시작될 때 SQL이 실행될 수 있도록 `DataSourceInitializer`를 사용했습니다. <b>이러한 설정은 단지 테스트를 해보려는 것이기 때문에 실제 코드에는 적용하지 말아야 합니다.</b>  

`setUsersByUsernameQuery`와 `setAuthoritiesByUsernameQuery`에 작성해주는 SQL의 select 순서는 맞추어야 합니다. 즉 ID,PASSWORD,ENABLED 순서대로 select 합니다.  

이렇게 하면 스프링 시큐리티 보안 필터에 의해 아이디 패스워드가 일치하는 경우에만 요청한 URL의 컨트롤러가 실행될 수 있습니다.

## SecurityContext
인증 후에 사용자 정보를 가져오려면 아래와 같이 `SecurityContext`를 가져와야 합니다.

```
SecurityContext context = SecurityContextHolder.getContext();
User user = context.getAuthentication().getPrincipal();
```
또는 컨트롤러의 메소드에서 `@AuthenticationPrincipal` 어노테이션을 사용할 수 있습니다.

```
@RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
public String home(Locale locale, Model model, @AuthenticationPrincipal User user) {
 
}
```


[처음](../README.md) | [다음](../05/README.md)