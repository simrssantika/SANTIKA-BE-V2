package com.santika.simrs.global.security

import com.santika.simrs.global.jwt.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableMethodSecurity
class SecurityConfiguration(
    private val jwtAuthFilter: JwtAuthFilter,
    private val customAuthEntryPoint: CustomAuthEntryPoint,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // Token disimpan di cookie HttpOnly → rentan CSRF, jadi aktifkan proteksi CSRF
        // double-submit cookie. XSRF-TOKEN dibuat non-HttpOnly agar FE bisa membacanya
        // dan mengirim balik lewat header X-XSRF-TOKEN. Handler plain (bukan XOR) supaya
        // nilai cookie == nilai header (cocok untuk SPA, bukan form server-rendered).
        http.csrf { csrf ->
            csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            csrf.csrfTokenRequestHandler(CsrfTokenRequestAttributeHandler())
            csrf.ignoringRequestMatchers(
                "/api/v1/auth/login",
                "/api/v1/auth/csrf",
                "/api/v1/files/**",
                "/actuator/**"
            )
        }
            .headers {
                it.frameOptions { fo -> fo.sameOrigin() }
                it.xssProtection { xss -> xss.disable() }
                it.contentTypeOptions {}
                it.httpStrictTransportSecurity {}
                it.contentSecurityPolicy { csp -> csp.policyDirectives("default-src 'self'") }
                it.referrerPolicy { rp ->
                    rp.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                }
            }
            .cors { cors ->
                cors.configurationSource {
                    CorsConfiguration().apply {
                        allowedOrigins = listOf(
                            "http://localhost:3000",
                            "http://localhost:5173",
                            "http://192.168.1.104:5173",
                            "http://192.168.1.117:5012",
                            "http://192.168.1.117:5013"
                        )
                        allowedMethods = listOf("*")
                        allowedHeaders = listOf("*")
                        allowCredentials = true
                    }
                }
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/error/**",
                    "/static/**",
                    "/assets/**",
                    "/webjars/**",
                    "/api/v1/auth/**",
                    "/api/v1/files/**",
                    "/actuator/**"
                ).permitAll()
                it.anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { ex ->
                ex.accessDeniedHandler(customAccessDeniedHandler)
                ex.authenticationEntryPoint(customAuthEntryPoint)
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
