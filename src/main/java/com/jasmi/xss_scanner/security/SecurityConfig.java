package com.jasmi.xss_scanner.security;

import com.jasmi.xss_scanner.exceptions.FailToAuthenticateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {
        http
                .httpBasic(hp -> hp.disable())
                .authorizeHttpRequests(auth -> auth

                                .requestMatchers("/xss_scanner_api/login").permitAll()
                                .requestMatchers("/xss_scanner_api/signup").permitAll()
                                .requestMatchers("/xss_scanner_api/scan_request").permitAll()
                                .requestMatchers("xss_scanner_api/scan_request/*/screenshot").permitAll()
                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/scan_results").permitAll()
                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/scan_result/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/vulnerabilities").permitAll()

                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/scan_requests").hasAnyAuthority("ROLE_INTERNALUSER", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/scan_request/**").hasAnyAuthority("ROLE_INTERNALUSER", "ROLE_ADMIN")

                                .requestMatchers(HttpMethod.POST, "/xss_scanner_api/solution/**").hasAnyAuthority("ROLE_INTERNALUSER", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/solution/**").hasAnyAuthority("ROLE_INTERNALUSER", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/xss_scanner_api/solution/**").hasAnyAuthority("ROLE_INTERNALUSER", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/solutions").hasAnyAuthority("ROLE_INTERNALUSER", "ROLE_ADMIN")

                                .requestMatchers(HttpMethod.POST, "/xss_scanner_api/vulnerability/**").hasAnyAuthority("ROLE_INTERNALUSER", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/vulnerability/**").hasAnyAuthority("ROLE_INTERNALUSER", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/xss_scanner_api/vulnerability/**").hasAnyAuthority("ROLE_INTERNALUSER", "ROLE_ADMIN")

                                .requestMatchers(HttpMethod.DELETE, "/xss_scanner_api/scan_request/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/xss_scanner_api/scan_result/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/xss_scanner_api/vulnerability/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/xss_scanner_api/solution/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/user/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/xss_scanner_api/users").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/xss_scanner_api/user/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/xss_scanner_api/user/**").hasAuthority("ROLE_ADMIN")

                                .anyRequest().denyAll()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder encoder, UserDetailsService userDetailsService) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(encoder);
        return builder.build();
    }
}