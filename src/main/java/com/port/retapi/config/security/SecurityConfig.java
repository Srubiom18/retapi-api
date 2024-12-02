package com.port.retapi.config.security;

import com.port.retapi.config.security.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final UserDetailsServiceImpl userDetailsService;


    public SecurityConfig(JwtTokenFilter jwtTokenFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.userDetailsService = userDetailsService;

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Consfiguración para deshabilitar el CSRF
                .csrf(csrf -> csrf.disable())
                // Usar sesion sin estado ya que estamos utilizando JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configuramos la authorización hacia los endpoint
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/standard/**").hasAnyRole("STANDARD", "ELEVATED", "ADMINISTRATOR")
                        .requestMatchers("/elevated/**").hasAnyRole("ELEVATED", "ADMINISTRATOR")
                        .requestMatchers("/administrator/**").hasAnyRole("ADMINISTRATOR")
                        .anyRequest().authenticated()
                )
                // Añadir el filtro JWT antes de la autenticacion por defecto de Spring security
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
