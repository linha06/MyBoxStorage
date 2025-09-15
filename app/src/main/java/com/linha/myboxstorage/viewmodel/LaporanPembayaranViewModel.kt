package com.linha.myboxstorage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linha.myboxstorage.data.LaporanPembayaran
import com.linha.myboxstorage.data.LaporanWithTransaksi
import com.linha.myboxstorage.repo.LaporanPembayaranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LaporanPembayaranViewModel(private val repo: LaporanPembayaranRepository) : ViewModel() {

    private val _laporanList = MutableLiveData<List<LaporanPembayaran>>()
    val laporanList: LiveData<List<LaporanPembayaran>> = _laporanList

    private val _selectedLaporan = MutableLiveData<LaporanPembayaran?>()
    val selectedLaporan: LiveData<LaporanPembayaran?> = _selectedLaporan

    private val _laporanWithTransaksi = MutableLiveData<LaporanWithTransaksi?>()
    val laporanWithTransaksi: LiveData<LaporanWithTransaksi?> = _laporanWithTransaksi

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        getAllLaporanPembayaran()
    }

    private fun getAllLaporanPembayaran() {
        viewModelScope.launch {
            try {
                val result = repo.getAllLaporanPembayaran()
                _laporanList.value = result
                Log.d("LaporanVM", "Success get all laporan pembayaran")
            } catch (e: Exception) {
                Log.e("LaporanVM", "Failed to get all laporan pembayaran, error: ${e.message}")
            }
        }
    }

    fun insertLaporanPembayaran(laporan: LaporanPembayaran) {
        viewModelScope.launch {
            try {
                repo.insertLaporanPembayaran(laporan)
                Log.d("LaporanVM", "Success insert data laporan pembayaran")
                getAllLaporanPembayaran()
            } catch (e: Exception) {
                Log.e("LaporanVM", "Failed to insert laporan pembayaran, error: ${e.message}")
            }
        }
    }

    fun deleteLaporanPembayaran(id: Long) {
        viewModelScope.launch {
            try {
                repo.deleteLaporanPembayaran(id)
                Log.d("LaporanVM", "Success delete laporan pembayaran by id-$id")
                getAllLaporanPembayaran()
            } catch (e: Exception) {
                Log.e("LaporanVM", "Failed to delete laporan pembayaran id-$id, error: ${e.message}")
            }
        }
    }

    fun getLaporanPembayaranById(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.getLaporanPembayaranById(id)
                _selectedLaporan.value = result
                Log.d("LaporanVM", "Success get laporan pembayaran by id-$id")
            } catch (e: Exception) {
                Log.e("LaporanVM", "Failed to get laporan pembayaran by id-$id, error: ${e.message}")
                _selectedLaporan.value = null
            }
        }
    }

    fun updateLaporanPembayaran(laporan: LaporanPembayaran) {
        viewModelScope.launch {
            try {
                repo.updateLaporanPembayaran(laporan)
                Log.d("LaporanVM", "Success update laporan pembayaran by id-${laporan.id}")
                getAllLaporanPembayaran()
            } catch (e: Exception) {
                Log.e("LaporanVM", "Failed to update laporan pembayaran, error: ${e.message}")
            }
        }
    }

    // Metode untuk memanggil transaksi relasional
    fun getLaporanWithTransaksi(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.getLaporanWithTransaksi(id)
                _laporanWithTransaksi.value = result
                Log.d("LaporanVM", "Success get LaporanWithTransaksi for id-$id")
            } catch (e: Exception) {
                Log.e("LaporanVM", "Failed to get LaporanWithTransaksi for id-$id, error: ${e.message}")
                _laporanWithTransaksi.value = null
            }
        }
    }

    fun refreshLaporanPembayaran() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                getAllLaporanPembayaran()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    class LaporanPembayaranViewModelFactory(private val repo: LaporanPembayaranRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LaporanPembayaranViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LaporanPembayaranViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}