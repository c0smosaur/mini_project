package api.config;

import api.config.auth.JwtAuthenticationFilter;
import api.config.jwt.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // httpBasic 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // csrf 토큰 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // form login 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/api/members/register",
                                        "api/members/login",
                                        "api/accommodations",
                                        "api/accommodations/{accommodationId}").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                // STATELESS 설정
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // JWT 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                // logout 설정
                .logout(logout -> logout.logoutSuccessHandler(customLogoutSuccessHandler)
                        .logoutUrl("api/member/logout")
                        .logoutSuccessUrl("api/accommodations"))
        ;

        return httpSecurity.build();
    }
}
