
package com.pbw.application.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Make sure public resources are accessible
                .requestMatchers("/", "/**", "/script/**", "/css/**", "/js/**").permitAll()
                // Configure other paths as needed
                .anyRequest().authenticated()
            )
            // Disable CSRF if this is just a test/dev environment
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
