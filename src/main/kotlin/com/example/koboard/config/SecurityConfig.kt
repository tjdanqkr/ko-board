package com.example.koboard.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@EnableWebSecurity
@Configuration
class SecurityConfig (
    private val userDetailsService: UserDetailsService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
){
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.formLogin { it.disable() }
        http.httpBasic { it.disable() }
        http.csrf { it.disable() }

        http.cors { it.configurationSource {
            val config = CorsConfiguration()
            config.allowedOrigins = listOf("*")
            config.allowedMethods = listOf("*")
            config.allowedHeaders = listOf("*")
            config
        } }

        http.authorizeHttpRequests { it
            .requestMatchers("/api/v1/auth/sign-in", "api/v1/auth/sign-up")
            .permitAll()
            .requestMatchers(HttpMethod.GET)
            .permitAll()
            .anyRequest()
            .authenticated()
        }
        http.userDetailsService(userDetailsService)
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build();
    }
}