package com.linha.myboxstorage.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linha.myboxstorage.data.Gerobak
import com.linha.myboxstorage.data.GerobakWithTransaksi
import com.linha.myboxstorage.data.LokasiWithTransaksi
import com.linha.myboxstorage.data.TransaksiPenitipan
import com.linha.myboxstorage.data.UserWithTransaksi
import com.linha.myboxstorage.repo.LaporanPembayaranRepository
import com.linha.myboxstorage.repo.TransaksiPenitipanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TransaksiPenitipanFormState(
    val durasi: Int = 0,
    val totalPembayaran: Long = 0,
    val perkalian: Int = 0,
    val tanggalPenitipan: String = "",
    val tanggalPengambilan: String = ""
)

data class TransaksiPenitipanDisplay(
    val id: Long,
    val totalPembayaran: Int,
    val namaPedagang: String,
    val nomorSeriGerobak: String,
    val statusPembayaran: String,
    val tanggalPenitipan: String,
    val tanggalPengambilan: String
)

class TransaksiPenitipanViewModel(
    private val transaksiRepo: TransaksiPenitipanRepository,
    private val laporanRepo: LaporanPembayaranRepository
) : ViewModel() {

    private val _transaksiList = MutableLiveData<List<TransaksiPenitipan>>()
    val transaksiList: LiveData<List<TransaksiPenitipan>> = _transaksiList

    private val _selectedTransaksi = MutableLiveData<TransaksiPenitipan?>()
    val selectedTransaksi: LiveData<TransaksiPenitipan?> = _selectedTransaksi

    private val _userWithTransaksi = MutableLiveData<UserWithTransaksi?>()
    val userWithTransaksi: LiveData<UserWithTransaksi?> = _userWithTransaksi

    private val _gerobakWithTransaksi = MutableLiveData<GerobakWithTransaksi?>()
    val gerobakWithTransaksi: LiveData<GerobakWithTransaksi?> = _gerobakWithTransaksi

    private val _lokasiWithTransaksi = MutableLiveData<LokasiWithTransaksi?>()
    val lokasiWithTransaksi: LiveData<LokasiWithTransaksi?> = _lokasiWithTransaksi

    private val _transaksiDisplayList = MutableLiveData<List<TransaksiPenitipanDisplay>>()
    val transaksiDisplayList: LiveData<List<TransaksiPenitipanDisplay>> = _transaksiDisplayList

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _totalPendapatan = MutableLiveData<Long>()
    val totalPendapatan: LiveData<Long> = _totalPendapatan

    // state form transaksi
    private val _formState = MutableStateFlow(TransaksiPenitipanFormState())
    val formState: StateFlow<TransaksiPenitipanFormState> = _formState.asStateFlow()

    init {
        getTotalPendapatan()
    }

    fun updateDurasi(durasi: Int) {
        val updatedState = _formState.value.copy(
            durasi = durasi,
            totalPembayaran = (durasi * _formState.value.perkalian).toLong()
        )
        _formState.value = updatedState
    }

    fun updatePerkalian(perkalian: Int) {
        val updatedState = _formState.value.copy(
            perkalian = perkalian,
            totalPembayaran = (_formState.value.durasi * perkalian).toLong()
        )
        _formState.value = updatedState
    }

    fun updateTanggalPenitipan(tanggal: String) {
        _formState.update { it.copy(tanggalPenitipan = tanggal) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTanggalPengambilan() {
        val currentDateTime = LocalDateTime.now()
        val tanggalPengambilan = currentDateTime.plusMonths(_formState.value.durasi.toLong())
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        _formState.update { it.copy(tanggalPengambilan = tanggalPengambilan.format(formatter)) }
    }

    fun resetForm() {
        _formState.value = TransaksiPenitipanFormState()
    }

    private fun getTotalPendapatan() {
        viewModelScope.launch {
            try {
                val result = transaksiRepo.getTotalPendapatan()
                _totalPendapatan.value = result
                Log.d("TransaksiVM", "Success get total pendapatan")
            } catch (e: Exception) {
                Log.e("TransaksiVM", "Failed get total pendapatan, error: ${e.message}")
            }
        }
    }

    fun calculateTotalPendapatanBulanIni(gerobakList: List<Gerobak>?) {
        if (gerobakList.isNullOrEmpty()) {
            _totalPendapatan.value = 0L
            return
        }

        var total = 0L
        for (gerobak in gerobakList) {
            total += when (gerobak.ukuran) {
                "Kecil" -> 200000L
                "Besar" -> 300000L
                else -> 0L
            }
        }
        _totalPendapatan.value = total
    }

    fun getAllTransaksiPenitipan() {
        viewModelScope.launch {
            try {
                val result = transaksiRepo.getAllTransaksiPenitipan()
                _transaksiList.value = result
                Log.d("TransaksiVM", "Success get all transaksi penitipan")
            } catch (e: Exception) {
                Log.e("TransaksiVM", "Failed to get all transaksi penitipan, error: ${e.message}")
            }
        }
    }

    fun getAllTransaksiPenitipanForDisplay() {
        viewModelScope.launch {
            try {
                val transaksiList = transaksiRepo.getAllTransaksiPenitipan()
                val laporanList = laporanRepo.getAllLaporanPembayaran()

                val displayList = transaksiList.map { transaksi ->
                    val user = transaksiRepo.getUserWithTransaksi(transaksi.pedagangId.toLong())
                    val gerobak = transaksiRepo.getGerobakWithTransaksi(transaksi.gerobakId.toLong())
                    val status = laporanList.find { it.transaksiId.toLong() == transaksi.id }

                    TransaksiPenitipanDisplay(
                        id = transaksi.id,
                        totalPembayaran = transaksi.totalPembayaran.toInt(),
                        namaPedagang = user?.user?.nama ?: "N/A",
                        nomorSeriGerobak = (gerobak?.gerobak?.nomorSeri ?: "N/A").toString(),
                        statusPembayaran = status?.statusPembayaran ?: "N/A",
                        tanggalPenitipan = transaksi.tanggalPenitipan,
                        tanggalPengambilan = transaksi.tanggalPengambilan
                    )
                }
                _transaksiDisplayList.value = displayList
                Log.d("TransaksiVM", "Success get all transaksi for display")
            } catch (e: Exception) {
                Log.e("TransaksiVM", "Failed to get all transaksi for display, error: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExpiredTransaksiCount(): LiveData<Int> {
        val expiredCount = MutableLiveData<Int>()
        viewModelScope.launch {
            try {
                val allTransaksi = transaksiRepo.getAllTransaksiPenitipan()
                val today = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

                val expiredList = allTransaksi.filter { transaksi ->
                    try {
                        val tanggalPengambilan = LocalDate.parse(transaksi.tanggalPengambilan, formatter)
                        tanggalPengambilan.isBefore(today)
                    } catch (e: Exception) {
                        false
                    }
                }
                expiredCount.postValue(expiredList.size)
            } catch (e: Exception) {
                Log.e("TransaksiVM", "Failed to get expired transaksi count, error: ${e.message}")
                expiredCount.postValue(0)
            }
        }
        return expiredCount
    }


    suspend fun insertTransaksiPenitipan(transaksi: TransaksiPenitipan): Long {
        return transaksiRepo.insertTransaksiPenitipan(transaksi)
    }

    fun deleteTransaksiPenitipan(id: Long) {
        viewModelScope.launch {
            try {
                transaksiRepo.deleteTransaksiPenitipan(id)
                Log.d("TransaksiVM", "Success delete transaksi id-$id")
                getAllTransaksiPenitipan()
            } catch (e: Exception) {
                Log.e("TransaksiVM", "Failed to delete transaksi id-$id, error: ${e.message}")
            }
        }
    }

    fun getTransaksiPenitipanById(id: Long) {
        viewModelScope.launch {
            try {
                val result = transaksiRepo.getTransaksiPenitipanById(id)
                _selectedTransaksi.value = result
                Log.d("TransaksiVM", "Success get transaksi by id-$id")
            } catch (e: Exception) {
                Log.e("TransaksiVM", "Failed to get transaksi by id-$id, error: ${e.message}")
                _selectedTransaksi.value = null
            }
        }
    }

    fun updateTransaksiPenitipan(transaksi: TransaksiPenitipan) {
        viewModelScope.launch {
            try {
                transaksiRepo.updateTransaksiPenitipan(transaksi)
                Log.d("TransaksiVM", "Success update transaksi id-${transaksi.id}")
                getAllTransaksiPenitipan()
            } catch (e: Exception) {
                Log.e("TransaksiVM", "Failed to update transaksi, error: ${e.message}")
            }
        }
    }

    fun getUserWithTransaksi(id: Long) {
        viewModelScope.launch {
            try {
                val result = transaksiRepo.getUserWithTransaksi(id)
                _userWithTransaksi.value = result
                Log.d("TransaksiVM", "Success get UserWithTransaksi for id-$id")
            } catch (e: Exception) {
                Log.e(
                    "TransaksiVM",
                    "Failed to get UserWithTransaksi for id-$id, error: ${e.message}"
                )
                _userWithTransaksi.value = null
            }
        }
    }

    fun getGerobakWithTransaksi(id: Long) {
        viewModelScope.launch {
            try {
                val result = transaksiRepo.getGerobakWithTransaksi(id)
                _gerobakWithTransaksi.value = result
                Log.d("TransaksiVM", "Success get GerobakWithTransaksi for id-$id")
            } catch (e: Exception) {
                Log.e(
                    "TransaksiVM",
                    "Failed to get GerobakWithTransaksi for id-$id, error: ${e.message}"
                )
                _gerobakWithTransaksi.value = null
            }
        }
    }

    fun getLokasiWithTransaksi(id: Long) {
        viewModelScope.launch {
            try {
                val result = transaksiRepo.getLokasiWithTransaksi(id)
                _lokasiWithTransaksi.value = result
                Log.d("TransaksiVM", "Success get LokasiWithTransaksi for id-$id")
            } catch (e: Exception) {
                Log.e(
                    "TransaksiVM",
                    "Failed to get LokasiWithTransaksi for id-$id, error: ${e.message}"
                )
                _lokasiWithTransaksi.value = null
            }
        }
    }

    fun refreshTransaksiPenitipan() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                getAllTransaksiPenitipan()
                getAllTransaksiPenitipanForDisplay()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    class TransaksiPenitipanViewModelFactory(
        private val transaksiRepo: TransaksiPenitipanRepository,
        private val laporanRepo: LaporanPembayaranRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TransaksiPenitipanViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TransaksiPenitipanViewModel(transaksiRepo, laporanRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
