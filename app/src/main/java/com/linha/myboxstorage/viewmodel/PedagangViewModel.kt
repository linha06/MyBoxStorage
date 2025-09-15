package com.linha.myboxstorage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linha.myboxstorage.data.Pedagang
import com.linha.myboxstorage.repo.PedagangRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class untuk menampung semua state input form
data class PedagangFormState(
    val nama: String = "",
    val alamat: String = "",
    val noHp: String = "",
    val noKTP: String = ""
)

class PedagangViewModel(private val repo: PedagangRepository) : ViewModel() {

    private val _formState = MutableStateFlow(PedagangFormState())
    val formState: StateFlow<PedagangFormState> = _formState.asStateFlow()

    private val _pedagang = MutableLiveData<List<Pedagang>>()
    val pedagang: LiveData<List<Pedagang>> = _pedagang

    private val _selectedPedagang = MutableLiveData<Pedagang?>()
    val selectedPedagang: LiveData<Pedagang?> = _selectedPedagang

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        getAllPedagang()
    }

    // Fungsi untuk memperbarui setiap field secara terpisah
    fun updateNama(nama: String) {
        _formState.update { it.copy(nama = nama) }
    }

    fun updateAlamat(alamat: String) {
        _formState.update { it.copy(alamat = alamat) }
    }

    fun updateNoHp(noHp: String) {
        _formState.update { it.copy(noHp = noHp) }
    }

    fun updateNoKTP(noKTP: String) {
        _formState.update { it.copy(noKTP = noKTP) }
    }

    // Reset form setelah data disimpan atau tidak digunakan
    fun resetForm() {
        _formState.value = PedagangFormState()
    }

    private fun getAllPedagang() {
        viewModelScope.launch {
            try {
                val result = repo.getAllPedagang()
                _pedagang.value = result
                Log.d("Pedagang ViewModel", "Success get All pedagang")
            } catch (e: Exception) {
                Log.e("Pedagang ViewModel", "failed get All Pedagang, error : ${e.message}")
            }
        }
    }

    suspend fun insertPedagang(pedagang: Pedagang): Long {
        return repo.insertPedagang(pedagang)
    }

    fun deletePedagang(id: Long) {
        viewModelScope.launch {
            try {
                repo.deletePedagang(id)
                Log.d("Pedagang ViewModel", "success delete data Pedagang by id-$id")
                getAllPedagang()
            } catch (e: Exception) {
                Log.e("Pedagang ViewModel", "failed delete Pedagang id-$id, error : ${e.message}")
            }
        }
    }

    fun getPedagangById(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.getPedagangById(id)
                _selectedPedagang.value = result
                Log.d("Pedagang ViewModel", "Success get data Pedagang by id-$id")
            } catch (e: Exception) {
                Log.e("Pedagang ViewModel", "Failed to get Pedagang by id-$id, error: ${e.message}")
                _selectedPedagang.value = null
            }
        }
    }

    fun updatePedagang(id: Long) {
        viewModelScope.launch {
            try {
                val currentFormState = formState.value
                repo.updatePedagangById(
                    id = id,
                    nama = currentFormState.nama,
                    alamat = currentFormState.alamat,
                    noHP = currentFormState.noHp,
                    noKTP = currentFormState.noKTP
                )
                Log.d("Pedagang ViewModel", "Success update data Pedagang by id-$id")
                getAllPedagang()
            } catch (e: Exception) {
                Log.e("Pedagang ViewModel", "Failed to update Pedagang id-$id, error: ${e.message}")
            }
        }
    }

    fun refreshPedagang() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val list = repo.getAllPedagang()
            _pedagang.value = list
            _isRefreshing.value = false
        }
    }

    class PedagangViewModelFactory(private val pedagangDao: PedagangRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PedagangViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PedagangViewModel(pedagangDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }
}