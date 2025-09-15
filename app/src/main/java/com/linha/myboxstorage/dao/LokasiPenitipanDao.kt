package com.linha.myboxstorage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linha.myboxstorage.data.LokasiPenitipan

@Dao
interface LokasiPenitipanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLokasiPenitipan(lokasiPenitipan: LokasiPenitipan) : Long

    @Query("SELECT * FROM lokasi_penitipan")
    suspend fun getAllLokasiPenitipan(): List<LokasiPenitipan>

    @Query("DELETE FROM lokasi_penitipan WHERE lokasiId = :id")
    suspend fun deleteLokasiPenitipan(id: Long)

    @Query("SELECT * FROM lokasi_penitipan WHERE lokasiId = :id")
    suspend fun getLokasiPenitipanById(id: Long) : LokasiPenitipan

    @Query("UPDATE lokasi_penitipan SET namaArea = :namaArea, kapasitasMaks = :kapasitasMaks WHERE lokasiId = :id")
    suspend fun updateLokasiPenitipan(id: Long, namaArea: Char, kapasitasMaks: Int)

    @Query("SELECT lokasiId FROM lokasi_penitipan WHERE namaArea = :namaArea")
    suspend fun getLokasiPenitipanIdByNamaArea(namaArea: Char?): Long
}