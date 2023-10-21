package com.example.study.config.auth;

import com.example.study.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> {
            try {
                csrf.disable()
                        .headers(headers -> headers.frameOptions(option -> option.disable()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        http.authorizeHttpRequests(request ->
                request.requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated());

        http.logout(logout -> logout.logoutSuccessUrl("/"));

        http.oauth2Login(oauth -> oauth.userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService)));
        return http.build();
    }
}
