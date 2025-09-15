package com.linha.myboxstorage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linha.myboxstorage.data.Pedagang

@Dao
interface PedagangDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPedagang(pedagang: Pedagang) : Long

    @Query("SELECT * FROM pedagang")
    suspend fun getAllPedagang(): List<Pedagang>

    @Query("DELETE FROM pedagang WHERE pedagangId = :id")
    suspend fun deletePedagang(id: Long)

    @Query("SELECT * FROM pedagang WHERE pedagangId = :id")
    suspend fun getPedagangById(id: Long) : Pedagang

    @Query("UPDATE pedagang SET nama = :nama, alamat = :alamat, noHP = :noHP, noKTP = :noKTP WHERE pedagangId = :id")
    suspend fun updatePedagang(id: Long, nama: String, alamat: String, noHP: String, noKTP: String)
}