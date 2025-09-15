package com.linha.myboxstorage.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithGerobak(
    @Embedded val user: Pedagang,
    @Relation(
        parentColumn = "pedagangId",
        entityColumn = "pedagangId"
    ) val gerobak: List<Gerobak>
)

data class LokasiWithGerobak(
    @Embedded val lokasi: LokasiPenitipan,
    @Relation(
        parentColumn = "lokasiId",
        entityColumn = "lokasiId"
    ) val gerobak: List<Gerobak>
)

data class UserWithTransaksi(
    @Embedded val user: Pedagang,
    @Relation(
        parentColumn = "pedagangId",
        entityColumn = "pedagangId"
    ) val pedagangTransaksi: List<TransaksiPenitipan>
)

data class GerobakWithTransaksi(
    @Embedded val gerobak: Gerobak,
    @Relation(
        parentColumn = "gerobakId",
        entityColumn = "gerobakId"
    ) val gerobakTransaksi: List<TransaksiPenitipan>
)

data class LokasiWithTransaksi(
    @Embedded val lokasi: LokasiPenitipan,
    @Relation(
        parentColumn = "lokasiId",
        entityColumn = "lokasiId"
    ) val lokasiTransaksi: List<TransaksiPenitipan>
)

data class LaporanWithTransaksi(
    @Embedded val userTransaksi: TransaksiPenitipan,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    ) val laporanTransaksi: List<LaporanPembayaran>
)