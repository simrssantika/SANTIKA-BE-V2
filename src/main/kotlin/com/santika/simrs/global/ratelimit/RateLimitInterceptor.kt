package com.santika.simrs.global.ratelimit

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
class RateLimitInterceptor(
    private val rateLimitRedisTemplate: StringRedisTemplate,
    circuitBreakerRegistry: CircuitBreakerRegistry
) : HandlerInterceptor {

    private val circuitBreaker = circuitBreakerRegistry.circuitBreaker("dragonfly")

    companion object {
        private const val AUTH_LIMIT = 5L
        private const val API_LIMIT = 60L
        private val WINDOW = Duration.ofMinutes(1)
        private val UUID_PATTERN = Regex("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
        private val NUMERIC_PATTERN = Regex("/\\d+")
    }

    private fun normalizeUri(uri: String): String =
        uri.replace(UUID_PATTERN, "{id}")
           .replace(NUMERIC_PATTERN, "/{id}")

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val ip = request.getHeader("X-Forwarded-For")?.split(",")?.first()?.trim()
            ?: request.remoteAddr
        val method = request.method
        val endpoint = normalizeUri(request.requestURI)
        val isAuth = request.requestURI.startsWith("/api/v1/auth")
        val key = "rl:$ip:$method:$endpoint"
        val limit = if (isAuth) AUTH_LIMIT else API_LIMIT

        return try {
            val count = circuitBreaker.executeSupplier {
                val ops = rateLimitRedisTemplate.opsForValue()
                val c = ops.increment(key) ?: 1L
                if (c == 1L) rateLimitRedisTemplate.expire(key, WINDOW)
                c
            }

            if (count <= limit) {
                response.setHeader("X-Rate-Limit-Remaining", (limit - count).toString())
                true
            } else {
                val ttl = rateLimitRedisTemplate.getExpire(key, TimeUnit.SECONDS)
                response.status = HttpStatus.TOO_MANY_REQUESTS.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                response.setHeader("Retry-After", ttl.toString())
                response.writer.write(
                    """{"status":429,"message":"Terlalu banyak permintaan. Coba lagi dalam $ttl detik"}"""
                )
                false
            }
        } catch (_: CallNotPermittedException) {
            // Circuit terbuka - DragonFly tidak tersedia, izinkan request lewat
            true
        } catch (_: Exception) {
            // Redis error - fail-open agar app tetap jalan
            true
        }
    }
}
