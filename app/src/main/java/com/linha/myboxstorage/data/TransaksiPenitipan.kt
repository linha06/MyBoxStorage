package com.linha.myboxstorage.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaksi_penitipan",
    foreignKeys = [
        ForeignKey(
            entity = Pedagang::class,
            parentColumns = ["pedagangId"],
            childColumns = ["pedagangId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Gerobak::class,
            parentColumns = ["gerobakId"],
            childColumns = ["gerobakId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LokasiPenitipan::class,
            parentColumns = ["lokasiId"],
            childColumns = ["lokasiId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransaksiPenitipan(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pedagangId: Int,
    val gerobakId: Int,
    val lokasiId: Int,
    val durasi: Int,
    val totalPembayaran : Long,
    val tanggalPenitipan: String,
    val tanggalPengambilan: String
)
