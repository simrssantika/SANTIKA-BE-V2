package com.santika.simrs.module.kepegawaian.infrastructure.mapper

import com.santika.simrs.module.kepegawaian.dto.request.PegawaiReq
import com.santika.simrs.module.kepegawaian.infrastructure.entities.PegawaiEntity

fun PegawaiEntity.applyFrom(req: PegawaiReq): PegawaiEntity = apply {
    nama               = req.nama
    nip                = req.nip
    jenisIdentitas     = req.jenisIdentitas
    identitasLain      = req.identitasLain
    noIdentitas        = req.noIdentitas
    kelamin            = req.kelamin
    jabatan            = req.jabatan
    telephone          = req.telephone
    email              = req.email
    jenjangJabatan     = req.jenjangJabatan
    kelompokPegawaiId  = req.kelompokPegawaiId
    resikoPegawaiId    = req.resikoPegawaiId
    daruratPegawaiId   = req.daruratPegawaiId
    departemenPegawaiId = req.departemenPegawaiId
    bidangPegawaiId    = req.bidangPegawaiId
    statusPegawai      = req.statusPegawai ?: "KONTRAK"
    vendorTenagaLuarId = req.vendorTenagaLuarId
    pendidikan         = req.pendidikan
    tempatLahir        = req.tempatLahir
    tanggalLahir       = req.tanggalLahir
    provinsiId         = req.provinsiId
    kabupatenId        = req.kabupatenId
    kecamatanId        = req.kecamatanId
    kelurahanId        = req.kelurahanId
    alamat             = req.alamat
    mulaiBekerja       = req.mulaiBekerja
    fotoId             = req.fotoId
    isActive           = req.isActive
}
