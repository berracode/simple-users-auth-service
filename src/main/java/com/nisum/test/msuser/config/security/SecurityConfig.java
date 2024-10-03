package com.nisum.test.msuser.config.security;


import com.nisum.test.msuser.config.CorsFilter;
import com.nisum.test.msuser.config.security.jwt.JwtAuthenticationEntryPoint;
import com.nisum.test.msuser.config.security.jwt.JwtAuthenticationFilter;
import com.nisum.test.msuser.config.security.jwt.JwtAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private static final String[] AUTH_LIST = {
        "/v3/api-docs/**", "/configuration/ui", "/configuration/security",
        "/swagger-resources/**", "/swagger-ui/index.html", "/swagger-ui.index.html", "/swagger-ui/**", "/v3/api-docs/swagger-config/**",
        "/webjars/**",
        "/users/login", "/users/register", "/h2-console/**", "/favicon.ico"};

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final MyUserDetailServiceAuth userAuthService;
    private final CorsFilter corsFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)));
        jwtAuthenticationFilter.setFilterProcessesUrl("/users/login");
        http.authenticationProvider(authenticationProvider());

        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                    .frameOptions(frameOptions -> frameOptions.sameOrigin())
                    )
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(AUTH_LIST).permitAll()
                    .anyRequest().authenticated()
                                  )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> {
                exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
            })
            .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
            .addFilter(jwtAuthenticationFilter)
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userAuthService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
