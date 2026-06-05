package com.santika.simrs.module.kepegawaian.infrastructure.jooq

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.kepegawaian.dto.response.DokterDetailRes
import com.santika.simrs.module.kepegawaian.dto.response.DokterListRes
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DokterJooqRepo(private val dsl: DSLContext) {

    // ── table aliases ──────────────────────────────────────────────────────────
    private val d = DSL.table(DSL.name("kepegawaian", "dokter")).`as`("d")
    private val s = DSL.table(DSL.name("kepegawaian", "spesialis")).`as`("s")
    private val p = DSL.table(DSL.name("kepegawaian", "pegawai")).`as`("p")

    // ── dokter fields ──────────────────────────────────────────────────────────
    private val dId = DSL.field(DSL.name("d", "id"), UUID::class.java)
    private val dPegawaiId = DSL.field(DSL.name("d", "pegawai_id"), UUID::class.java)
    private val dNamaDokter = DSL.field(DSL.name("d", "nama_dokter"), String::class.java)
    private val dNip = DSL.field(DSL.name("d", "nip"), String::class.java)
    private val dTelephoneDokter = DSL.field(DSL.name("d", "telephone_dokter"), String::class.java)
    private val dEmailDokter = DSL.field(DSL.name("d", "email_dokter"), String::class.java)
    private val dSpesialisId = DSL.field(DSL.name("d", "spesialis_id"), UUID::class.java)
    private val dAlumni = DSL.field(DSL.name("d", "alumni"), String::class.java)
    private val dNoIjinPraktek = DSL.field(DSL.name("d", "no_ijin_praktek"), String::class.java)
    private val dIsActive = DSL.field(DSL.name("d", "is_active"), Boolean::class.javaObjectType)
    private val dDeletedAt = DSL.field(DSL.name("d", "deleted_at"))

    // ── spesialis fields ───────────────────────────────────────────────────────
    private val sId = DSL.field(DSL.name("s", "id"), UUID::class.java)
    private val sNamaSpesialis = DSL.field(DSL.name("s", "nama_spesialis"), String::class.java)
    private val sDeletedAt = DSL.field(DSL.name("s", "deleted_at"))

    // ── pegawai fields (untuk ref) ─────────────────────────────────────────────
    private val pId = DSL.field(DSL.name("p", "id"), UUID::class.java)
    private val pNip = DSL.field(DSL.name("p", "nip"), String::class.java)
    private val pNama = DSL.field(DSL.name("p", "nama"), String::class.java)
    private val pDeletedAt = DSL.field(DSL.name("p", "deleted_at"))

    // alias untuk menghindari ambiguitas nama pada join
    private val pNamaAlias = pNama.`as`("nama_pegawai")
    private val pNipAlias = pNip.`as`("nip_pegawai")

    // ── public methods ─────────────────────────────────────────────────────────

    fun findAll(
        pageable: Pageable,
        search: String?,
        isActive: Boolean?,
        spesialisId: UUID?
    ): Slice<DokterListRes> {
        val conditions = buildConditions(search, isActive, spesialisId)
        val rows = dsl.select(
            dId, dNamaDokter, dNip, dTelephoneDokter, dIsActive,
            dSpesialisId, sNamaSpesialis
        )
            .from(d)
            .leftJoin(s).on(dSpesialisId.eq(sId).and(sDeletedAt.isNull))
            .where(conditions)
            .orderBy(dNamaDokter)
            .limit(pageable.pageSize + 1)
            .offset(pageable.offset)
            .fetch()

        val hasNext = rows.size > pageable.pageSize
        val content = (if (hasNext) rows.dropLast(1) else rows).map { r ->
            DokterListRes(
                id = r[dId],
                namaDokter = r[dNamaDokter],
                nip = r[dNip],
                telephoneDokter = r[dTelephoneDokter],
                isActive = r[dIsActive],
                spesialis = r[dSpesialisId]?.let {
                    DokterListRes.SpesialisRef(id = it, namaSpesialis = r[sNamaSpesialis])
                }
            )
        }
        return SliceImpl(content, pageable, hasNext)
    }

    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes> {
        val cond = mutableListOf(dDeletedAt.isNull)
        if (!search.isNullOrBlank())
            cond.add(
                DSL.lower(DSL.concat(DSL.coalesce(dNip, DSL.inline("")), DSL.coalesce(dNamaDokter, DSL.inline(""))))
                    .like(DSL.lower(DSL.inline("%$search%")))
            )

        val rows = dsl.select(dId, dNip, dNamaDokter)
            .from(d).where(cond).orderBy(dNamaDokter)
            .limit(pageable.pageSize + 1).offset(pageable.offset).fetch()

        val hasNext = rows.size > pageable.pageSize
        val content = (if (hasNext) rows.dropLast(1) else rows).map { r -> OptionsRes(r[dId], "${r[dNip]} — ${r[dNamaDokter]}") }
        return SliceImpl(content, pageable, hasNext)
    }

    fun findById(id: UUID): DokterDetailRes? =
        fetchDetail(dId.eq(id).and(dDeletedAt.isNull))

    /** Dipakai PegawaiService untuk embed dokter di detail pegawai (double query). */
    fun findByPegawaiId(pegawaiId: UUID): DokterDetailRes? =
        fetchDetail(dPegawaiId.eq(pegawaiId).and(dDeletedAt.isNull))

    private fun fetchDetail(condition: Condition): DokterDetailRes? {
        val r = dsl.select(
            dId, dPegawaiId, dNamaDokter, dNip, dTelephoneDokter, dEmailDokter,
            dAlumni, dNoIjinPraktek, dIsActive,
            dSpesialisId, sNamaSpesialis,
            pNamaAlias, pNipAlias
        )
            .from(d)
            .leftJoin(s).on(dSpesialisId.eq(sId).and(sDeletedAt.isNull))
            .leftJoin(p).on(dPegawaiId.eq(pId).and(pDeletedAt.isNull))
            .where(condition)
            .fetchOne() ?: return null

        return mapToDetail(r)
    }

    // ── private helpers ────────────────────────────────────────────────────────

    private fun buildConditions(
        search: String?,
        isActive: Boolean?,
        spesialisId: UUID?
    ): List<Condition> {
        val conds = mutableListOf(dDeletedAt.isNull)
        if (!search.isNullOrBlank()) {
            conds.add(
                DSL.lower(
                    DSL.concat(
                        DSL.coalesce(dNamaDokter, DSL.inline("")),
                        DSL.coalesce(dNip, DSL.inline(""))
                    )
                ).like(DSL.lower(DSL.inline("%$search%")))
            )
        }
        if (isActive != null) conds.add(dIsActive.eq(isActive))
        if (spesialisId != null) conds.add(dSpesialisId.eq(spesialisId))
        return conds
    }

    private fun mapToDetail(r: Record) = DokterDetailRes(
        id = r[dId],
        namaDokter = r[dNamaDokter],
        nip = r[dNip],
        telephoneDokter = r[dTelephoneDokter],
        emailDokter = r[dEmailDokter],
        alumni = r[dAlumni],
        noIjinPraktek = r[dNoIjinPraktek],
        isActive = r[dIsActive],
        spesialis = r[dSpesialisId]?.let {
            DokterDetailRes.SpesialisRef(it, r[sNamaSpesialis])
        },
        pegawai = r[dPegawaiId]?.let {
            DokterDetailRes.PegawaiRef(it, r[pNipAlias], r[pNamaAlias])
        }
    )
}