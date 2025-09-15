package com.linha.myboxstorage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lokasi_penitipan")
data class LokasiPenitipan(
    @PrimaryKey(autoGenerate = true) val lokasiId: Long = 0,
    val namaArea: Char,
    val kapasitasMaks: Int
)
