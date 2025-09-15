package com.linha.myboxstorage.repo

import com.linha.myboxstorage.dao.LaporanPembayaranDao
import com.linha.myboxstorage.data.LaporanPembayaran
import com.linha.myboxstorage.data.LaporanWithTransaksi

class LaporanPembayaranRepository(private val dao: LaporanPembayaranDao) {

    suspend fun insertLaporanPembayaran(laporan: LaporanPembayaran) {
        dao.insertLaporanPembayaran(laporan)
    }

    suspend fun getAllLaporanPembayaran(): List<LaporanPembayaran> {
        return dao.getAllLaporanPembayaran()
    }

    suspend fun getLaporanPembayaranById(id: Long): LaporanPembayaran? {
        return dao.getLaporanPembayaranById(id)
    }

    suspend fun updateLaporanPembayaran(laporan: LaporanPembayaran) {
        dao.updateLaporanPembayaran(
            id = laporan.id,
            metodePembayaran = laporan.metodePembayaran,
            statusPembayaran = laporan.statusPembayaran,
            tanggalPembayaran = laporan.tanggalPembayaran
        )
    }

    suspend fun deleteLaporanPembayaran(id: Long) {
        dao.deleteLaporanPembayaran(id)
    }

    suspend fun getLaporanWithTransaksi(id: Long): LaporanWithTransaksi? {
        return dao.getLaporanWithTransaksi(id)
    }
}