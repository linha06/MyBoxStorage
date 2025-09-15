package com.linha.myboxstorage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedagang")
data class Pedagang(
    @PrimaryKey(autoGenerate = true) val pedagangId: Long = 0,
    val nama: String,
    val alamat: String,
    val noHP: String,
    val noKTP: String
)