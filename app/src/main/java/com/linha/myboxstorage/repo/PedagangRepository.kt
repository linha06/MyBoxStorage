package com.linha.myboxstorage.repo

import com.linha.myboxstorage.dao.PedagangDao
import com.linha.myboxstorage.data.Pedagang

class PedagangRepository(private val dao: PedagangDao) {
    suspend fun insertPedagang(pedagang: Pedagang): Long {
        return dao.insertPedagang(pedagang)
    }

    suspend fun getAllPedagang(): List<Pedagang> {
        return dao.getAllPedagang()
    }

    suspend fun deletePedagang(id: Long) {
        dao.deletePedagang(id)
    }

    suspend fun getPedagangById(id: Long) : Pedagang {
        return dao.getPedagangById(id)
    }

    suspend fun updatePedagangById(id: Long, nama: String, alamat: String, noHP: String, noKTP: String){
        dao.updatePedagang(id, nama, alamat, noHP, noKTP)
    }
}