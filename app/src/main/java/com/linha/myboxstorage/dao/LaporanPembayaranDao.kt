package com.linha.myboxstorage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.linha.myboxstorage.data.LaporanPembayaran
import com.linha.myboxstorage.data.LaporanWithTransaksi

@Dao
interface LaporanPembayaranDao {
    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaporanPembayaran(laporanPembayaran: LaporanPembayaran) : Long

    // READ
    @Query("SELECT * FROM laporan_pembayaran")
    suspend fun getAllLaporanPembayaran(): List<LaporanPembayaran>

    @Query("SELECT * FROM laporan_pembayaran WHERE id = :id")
    suspend fun getLaporanPembayaranById(id: Long): LaporanPembayaran?

    // UPDATE
    @Query("UPDATE laporan_pembayaran SET metodePembayaran = :metodePembayaran, statusPembayaran = :statusPembayaran, tanggalPembayaran = :tanggalPembayaran WHERE id = :id")
    suspend fun updateLaporanPembayaran(
        id: Long,
        metodePembayaran: String,
        statusPembayaran: String,
        tanggalPembayaran: String
    )

    // DELETE
    @Query("DELETE FROM laporan_pembayaran WHERE id = :id")
    suspend fun deleteLaporanPembayaran(id: Long)

    // Transaksi relasional
    @Transaction
    @Query("SELECT * FROM transaksi_penitipan WHERE id = :id")
    suspend fun getLaporanWithTransaksi(id: Long): LaporanWithTransaksi?
}