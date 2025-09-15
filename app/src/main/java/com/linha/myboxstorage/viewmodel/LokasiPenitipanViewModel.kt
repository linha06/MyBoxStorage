package com.linha.myboxstorage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linha.myboxstorage.data.LokasiPenitipan
import com.linha.myboxstorage.repo.LokasiPenitipanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LokasiPenitipanViewModel(private val repo: LokasiPenitipanRepository) : ViewModel() {

    private val _lokasiList = MutableLiveData<List<LokasiPenitipan>>()
    val lokasiList: LiveData<List<LokasiPenitipan>> = _lokasiList

    private val _selectedLokasi = MutableLiveData<LokasiPenitipan?>()
    val selectedLokasi: LiveData<LokasiPenitipan?> = _selectedLokasi

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        getAllLokasiPenitipan()
    }

    fun getAllLokasiPenitipan() {
        viewModelScope.launch {
            try {
                val result = repo.getAllLokasiPenitipan()
                _lokasiList.value = result
                Log.d("LokasiViewModel", "Success get all lokasi penitipan")
            } catch (e: Exception) {
                Log.e("LokasiViewModel", "Failed to get all lokasi penitipan, error: ${e.message}")
            }
        }
    }

    fun insertLokasiPenitipan(lokasi: LokasiPenitipan) {
        viewModelScope.launch {
            try {
                repo.insertLokasiPenitipan(lokasi)
                Log.d("LokasiViewModel", "Success insert data lokasi penitipan")
                getAllLokasiPenitipan()
            } catch (e: Exception) {
                Log.e("LokasiViewModel", "Failed to insert lokasi penitipan, error: ${e.message}")
            }
        }
    }

    fun deleteLokasiPenitipan(id: Long) {
        viewModelScope.launch {
            try {
                repo.deleteLokasiPenitipan(id)
                Log.d("LokasiViewModel", "Success delete lokasi penitipan by id-$id")
                getAllLokasiPenitipan()
            } catch (e: Exception) {
                Log.e("LokasiViewModel", "Failed to delete lokasi penitipan id-$id, error: ${e.message}")
            }
        }
    }

    fun getLokasiPenitipanById(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.getLokasiPenitipanById(id)
                _selectedLokasi.value = result
                Log.d("LokasiViewModel", "Success get lokasi penitipan by id-$id")
            } catch (e: Exception) {
                Log.e("LokasiViewModel", "Failed to get lokasi penitipan by id-$id, error: ${e.message}")
                _selectedLokasi.value = null
            }
        }
    }

    suspend fun getLokasiPenitipanIdByNamaArea(namaArea: Char?): Long {
        return repo.getLokasiPenitipanIdByNamaArea(namaArea)
    }

    fun updateLokasiPenitipan(id: Long, namaArea: Char, kapasitasMaks: Int) {
        viewModelScope.launch {
            try {
                repo.updateLokasiPenitipanById(id, namaArea, kapasitasMaks)
                Log.d("LokasiViewModel", "Success update lokasi penitipan by id-$id")
                getAllLokasiPenitipan()
            } catch (e: Exception) {
                Log.e("LokasiViewModel", "Failed to update lokasi penitipan id-$id, error: ${e.message}")
            }
        }
    }

    fun refreshLokasiPenitipan() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                getAllLokasiPenitipan()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    class LokasiPenitipanViewModelFactory(private val repo: LokasiPenitipanRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LokasiPenitipanViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LokasiPenitipanViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}