package com.vibe.infrastructure.configuration

import com.vibe.application.service.UserService
import com.vibe.infrastructure.security.FirebaseFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userService: UserService,
    private val environment: Environment
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors ->
                val source = UrlBasedCorsConfigurationSource()
                val config = CorsConfiguration()
                config.allowedOrigins = listOf("*") // TODO: trocar pelo dominio real
                config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                config.allowedHeaders = listOf("Authorization", "Content-Type", "X-Impersonate-User")
                source.registerCorsConfiguration("/**", config)
                cors.configurationSource(source)
            }
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/v1/public/**").permitAll()
                auth.anyRequest().authenticated()
            }
            .addFilterBefore(
                FirebaseFilter(userService, environment),
                BasicAuthenticationFilter::class.java
            )

        return http.build()
    }
}