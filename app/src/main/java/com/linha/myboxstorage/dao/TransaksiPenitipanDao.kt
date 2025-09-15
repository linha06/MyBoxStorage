package com.linha.myboxstorage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.linha.myboxstorage.data.GerobakWithTransaksi
import com.linha.myboxstorage.data.LokasiWithTransaksi
import com.linha.myboxstorage.data.TransaksiPenitipan
import com.linha.myboxstorage.data.UserWithTransaksi

@Dao
interface TransaksiPenitipanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksiPenitipan(transaksiPenitipan: TransaksiPenitipan) : Long

    @Query("SELECT * FROM transaksi_penitipan")
    suspend fun getAllTransaksiPenitipan(): List<TransaksiPenitipan>

    @Query("SELECT * FROM transaksi_penitipan WHERE id = :id")
    suspend fun getTransaksiPenitipanById(id: Long) : TransaksiPenitipan?

    @Query("DELETE FROM transaksi_penitipan WHERE id = :id")
    suspend fun deleteTransaksiPenitipan(id: Long)

    @Query("UPDATE transaksi_penitipan SET durasi = :durasi, totalPembayaran = :totalPembayaran, tanggalPenitipan = :tanggalPenitipan, tanggalPengambilan = :tanggalPengambilan WHERE id = :id")
    suspend fun updateTransaksiPenitipan(
        id: Long,
        durasi: Int,
        totalPembayaran: Long,
        tanggalPenitipan: String,
        tanggalPengambilan: String
    )

    @Transaction
    @Query("SELECT * FROM pedagang WHERE pedagangId = :id")
    suspend fun getUserWithTransaksi(id: Long): UserWithTransaksi?

    @Transaction
    @Query("SELECT * FROM gerobak WHERE gerobakId = :id")
    suspend fun getGerobakWithTransaksi(id: Long): GerobakWithTransaksi?

    @Transaction
    @Query("SELECT * FROM lokasi_penitipan WHERE lokasiId = :id")
    suspend fun getLokasiWithTransaksi(id: Long): LokasiWithTransaksi?

    @Query("SELECT SUM(totalPembayaran) FROM transaksi_penitipan")
    suspend fun getTotalPendapatan(): Long
}