package com.linha.myboxstorage.repo

import com.linha.myboxstorage.dao.LokasiPenitipanDao
import com.linha.myboxstorage.data.LokasiPenitipan

class LokasiPenitipanRepository(private val dao: LokasiPenitipanDao) {
    suspend fun insertLokasiPenitipan(lokasi: LokasiPenitipan) {
        dao.insertLokasiPenitipan(lokasi)
    }

    suspend fun getAllLokasiPenitipan(): List<LokasiPenitipan> {
        return dao.getAllLokasiPenitipan()
    }

    suspend fun deleteLokasiPenitipan(id: Long) {
        dao.deleteLokasiPenitipan(id)
    }

    suspend fun getLokasiPenitipanById(id: Long) : LokasiPenitipan {
        return dao.getLokasiPenitipanById(id)
    }

    suspend fun updateLokasiPenitipanById(id: Long, namaArea: Char, kapasitasMaks: Int){
        dao.updateLokasiPenitipan(id, namaArea, kapasitasMaks)
    }

    suspend fun getLokasiPenitipanIdByNamaArea(namaArea: Char?): Long {
        return dao.getLokasiPenitipanIdByNamaArea(namaArea)
    }
}