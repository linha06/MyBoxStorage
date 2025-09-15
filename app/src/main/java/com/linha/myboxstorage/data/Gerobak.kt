package com.linha.myboxstorage.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gerobak",
    foreignKeys = [
        ForeignKey(
            entity = Pedagang::class,
            parentColumns = ["pedagangId"],
            childColumns = ["pedagangId"],
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
data class Gerobak(
    @PrimaryKey(autoGenerate = true) val gerobakId: Long = 0,
    val pedagangId: Int,
    val lokasiId: Int,
    val nomorSeri: Int,
    val ukuran: String,
    val fotoUri: Uri? = null
)
