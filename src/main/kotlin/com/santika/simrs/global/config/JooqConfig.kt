package com.santika.simrs.global.config

import org.jooq.DSLContext
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class JooqConfig(private val dsl: DSLContext) {
    @EventListener(ApplicationReadyEvent::class)
    fun onStartup() {
        dsl.selectOne().fetch()
    }
}
