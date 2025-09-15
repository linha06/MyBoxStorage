package com.linha.myboxstorage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.linha.myboxstorage.data.Gerobak
import com.linha.myboxstorage.data.LokasiWithGerobak
import com.linha.myboxstorage.data.UserWithGerobak
import com.linha.myboxstorage.repo.GerobakRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GerobakViewModel(private val repo: GerobakRepository) : ViewModel() {

    private val _gerobakList = MutableLiveData<List<Gerobak>>()
    val gerobakList: LiveData<List<Gerobak>> = _gerobakList

    private val _selectedGerobak = MutableLiveData<Gerobak?>()
    val selectedGerobak: LiveData<Gerobak?> = _selectedGerobak

    private val _userWithGerobak = MutableLiveData<UserWithGerobak?>()
    val userWithGerobak: LiveData<UserWithGerobak?> = _userWithGerobak

    private val _lokasiWithGerobak = MutableLiveData<LokasiWithGerobak?>()
    val lokasiWithGerobak: LiveData<LokasiWithGerobak?> = _lokasiWithGerobak

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        getAllGerobak()
    }

    private fun getAllGerobak() {
        viewModelScope.launch {
            try {
                val result = repo.getAllGerobak()
                _gerobakList.value = result
                Log.d("GerobakViewModel", "Success get all gerobak")
            } catch (e: Exception) {
                Log.e("GerobakViewModel", "Failed to get all gerobak, error: ${e.message}")
            }
        }
    }

    suspend fun insertGerobak(gerobak: Gerobak): Long {
        return repo.insertGerobak(gerobak)
    }

    fun deleteGerobak(id: Long) {
        viewModelScope.launch {
            try {
                repo.deleteGerobak(id)
                Log.d("GerobakViewModel", "Success delete gerobak by id-$id")
                getAllGerobak() // Refresh list setelah delete
            } catch (e: Exception) {
                Log.e("GerobakViewModel", "Failed to delete gerobak id-$id, error: ${e.message}")
            }
        }
    }

    fun getGerobakById(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.getGerobakById(id)
                _selectedGerobak.value = result
                Log.d("GerobakViewModel", "Success get gerobak by id-$id")
            } catch (e: Exception) {
                Log.e("GerobakViewModel", "Failed to get gerobak by id-$id, error: ${e.message}")
                _selectedGerobak.value = null
            }
        }
    }

    fun updateGerobak(gerobak: Gerobak) {
        viewModelScope.launch {
            try {
                repo.updateGerobakById(gerobak)
                Log.d("GerobakViewModel", "Success update gerobak by id-${gerobak.gerobakId}")
                getAllGerobak() // Refresh list setelah update
            } catch (e: Exception) {
                Log.e("GerobakViewModel", "Failed to update gerobak, error: ${e.message}")
            }
        }
    }

    fun getUserWithGerobak(userId: Long) {
        viewModelScope.launch {
            try {
                val result = repo.getUserWithGerobak(userId)
                _userWithGerobak.value = result
                Log.d("GerobakViewModel", "Success get UserWithGerobak for user id-$userId")
            } catch (e: Exception) {
                Log.e("GerobakViewModel", "Failed to get UserWithGerobak for user id-$userId, error: ${e.message}")
                _userWithGerobak.value = null
            }
        }
    }

    fun getLokasiWithGerobak(lokasiId: Long) {
        viewModelScope.launch {
            try {
                val result = repo.getLokasiWithGerobak(lokasiId)
                _lokasiWithGerobak.value = result
                Log.d("GerobakViewModel", "Success get UserWithGerobak for user id-$lokasiId")
            } catch (e: Exception) {
                Log.e("GerobakViewModel", "Failed to get UserWithGerobak for user id-$lokasiId, error: ${e.message}")
                _lokasiWithGerobak.value = null
            }
        }
    }

    fun refreshGerobak() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                getAllGerobak()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    class GerobakViewModelFactory(private val repo: GerobakRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GerobakViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GerobakViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}