package com.linha.myboxstorage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linha.myboxstorage.data.LokasiPenitipan
import com.linha.myboxstorage.repo.LokasiPenitipanRepository
import com.linha.myboxstorage.viewmodel.LokasiPenitipanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowLokasiPenitipanScreen(
    lokasiPenitipanRepo: LokasiPenitipanRepository,
    modifier: Modifier = Modifier
) {
    val lokasiViewModel: LokasiPenitipanViewModel =
        viewModel(factory = LokasiPenitipanViewModel.LokasiPenitipanViewModelFactory(lokasiPenitipanRepo))

    val lokasi by lokasiViewModel.lokasiList.observeAsState(emptyList())
    val isRefreshing by lokasiViewModel.isRefreshing.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()
    var dialogDelete by remember { mutableStateOf(false) }
    var id by remember { mutableLongStateOf(0) }

    var dialogAdd by remember { mutableStateOf(false) }
    var dialogEdit by remember { mutableStateOf(false) }

    var namaAreaInput by remember { mutableStateOf("") }
    var slotLokasi by remember { mutableIntStateOf(0) }

    // state untuk menampilkan error
    var isNamaAreaError by remember { mutableStateOf(false) }

    val selectedLokasi by lokasiViewModel.selectedLokasi.observeAsState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { lokasiViewModel.refreshLokasiPenitipan() },
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Daftar Lokasi Tersedia", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = lokasi) {
                    CardPedagang(
                        textId = "${it.lokasiId}",
                        textNama = "Area ${it.namaArea}",
                        textNoKtp = "Kapasitas : ${it.kapasitasMaks}",
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        onCardClick = {
                            namaAreaInput = it.namaArea.toString()
                            slotLokasi = it.kapasitasMaks
                            dialogEdit = true
                            id = it.lokasiId
                        },
                        onCardLongClick = {
                            dialogDelete = true
                            id = it.lokasiId
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = {
                dialogAdd = true
            }) {
                Text(text = "Tambah Lokasi")
            }

        }
        if (dialogDelete) {
            BasicAlertDialog(
                onDismissRequest = { dialogDelete = false },
                properties = DialogProperties(),
                content = {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, Color.Black)
                            .padding(20.dp)
                    ) {
                        Text(text = "Test Alert Dialog Hapus Lokasi Penitipan")
                        Button(onClick = {
                            lokasiViewModel.deleteLokasiPenitipan(id)
                        }) {
                            Text(text = "Delete")
                        }
                    }
                }
            )
        }

        if (dialogAdd) {
            BasicAlertDialog(
                onDismissRequest = { dialogAdd = false },
                properties = DialogProperties(),
                content = {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, Color.Black)
                            .padding(20.dp)
                    ) {
                        CustomTextFieldWithPlaceholder(
                            textPlaceholder = "Nama Lokasi (1 Karakter)",
                            textValue = namaAreaInput,
                            onChange = {
                                if (it.length <= 1) {
                                    namaAreaInput = it
                                    isNamaAreaError = false
                                }
                            }
                        )
                        if (isNamaAreaError) {
                            Text(
                                text = "Nama Area harus diisi dengan 1 karakter.",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        CustomTextFieldWithPlaceholder(
                            textPlaceholder = "Slot Lokasi",
                            textValue = slotLokasi.toString(),
                            onChange = {
                                slotLokasi = it.toIntOrNull() ?: 0
                            }
                        )
                        Button(onClick = {
                            if (namaAreaInput.length == 1) {
                                val lokasi = LokasiPenitipan(
                                    namaArea = namaAreaInput.first(),
                                    kapasitasMaks = slotLokasi
                                )
                                lokasiViewModel.insertLokasiPenitipan(lokasi)
                                dialogAdd = false

                                namaAreaInput = ""
                                slotLokasi = 0
                            } else {
                                isNamaAreaError = true
                            }
                        }) {
                            Text(text = "Tambah")
                        }
                    }
                }
            )
        }

        if (dialogEdit){
            LaunchedEffect(id) {
                lokasiViewModel.getLokasiPenitipanById(id)
            }

            BasicAlertDialog(
                onDismissRequest = { dialogEdit = false },
                properties = DialogProperties(),
                content = {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, Color.Black)
                            .padding(20.dp)
                    ) {
                        CustomTextFieldWithPlaceholder(
                            textPlaceholder = "Nama Lokasi (1 Karakter)",
                            textValue = namaAreaInput,
                            onChange = {
                                if (it.length <= 1) {
                                    namaAreaInput = it
                                    isNamaAreaError = false
                                }
                            }
                        )
                        if (isNamaAreaError) {
                            Text(
                                text = "Nama Area harus diisi dengan 1 karakter.",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        CustomTextFieldWithPlaceholder(
                            textPlaceholder = "Slot Lokasi",
                            textValue = slotLokasi.toString(),
                            onChange = {
                                slotLokasi = it.toIntOrNull() ?: 0
                            }
                        )
                        Button(onClick = {
                            if (namaAreaInput.length == 1) {
                                val lokasi = LokasiPenitipan(
                                    namaArea = namaAreaInput.first(),
                                    kapasitasMaks = slotLokasi
                                )
                                lokasiViewModel.insertLokasiPenitipan(lokasi)
                                dialogEdit = false

                                namaAreaInput = ""
                                slotLokasi = 0
                            } else {
                                isNamaAreaError = true
                            }
                        }) {
                            Text(text = "Tambah")
                        }
                    }
                }
            )
        }
    }
}