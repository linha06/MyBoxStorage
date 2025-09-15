package com.linha.myboxstorage.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "laporan_pembayaran",
    foreignKeys = [
        ForeignKey(
            entity = TransaksiPenitipan::class,
            parentColumns = ["id"],
            childColumns = ["transaksiId"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class LaporanPembayaran(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transaksiId: Int,
    val metodePembayaran: String,
    val statusPembayaran: String,
    val tanggalPembayaran: String // nanti diganti format dd mm yyyy
)
