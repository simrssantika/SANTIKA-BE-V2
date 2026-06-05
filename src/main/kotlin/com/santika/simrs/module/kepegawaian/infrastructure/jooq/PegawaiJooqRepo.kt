package com.santika.simrs.module.kepegawaian.infrastructure.jooq

import com.santika.simrs.global.response.OptionsRes
import com.santika.simrs.module.kepegawaian.dto.response.PegawaiDetailRes
import com.santika.simrs.module.kepegawaian.dto.response.PegawaiListRes
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class PegawaiJooqRepo(private val dsl: DSLContext) {

    // ── table aliases ──────────────────────────────────────────────────────────
    private val p = DSL.table(DSL.name("kepegawaian", "pegawai")).`as`("p")
    private val dep = DSL.table(DSL.name("kepegawaian", "departemen_pegawai")).`as`("dep")
    private val kp = DSL.table(DSL.name("kepegawaian", "kelompok_pegawai")).`as`("kp")
    private val rp = DSL.table(DSL.name("kepegawaian", "resiko_pegawai")).`as`("rp")
    private val dp = DSL.table(DSL.name("kepegawaian", "darurat_pegawai")).`as`("dp")
    private val bp = DSL.table(DSL.name("kepegawaian", "bidang_pegawai")).`as`("bp")
    private val v = DSL.table(DSL.name("kepegawaian", "vendor_tenaga_luar")).`as`("v")
    private val prov = DSL.table(DSL.name("master", "wilayah_provinsi")).`as`("prov")
    private val kab = DSL.table(DSL.name("master", "wilayah_kab_kota")).`as`("kab")
    private val kec = DSL.table(DSL.name("master", "wilayah_kecamatan")).`as`("kec")
    private val kel = DSL.table(DSL.name("master", "wilayah_kelurahan")).`as`("kel")
    private val f = DSL.table(DSL.name("shared", "storage_files")).`as`("f")

    // ── pegawai fields ─────────────────────────────────────────────────────────
    private val pId = DSL.field(DSL.name("p", "id"), UUID::class.java)
    private val pNama = DSL.field(DSL.name("p", "nama"), String::class.java)
    private val pNip = DSL.field(DSL.name("p", "nip"), String::class.java)
    private val pJenisIdentitas = DSL.field(DSL.name("p", "jenis_identitas"), String::class.java)
    private val pIdentitasLain = DSL.field(DSL.name("p", "identitas_lain"), String::class.java)
    private val pNoIdentitas = DSL.field(DSL.name("p", "no_identitas"), String::class.java)
    private val pKelamin = DSL.field(DSL.name("p", "kelamin"), String::class.java)
    private val pJabatan = DSL.field(DSL.name("p", "jabatan"), String::class.java)
    private val pTelephone = DSL.field(DSL.name("p", "telephone"), String::class.java)
    private val pEmail = DSL.field(DSL.name("p", "email"), String::class.java)
    private val pJenjangJabatan = DSL.field(DSL.name("p", "jenjang_jabatan"), String::class.java)
    private val pStatusPegawai = DSL.field(DSL.name("p", "status_pegawai"), String::class.java)
    private val pPendidikan = DSL.field(DSL.name("p", "pendidikan"), String::class.java)
    private val pTempatLahir = DSL.field(DSL.name("p", "tempat_lahir"), String::class.java)
    private val pTanggalLahir = DSL.field(DSL.name("p", "tanggal_lahir"), LocalDate::class.java)
    private val pAlamat = DSL.field(DSL.name("p", "alamat"), String::class.java)
    private val pMulaiBekerja = DSL.field(DSL.name("p", "mulai_bekerja"), LocalDate::class.java)
    private val pFotoId = DSL.field(DSL.name("p", "foto_id"), UUID::class.java)
    private val pIsActive = DSL.field(DSL.name("p", "is_active"), Boolean::class.javaObjectType)
    private val pDeletedAt = DSL.field(DSL.name("p", "deleted_at"))
    private val pKelompokId = DSL.field(DSL.name("p", "kelompok_pegawai_id"), UUID::class.java)
    private val pResikoId = DSL.field(DSL.name("p", "resiko_pegawai_id"), UUID::class.java)
    private val pDaruratId = DSL.field(DSL.name("p", "darurat_pegawai_id"), UUID::class.java)
    private val pDepartemenId = DSL.field(DSL.name("p", "departemen_pegawai_id"), UUID::class.java)
    private val pBidangId = DSL.field(DSL.name("p", "bidang_pegawai_id"), UUID::class.java)
    private val pVendorId = DSL.field(DSL.name("p", "vendor_tenaga_luar_id"), UUID::class.java)
    private val pProvinsiId = DSL.field(DSL.name("p", "provinsi_id"), UUID::class.java)
    private val pKabupatenId = DSL.field(DSL.name("p", "kabupaten_id"), UUID::class.java)
    private val pKecamatanId = DSL.field(DSL.name("p", "kecamatan_id"), UUID::class.java)
    private val pKelurahanId = DSL.field(DSL.name("p", "kelurahan_id"), UUID::class.java)

    // ── departemen fields ──────────────────────────────────────────────────────
    private val depId = DSL.field(DSL.name("dep", "id"), UUID::class.java)
    private val depNama = DSL.field(DSL.name("dep", "nama_departemen"), String::class.java)
    private val depDeletedAt = DSL.field(DSL.name("dep", "deleted_at"))

    // ── kelompok fields ────────────────────────────────────────────────────────
    private val kpId = DSL.field(DSL.name("kp", "id"), UUID::class.java)
    private val kpNama = DSL.field(DSL.name("kp", "nama_kelompok"), String::class.java)
    private val kpDeletedAt = DSL.field(DSL.name("kp", "deleted_at"))

    // ── resiko fields ──────────────────────────────────────────────────────────
    private val rpId = DSL.field(DSL.name("rp", "id"), UUID::class.java)
    private val rpNama = DSL.field(DSL.name("rp", "nama_resiko"), String::class.java)
    private val rpDeletedAt = DSL.field(DSL.name("rp", "deleted_at"))

    // ── darurat fields ─────────────────────────────────────────────────────────
    private val dpId = DSL.field(DSL.name("dp", "id"), UUID::class.java)
    private val dpNama = DSL.field(DSL.name("dp", "nama_emergency"), String::class.java)
    private val dpDeletedAt = DSL.field(DSL.name("dp", "deleted_at"))

    // ── bidang fields ──────────────────────────────────────────────────────────
    private val bpId = DSL.field(DSL.name("bp", "id"), UUID::class.java)
    private val bpNama = DSL.field(DSL.name("bp", "nama_bidang"), String::class.java)
    private val bpDeletedAt = DSL.field(DSL.name("bp", "deleted_at"))

    // ── vendor fields ──────────────────────────────────────────────────────────
    private val vId = DSL.field(DSL.name("v", "id"), UUID::class.java)
    private val vNama = DSL.field(DSL.name("v", "nama_vendor"), String::class.java)
    private val vDeletedAt = DSL.field(DSL.name("v", "deleted_at"))

    // ── wilayah fields ─────────────────────────────────────────────────────────
    private val provId = DSL.field(DSL.name("prov", "id"), UUID::class.java)
    private val provNama = DSL.field(DSL.name("prov", "nama_provinsi"), String::class.java)
    private val provDeletedAt = DSL.field(DSL.name("prov", "deleted_at"))

    private val kabId = DSL.field(DSL.name("kab", "id"), UUID::class.java)
    private val kabNama = DSL.field(DSL.name("kab", "nama_kab_kota"), String::class.java)
    private val kabDeletedAt = DSL.field(DSL.name("kab", "deleted_at"))

    private val kecId = DSL.field(DSL.name("kec", "id"), UUID::class.java)
    private val kecNama = DSL.field(DSL.name("kec", "nama_kecamatan"), String::class.java)
    private val kecDeletedAt = DSL.field(DSL.name("kec", "deleted_at"))

    private val kelId = DSL.field(DSL.name("kel", "id"), UUID::class.java)
    private val kelNama = DSL.field(DSL.name("kel", "nama_kelurahan"), String::class.java)
    private val kelDeletedAt = DSL.field(DSL.name("kel", "deleted_at"))

    // ── file fields (shared.storage_files) ─────────────────────────────────────
    private val fId = DSL.field(DSL.name("f", "id"), UUID::class.java)
    private val fName = DSL.field(DSL.name("f", "original_name"), String::class.java)
    private val fDeletedAt = DSL.field(DSL.name("f", "deleted_at"))

    // ── public methods ─────────────────────────────────────────────────────────

    fun findAll(
        pageable: Pageable,
        search: String?,
        isActive: Boolean?,
        statusPegawai: String?,
        departemenId: UUID?
    ): Slice<PegawaiListRes> {
        val conditions = buildConditions(search, isActive, statusPegawai, departemenId)
        val rows = dsl.select(
            pId, pNip, pNama, pKelamin, pJabatan, pStatusPegawai, pIsActive,
            pDepartemenId, depNama,
            pFotoId, fName
        )
            .from(p)
            .leftJoin(dep).on(pDepartemenId.eq(depId).and(depDeletedAt.isNull))
            .leftJoin(f).on(pFotoId.eq(fId).and(fDeletedAt.isNull))
            .where(conditions)
            .orderBy(pNama)
            .limit(pageable.pageSize + 1)
            .offset(pageable.offset)
            .fetch()

        val hasNext = rows.size > pageable.pageSize
        val content = (if (hasNext) rows.dropLast(1) else rows).map { r ->
            PegawaiListRes(
                id = r[pId],
                nip = r[pNip],
                nama = r[pNama],
                kelamin = r[pKelamin],
                jabatan = r[pJabatan],
                statusPegawai = r[pStatusPegawai],
                isActive = r[pIsActive],
                departemen = r[pDepartemenId]?.let {
                    PegawaiListRes.DepartemenRef(id = it, namaDepartemen = r[depNama])
                },
                foto = r[pFotoId]?.let {
                    PegawaiListRes.FotoRef(id = it, name = r[fName])
                }
            )
        }
        return SliceImpl(content, pageable, hasNext)
    }

    fun findOptions(pageable: Pageable, search: String?): Slice<OptionsRes> {
        val cond = mutableListOf(pDeletedAt.isNull)
        if (!search.isNullOrBlank())
            cond.add(
                DSL.lower(DSL.concat(DSL.coalesce(pNip, DSL.inline("")), DSL.coalesce(pNama, DSL.inline(""))))
                    .like(DSL.lower(DSL.inline("%$search%")))
            )

        val rows = dsl.select(pId, pNip, pNama)
            .from(p).where(cond).orderBy(pNama)
            .limit(pageable.pageSize + 1).offset(pageable.offset).fetch()

        val hasNext = rows.size > pageable.pageSize
        val content = (if (hasNext) rows.dropLast(1) else rows).map { r -> OptionsRes(r[pId], "${r[pNip]} — ${r[pNama]}") }
        return SliceImpl(content, pageable, hasNext)
    }

    fun findById(id: UUID): PegawaiDetailRes? {
        val r = dsl.select(
            pId, pNip, pNama, pJenisIdentitas, pIdentitasLain, pNoIdentitas,
            pKelamin, pTempatLahir, pTanggalLahir, pJabatan, pJenjangJabatan,
            pPendidikan, pTelephone, pEmail, pStatusPegawai, pMulaiBekerja,
            pIsActive, pAlamat,
            pProvinsiId, provNama,
            pKabupatenId, kabNama,
            pKecamatanId, kecNama,
            pKelurahanId, kelNama,
            pKelompokId, kpNama,
            pResikoId, rpNama,
            pDaruratId, dpNama,
            pDepartemenId, depNama,
            pBidangId, bpNama,
            pVendorId, vNama,
            pFotoId, fName
        )
            .from(p)
            .leftJoin(dep).on(pDepartemenId.eq(depId).and(depDeletedAt.isNull))
            .leftJoin(kp).on(pKelompokId.eq(kpId).and(kpDeletedAt.isNull))
            .leftJoin(rp).on(pResikoId.eq(rpId).and(rpDeletedAt.isNull))
            .leftJoin(dp).on(pDaruratId.eq(dpId).and(dpDeletedAt.isNull))
            .leftJoin(bp).on(pBidangId.eq(bpId).and(bpDeletedAt.isNull))
            .leftJoin(v).on(pVendorId.eq(vId).and(vDeletedAt.isNull))
            .leftJoin(prov).on(pProvinsiId.eq(provId).and(provDeletedAt.isNull))
            .leftJoin(kab).on(pKabupatenId.eq(kabId).and(kabDeletedAt.isNull))
            .leftJoin(kec).on(pKecamatanId.eq(kecId).and(kecDeletedAt.isNull))
            .leftJoin(kel).on(pKelurahanId.eq(kelId).and(kelDeletedAt.isNull))
            .leftJoin(f).on(pFotoId.eq(fId).and(fDeletedAt.isNull))
            .where(pId.eq(id).and(pDeletedAt.isNull))
            .fetchOne() ?: return null

        return mapToDetail(r)
    }

    // ── private helpers ────────────────────────────────────────────────────────

    private fun buildConditions(
        search: String?,
        isActive: Boolean?,
        statusPegawai: String?,
        departemenId: UUID?
    ): List<Condition> {
        val conds = mutableListOf(pDeletedAt.isNull)
        if (!search.isNullOrBlank()) {
            conds.add(
                DSL.lower(
                    DSL.concat(
                        DSL.coalesce(pNama, DSL.inline("")),
                        DSL.coalesce(pNip, DSL.inline(""))
                    )
                ).like(DSL.lower(DSL.inline("%$search%")))
            )
        }
        if (isActive != null) conds.add(pIsActive.eq(isActive))
        if (!statusPegawai.isNullOrBlank()) conds.add(pStatusPegawai.eq(statusPegawai))
        if (departemenId != null) conds.add(pDepartemenId.eq(departemenId))
        return conds
    }

    private fun mapToDetail(r: Record) = PegawaiDetailRes(
        id = r[pId],
        nip = r[pNip],
        nama = r[pNama],
        jenisIdentitas = r[pJenisIdentitas],
        identitasLain = r[pIdentitasLain],
        noIdentitas = r[pNoIdentitas],
        kelamin = r[pKelamin],
        tempatLahir = r[pTempatLahir],
        tanggalLahir = r[pTanggalLahir],
        jabatan = r[pJabatan],
        jenjangJabatan = r[pJenjangJabatan],
        pendidikan = r[pPendidikan],
        telephone = r[pTelephone],
        email = r[pEmail],
        statusPegawai = r[pStatusPegawai],
        mulaiBekerja = r[pMulaiBekerja],
        isActive = r[pIsActive],
        alamat = r[pAlamat],
        provinsi = r[pProvinsiId]?.let { PegawaiDetailRes.ProvinsiRef(it, r[provNama]) },
        kabupaten = r[pKabupatenId]?.let { PegawaiDetailRes.KabupatenRef(it, r[kabNama]) },
        kecamatan = r[pKecamatanId]?.let { PegawaiDetailRes.KecamatanRef(it, r[kecNama]) },
        kelurahan = r[pKelurahanId]?.let { PegawaiDetailRes.KelurahanRef(it, r[kelNama]) },
        kelompok = r[pKelompokId]?.let { PegawaiDetailRes.KelompokRef(it, r[kpNama]) },
        resiko = r[pResikoId]?.let { PegawaiDetailRes.ResikoRef(it, r[rpNama]) },
        darurat = r[pDaruratId]?.let { PegawaiDetailRes.DaruratRef(it, r[dpNama]) },
        departemen = r[pDepartemenId]?.let { PegawaiDetailRes.DepartemenRef(it, r[depNama]) },
        bidang = r[pBidangId]?.let { PegawaiDetailRes.BidangRef(it, r[bpNama]) },
        vendorTenagaLuar = r[pVendorId]?.let { PegawaiDetailRes.VendorRef(it, r[vNama]) },
        foto = r[pFotoId]?.let { PegawaiDetailRes.FotoRef(it, r[fName]) },
        dokter = null
    )
}