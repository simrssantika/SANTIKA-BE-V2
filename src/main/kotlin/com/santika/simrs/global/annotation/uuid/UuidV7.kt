package com.santika.simrs.global.annotation.uuid

import com.github.f4b6a3.uuid.UuidCreator
import org.hibernate.annotations.IdGeneratorType
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.generator.BeforeExecutionGenerator
import org.hibernate.generator.EventType
import org.hibernate.generator.EventTypeSets
import java.util.*

/**
 * Generator id UUID Version 7 (RFC 9562). UUIDv7 itu time-ordered (prefix timestamp
 * milidetik + suffix acak), jadi nilainya monoton naik → jauh lebih ramah index B-tree
 * dibanding UUIDv4 acak saat tabel membesar (insert tidak memencar halaman index).
 *
 * Nilai dibuat di aplikasi SEBELUM INSERT lewat uuid-creator
 * ([UuidCreator.getTimeOrderedEpoch] → langsung [java.util.UUID]), bukan default
 * Hibernate yang masih UUIDv4. Bila id sudah diisi manual (mis. impor data/test),
 * nilai itu dipertahankan.
 */
class UuidV7Generator : BeforeExecutionGenerator {

    override fun generate(
        session: SharedSessionContractImplementor,
        owner: Any?,
        currentValue: Any?,
        eventType: EventType?
    ): Any = currentValue ?: UuidCreator.getTimeOrderedEpoch()

    override fun getEventTypes(): EnumSet<EventType> = EventTypeSets.INSERT_ONLY
}

/**
 * Tempel pada field `@Id` bertipe [java.util.UUID] untuk dapat UUIDv7 otomatis,
 * TANPA perlu `@GeneratedValue`. Contoh:
 * ```
 * @Id
 * @UuidV7
 * var id: UUID? = null
 * ```
 */
@IdGeneratorType(UuidV7Generator::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class UuidV7
