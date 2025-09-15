package com.linha.myboxstorage.repo

import com.linha.myboxstorage.dao.TransaksiPenitipanDao
import com.linha.myboxstorage.data.GerobakWithTransaksi
import com.linha.myboxstorage.data.LokasiWithTransaksi
import com.linha.myboxstorage.data.TransaksiPenitipan
import com.linha.myboxstorage.data.UserWithTransaksi

class TransaksiPenitipanRepository(private val dao: TransaksiPenitipanDao) {

    suspend fun insertTransaksiPenitipan(transaksi: TransaksiPenitipan): Long {
        return dao.insertTransaksiPenitipan(transaksi)
    }

    suspend fun getAllTransaksiPenitipan(): List<TransaksiPenitipan> {
        return dao.getAllTransaksiPenitipan()
    }

    suspend fun getTransaksiPenitipanById(id: Long): TransaksiPenitipan? {
        return dao.getTransaksiPenitipanById(id)
    }

    suspend fun updateTransaksiPenitipan(transaksi: TransaksiPenitipan) {
        dao.updateTransaksiPenitipan(
            id = transaksi.id,
            durasi = transaksi.durasi,
            totalPembayaran = transaksi.totalPembayaran,
            tanggalPenitipan = transaksi.tanggalPenitipan,
            tanggalPengambilan = transaksi.tanggalPengambilan
        )
    }

    suspend fun deleteTransaksiPenitipan(id: Long) {
        dao.deleteTransaksiPenitipan(id)
    }

    suspend fun getUserWithTransaksi(id: Long): UserWithTransaksi? {
        return dao.getUserWithTransaksi(id)
    }

    suspend fun getGerobakWithTransaksi(id: Long): GerobakWithTransaksi? {
        return dao.getGerobakWithTransaksi(id)
    }

    suspend fun getLokasiWithTransaksi(id: Long): LokasiWithTransaksi? {
        return dao.getLokasiWithTransaksi(id)
    }

    suspend fun getTotalPendapatan(): Long {
        return dao.getTotalPendapatan()
    }
}