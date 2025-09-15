package com.linha.myboxstorage.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.linha.myboxstorage.data.Gerobak
import com.linha.myboxstorage.data.LokasiWithGerobak
import com.linha.myboxstorage.data.UserWithGerobak

@Dao
interface GerobakDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGerobak(gerobak: Gerobak) : Long

    @Query("SELECT * FROM gerobak")
    suspend fun getAllGerobak(): List<Gerobak>

    @Query("DELETE FROM gerobak WHERE gerobakId = :id")
    suspend fun deleteGerobak(id: Long)

    @Query("SELECT * FROM gerobak WHERE gerobakId = :id")
    suspend fun getGerobakById(id: Long) : Gerobak

    @Query("UPDATE gerobak SET nomorSeri = :nomorSeri, ukuran = :ukuran, fotoUri = :fotoUri WHERE gerobakId = :id")
    suspend fun updateGerobak(id: Long, nomorSeri: Int, ukuran: String, fotoUri: Uri?)

    @Transaction
    @Query("SELECT * FROM pedagang WHERE pedagangId = :id")
    suspend fun getUserWithGerobak(id: Long): UserWithGerobak?

    @Transaction
    @Query("SELECT * FROM lokasi_penitipan WHERE lokasiId = :id")
    suspend fun getLokasiWithGerobak(id: Long): LokasiWithGerobak?
}