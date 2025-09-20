package com.taskflow.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        // Collaborator endpoints
                        .requestMatchers(HttpMethod.POST, "/collaborators").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/collaborators/**").hasAnyAuthority("ADMIN", "MANAGER", "COLLABORATOR")
                        .requestMatchers(HttpMethod.PATCH, "/collaborators/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/collaborators/**").hasAuthority("ADMIN")
                        // Vacation endpoints
                        .requestMatchers(HttpMethod.POST, "/vacations").hasAnyAuthority("ADMIN", "MANAGER", "COLLABORATOR")
                        .requestMatchers(HttpMethod.PUT, "/vacations/**").hasAnyAuthority("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/vacations/**").hasAnyAuthority("ADMIN", "MANAGER", "COLLABORATOR")
                        .requestMatchers(HttpMethod.PATCH, "/vacations/**").hasAnyAuthority("ADMIN", "MANAGER", "COLLABORATOR")
                        .requestMatchers(HttpMethod.GET, "/vacations/**").hasAnyAuthority("ADMIN", "MANAGER", "COLLABORATOR")
                        //swagger
                        .requestMatchers("/v3/api-docs/**","/swagger-ui.html", "swagger-ui/**").permitAll()

                        .anyRequest()
                        .authenticated()
                ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
