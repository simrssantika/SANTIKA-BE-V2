package com.santika.simrs.global.audit

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import tools.jackson.databind.ObjectMapper
import java.util.concurrent.CompletableFuture

@Aspect
@Component
class UserActivityLogger(
    private val auditRepository: AuditRepository,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(UserActivityLogger::class.java)

    @AfterReturning(
        pointcut = "execution(* com.santika.simrs.module.*.service.*.*(..)) " +
                "&& !execution(* *get*(..)) " +
                "&& !execution(* *find*(..)) " +
                "&& !execution(* *list*(..)) " +
                "&& !execution(* *search*(..))",
        returning = "result"
    )
    fun logActivity(joinPoint: JoinPoint, result: Any?) {
        try {
            val attr = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
            val ipAddress = attr?.request?.let {
                it.getHeader("X-Forwarded-For") ?: it.remoteAddr
            } ?: "unknown"

            val authentication = SecurityContextHolder.getContext().authentication
            val username = if (authentication?.isAuthenticated == true) authentication.name ?: "ANONYMOUS"
            else "ANONYMOUS"

            val payload = try {
                joinPoint.args.mapIndexed { i, arg -> "arg$i" to arg }.toMap()
            } catch (e: Exception) {
                mapOf("error" to "Failed to serialize args")
            }

            CompletableFuture.runAsync {
                auditRepository.save(
                    AuditEntity(
                        username = "$username | $ipAddress",
                        action = "${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}",
                        payload = objectMapper.writeValueAsString(payload)
                    )
                )
            }
        } catch (e: Exception) {
            logger.error("Error logging activity", e)
        }
    }
}
