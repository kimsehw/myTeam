package com.kimsehw.myteam.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);*/

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new CookieCsrfTokenRepository())   // csrf 토큰 저장소를 http only cookie로 설정
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(auto -> auto
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/", "/members/**", "/images/**", "/test/**").permitAll()
                        .requestMatchers("/team/**").hasAnyRole("MEMBER", "LEADER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/members/login")
                        .loginProcessingUrl("/members/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        // AJAX 요청 시 json 응답, 로그인 페이지 폼 요청 시 로그인 페이지 렌더링
                        .successHandler((request, response, authentication) -> {
                            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) { // AJAX 요청 확인
                                response.setStatus(HttpServletResponse.SC_OK); // 모달 로그인 성공 시 200 응답
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                        .failureHandler((request, response, exception) -> {
                            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) { // AJAX 실패 처리
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json; charset=UTF-8");
                                response.getWriter().write("{\"error\": \"로그인 실패: 아이디 또는 비밀번호가 틀렸습니다.\"}");
                            } else { // 폼 로그인 실패 시 리다이렉트
                                response.sendRedirect("/members/login/error");
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/")
                )
                /*.exceptionHandling(exception -> exception
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        //인증 되지 않은 사용자가 리소스 접근 시 수행 되는 핸들러 등록
                )*/;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
