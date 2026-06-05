package com.santika.simrs.module.kepegawaian.infrastructure.jooq

import com.santika.simrs.module.kepegawaian.dto.response.DokumenPegawaiRes
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class DokumenPegawaiJooqRepo(private val dsl: DSLContext) {

    // ── table aliases ──────────────────────────────────────────────────────────
    private val dk = DSL.table(DSL.name("kepegawaian", "dokumen_pegawai")).`as`("dk")
    private val sf = DSL.table(DSL.name("shared", "storage_files")).`as`("sf")

    // ── dokumen fields ─────────────────────────────────────────────────────────
    private val dkId = DSL.field(DSL.name("dk", "id"), UUID::class.java)
    private val dkPegawaiId = DSL.field(DSL.name("dk", "pegawai_id"), UUID::class.java)
    private val dkNamaDokumen = DSL.field(DSL.name("dk", "nama_dokumen"), String::class.java)
    private val dkFileId = DSL.field(DSL.name("dk", "file_id"), UUID::class.java)
    private val dkIsActive = DSL.field(DSL.name("dk", "is_active"), Boolean::class.javaObjectType)
    private val dkDeletedAt = DSL.field(DSL.name("dk", "deleted_at"))

    // ── file fields (shared.storage_files) ─────────────────────────────────────
    private val sfId = DSL.field(DSL.name("sf", "id"), UUID::class.java)
    private val sfName = DSL.field(DSL.name("sf", "original_name"), String::class.java)
    private val sfDeletedAt = DSL.field(DSL.name("sf", "deleted_at"))

    fun findAllByPegawaiId(pegawaiId: UUID): List<DokumenPegawaiRes> =
        dsl.select(dkId, dkNamaDokumen, dkIsActive, dkFileId, sfId, sfName)
            .from(dk)
            .leftJoin(sf).on(dkFileId.eq(sfId).and(sfDeletedAt.isNull))
            .where(dkPegawaiId.eq(pegawaiId).and(dkDeletedAt.isNull))
            .orderBy(dkNamaDokumen)
            .fetch()
            .map { r ->
                DokumenPegawaiRes(
                    id = r[dkId],
                    namaDokumen = r[dkNamaDokumen],
                    isActive = r[dkIsActive] ?: true,
                    file = r[dkFileId]?.let { DokumenPegawaiRes.FileRef(r[sfId], r[sfName]) }
                )
            }
}
