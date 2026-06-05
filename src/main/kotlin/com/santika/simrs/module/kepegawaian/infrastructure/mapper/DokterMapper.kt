package com.santika.simrs.module.kepegawaian.infrastructure.mapper

import com.santika.simrs.module.kepegawaian.dto.request.DokterReq
import com.santika.simrs.module.kepegawaian.infrastructure.entities.DokterEntity

fun DokterEntity.applyFrom(req: DokterReq): DokterEntity = apply {
    pegawaiId            = req.pegawaiId
    namaDokter           = req.namaDokter
    nip                  = req.nip
    telephoneDokter      = req.telephoneDokter
    emailDokter          = req.emailDokter
    spesialisId          = req.spesialisId
    alumni               = req.alumni
    noIjinPraktek        = req.noIjinPraktek
    isActive             = req.isActive
}
