package com.study.webservice.config.auth;


import com.study.webservice.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@RequiredArgsConstructor
@EnableWebSecurity //설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                //h2-console 화면을 사용하기 위해 해당 옵션들 비활성화
                csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    //URL별 권한 관리를 설정하는 옵션의 시작점
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()//모두허용
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) //USER만 허용
                    .anyRequest().authenticated()
                .and()
                    //로그아웃 설정
                    .logout()
                    .logoutSuccessUrl("/") //로그아웃 성공시 이동경로 지정
                .and()
                    //로그인 설정
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService); //로그인 성공 후속 조치를 진행할 인터페이스 구현체 등록
    }
}
