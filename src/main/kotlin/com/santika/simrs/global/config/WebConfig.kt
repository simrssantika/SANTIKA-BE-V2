package com.santika.simrs.global.config

import com.santika.simrs.global.ratelimit.RateLimitInterceptor
import com.santika.simrs.global.signedurl.SignedUrlInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val rateLimitInterceptor: RateLimitInterceptor,
    private val signedUrlInterceptor: SignedUrlInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(rateLimitInterceptor)
            .addPathPatterns("/api/**")

        // Validasi signed URL global — request ber-signature di /api/** divalidasi otomatis
        registry.addInterceptor(signedUrlInterceptor)
            .addPathPatterns("/api/v1/files/signed{type}/{id}")
    }
}
