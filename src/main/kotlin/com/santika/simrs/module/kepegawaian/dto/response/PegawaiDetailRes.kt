package com.santika.simrs.module.kepegawaian.dto.response

import java.time.LocalDate
import java.util.UUID

data class PegawaiDetailRes(
    val id: UUID?,
    val nip: String?,
    val nama: String?,
    val jenisIdentitas: String?,
    val identitasLain: String?,
    val noIdentitas: String?,
    val kelamin: String?,
    val tempatLahir: String?,
    val tanggalLahir: LocalDate?,
    val jabatan: String?,
    val jenjangJabatan: String?,
    val pendidikan: String?,
    val telephone: String?,
    val email: String?,
    val statusPegawai: String?,
    val mulaiBekerja: LocalDate?,
    val isActive: Boolean?,
    val alamat: String?,
    val provinsi: ProvinsiRef?,
    val kabupaten: KabupatenRef?,
    val kecamatan: KecamatanRef?,
    val kelurahan: KelurahanRef?,
    val kelompok: KelompokRef?,
    val resiko: ResikoRef?,
    val darurat: DaruratRef?,
    val departemen: DepartemenRef?,
    val bidang: BidangRef?,
    val vendorTenagaLuar: VendorRef?,
    val foto: FotoRef?,
    val dokter: Any? = null
) {
    data class ProvinsiRef(val id: UUID?, val namaProvinsi: String?)
    data class KabupatenRef(val id: UUID?, val namaKabKota: String?)
    data class KecamatanRef(val id: UUID?, val namaKecamatan: String?)
    data class KelurahanRef(val id: UUID?, val namaKelurahan: String?)
    data class KelompokRef(val id: UUID?, val namaKelompok: String?)
    data class ResikoRef(val id: UUID?, val namaResiko: String?)
    data class DaruratRef(val id: UUID?, val namaEmergency: String?)
    data class DepartemenRef(val id: UUID?, val namaDepartemen: String?)
    data class BidangRef(val id: UUID?, val namaBidang: String?)
    data class VendorRef(val id: UUID?, val namaVendor: String?)
    data class FotoRef(val id: UUID?, val file: String?, val status: String?)
}
