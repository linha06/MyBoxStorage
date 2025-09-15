package com.linha.myboxstorage.repo

import com.linha.myboxstorage.dao.GerobakDao
import com.linha.myboxstorage.data.Gerobak
import com.linha.myboxstorage.data.LokasiWithGerobak
import com.linha.myboxstorage.data.UserWithGerobak

class GerobakRepository(private val dao: GerobakDao) {
    suspend fun insertGerobak(gerobak: Gerobak): Long {
        return dao.insertGerobak(gerobak)
    }

    suspend fun getAllGerobak(): List<Gerobak> {
        return dao.getAllGerobak()
    }

    suspend fun deleteGerobak(id: Long) {
        dao.deleteGerobak(id)
    }

    suspend fun getGerobakById(id: Long): Gerobak {
        return dao.getGerobakById(id)
    }

    suspend fun updateGerobakById(gerobak: Gerobak) {
        dao.updateGerobak(
            id = gerobak.gerobakId,
            nomorSeri = gerobak.nomorSeri,
            ukuran = gerobak.ukuran,
            fotoUri = gerobak.fotoUri
        )
    }

    suspend fun getUserWithGerobak(userId: Long): UserWithGerobak? {
        return dao.getUserWithGerobak(userId)
    }

    suspend fun getLokasiWithGerobak(lokasiId: Long): LokasiWithGerobak? {
        return dao.getLokasiWithGerobak(lokasiId)
    }
}