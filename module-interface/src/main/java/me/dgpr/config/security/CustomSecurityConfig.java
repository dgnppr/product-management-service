package me.dgpr.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import me.dgpr.domains.manager.usecase.QueryLogoutByTokenUseCase;
import me.dgpr.domains.manager.usecase.QueryManagerByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfig {

    private final JwtTokenProperties jwtTokenProperties;
    private final ObjectMapper objectMapper;
    private final JwtTokenHandler jwtTokenHandler;
    private final QueryManagerByIdUseCase queryManagerByIdUseCase;
    private final QueryLogoutByTokenUseCase queryLogoutByTokenUseCase;

    public CustomSecurityConfig(
            JwtTokenProperties jwtTokenProperties,
            ObjectMapper objectMapper,
            JwtTokenHandler jwtTokenHandler,
            QueryManagerByIdUseCase queryManagerByIdUseCase,
            QueryLogoutByTokenUseCase queryLogoutByTokenUseCase
    ) {
        this.jwtTokenProperties = jwtTokenProperties;
        this.objectMapper = objectMapper;
        this.jwtTokenHandler = jwtTokenHandler;
        this.queryManagerByIdUseCase = queryManagerByIdUseCase;
        this.queryLogoutByTokenUseCase = queryLogoutByTokenUseCase;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .cors(it -> it.configurationSource(corsConfigurationSource()))
                .sessionManagement(
                        it -> it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        it -> it.requestMatchers("/v*/managers", "/v*/login").permitAll()
                                .requestMatchers("/v*/**").authenticated()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenProperties.getSecretKey(),
                                jwtTokenHandler,
                                queryManagerByIdUseCase,
                                queryLogoutByTokenUseCase
                        ), UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(
                        it -> it.authenticationEntryPoint(
                                new CustomAuthenticationEntryPoint(objectMapper)
                        )
                )
                .build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
