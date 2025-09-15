package com.linha.myboxstorage.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linha.myboxstorage.data.Gerobak
import com.linha.myboxstorage.data.LaporanPembayaran
import com.linha.myboxstorage.data.Pedagang
import com.linha.myboxstorage.data.TransaksiPenitipan
import com.linha.myboxstorage.repo.GerobakRepository
import com.linha.myboxstorage.repo.LaporanPembayaranRepository
import com.linha.myboxstorage.repo.LokasiPenitipanRepository
import com.linha.myboxstorage.repo.PedagangRepository
import com.linha.myboxstorage.repo.TransaksiPenitipanRepository
import com.linha.myboxstorage.utils.saveImageToInternalStorage
import com.linha.myboxstorage.viewmodel.GerobakViewModel
import com.linha.myboxstorage.viewmodel.LaporanPembayaranViewModel
import com.linha.myboxstorage.viewmodel.LokasiPenitipanViewModel
import com.linha.myboxstorage.viewmodel.PedagangViewModel
import com.linha.myboxstorage.viewmodel.TransaksiPenitipanViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateFormScreen(
    pedagangRepo: PedagangRepository,
    gerobakRepo: GerobakRepository,
    lokasiRepo: LokasiPenitipanRepository,
    transaksiRepo: TransaksiPenitipanRepository,
    laporanPembayaranRepo: LaporanPembayaranRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State untuk Pedagang
    val pedagangViewModel: PedagangViewModel = viewModel(
        factory = PedagangViewModel.PedagangViewModelFactory(pedagangRepo)
    )
    val pedagangState by pedagangViewModel.formState.collectAsState()
    var pedagangId by remember { mutableLongStateOf(0L) }

    // State untuk Gerobak
    val gerobakViewModel: GerobakViewModel = viewModel(
        factory = GerobakViewModel.GerobakViewModelFactory(gerobakRepo)
    )
    var nomorSeri by remember { mutableStateOf("") }
    var gerobakId by remember { mutableLongStateOf(0L) }
    val radioOptions = listOf("Besar", "Kecil")
    val (selectedOptionGerobak, onOptionSelectedGerobak) = remember { mutableStateOf(radioOptions[0]) }
    val ukuran = selectedOptionGerobak

    // State untuk Lokasi Penitipan
    val lokasiViewModel: LokasiPenitipanViewModel = viewModel(
        factory = LokasiPenitipanViewModel.LokasiPenitipanViewModelFactory(lokasiRepo)
    )
    val lokasiList by lokasiViewModel.lokasiList.observeAsState(initial = emptyList())
    var selectedOptionLokasi by remember { mutableStateOf<Char?>(null) }

    LaunchedEffect(lokasiList) {
        if (lokasiList.isNotEmpty() && selectedOptionLokasi == null) {
            selectedOptionLokasi = lokasiList.first().namaArea
        }
    }

    lokasiViewModel.refreshLokasiPenitipan()

    // state untuk transaksi penitipan
    val transaksiViewModel: TransaksiPenitipanViewModel =
        viewModel(
            factory = TransaksiPenitipanViewModel.TransaksiPenitipanViewModelFactory(
                transaksiRepo, laporanPembayaranRepo
            )
        )

    val transaksiState by transaksiViewModel.formState.collectAsState()

    LaunchedEffect(selectedOptionGerobak) {
        val perkalianBaru = if (selectedOptionGerobak == "Besar") 200000 else 300000
        transaksiViewModel.updatePerkalian(perkalianBaru)
    }

    val currentDateTime = LocalDateTime.now()

    // format tanggal
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    // Format tanggal to String
    val formattedDateTime = currentDateTime.format(formatter)
    transaksiViewModel.updateTanggalPenitipan(formattedDateTime)

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var transaksiId by remember { mutableLongStateOf(0L) }

    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
            it?.let {
                val uri = saveImageToInternalStorage(context, it)
                imageUri = uri
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                imageUri = data?.data
            }
        }

    // state Laporan Pembayaran
    val paymentViewModel: LaporanPembayaranViewModel = viewModel(
        factory = LaporanPembayaranViewModel.LaporanPembayaranViewModelFactory(laporanPembayaranRepo)
    )
    var dialogPayment by remember { mutableStateOf(false) }

    // dropdown menu items laporan pembayaran
    val metodeItems = listOf("QRIS", "Transfer Bank", "Tunai")
    val statusItems = listOf("Lunas", "Pending", "Gagal")

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        PedagangBiodataSection(
            uiState = pedagangState,
            onNamaChange = { pedagangViewModel.updateNama(it) },
            onAlamatChange = { pedagangViewModel.updateAlamat(it) },
            onNoHpChange = { pedagangViewModel.updateNoHp(it) },
            onNoKTPChange = { pedagangViewModel.updateNoKTP(it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        GerobakInfoSection(
            nomorSeri = nomorSeri,
            onNomorSeriChange = { nomorSeri = it },
            selectedOption = selectedOptionGerobak,
            onOptionSelected = onOptionSelectedGerobak,
            onCameraClick = {
                cameraLauncher.launch(null)
            },
            onGalleryClick = {
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                }
                galleryLauncher.launch(intent)
            },
            textPath = imageUri ?: Uri.EMPTY
        )
        Spacer(modifier = Modifier.height(16.dp))
        LokasiPenitipanSection(
            selectedOption = selectedOptionLokasi,
            onOptionSelected = { selectedOptionLokasi = it },
            repo = lokasiRepo
        )
        Spacer(modifier = Modifier.height(16.dp))
        TransaksiPenitipanSection(
            viewModel = transaksiViewModel
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            colors = buttonColors(
                containerColor = Color(0xFF4768AF),
                contentColor = Color(0xFFF9F3F3),
                disabledContainerColor = Color(0xFF4768AF),
                disabledContentColor = Color(0xFFF9F3F3)
            ),
            onClick = {
                coroutineScope.launch {
                    try {
                        val pedagang = Pedagang(
                            nama = pedagangState.nama,
                            alamat = pedagangState.alamat,
                            noHP = pedagangState.noHp,
                            noKTP = pedagangState.noKTP
                        )
                        val newPedagangId = pedagangViewModel.insertPedagang(pedagang)
                        pedagangId = newPedagangId

                        val gerobak = Gerobak(
                            pedagangId = newPedagangId.toInt(),
                            nomorSeri = nomorSeri.toInt(),
                            ukuran = ukuran,
                            lokasiId = lokasiViewModel.getLokasiPenitipanIdByNamaArea(
                                selectedOptionLokasi
                            ).toInt(),
                            fotoUri = imageUri,
                        )
                        val newGerobakId = gerobakViewModel.insertGerobak(gerobak)
                        gerobakId = newGerobakId

                        val lokasiId = lokasiViewModel.getLokasiPenitipanIdByNamaArea(
                            selectedOptionLokasi).toInt()

                        val transaksi = TransaksiPenitipan(
                            pedagangId = newPedagangId.toInt(),
                            gerobakId = newGerobakId.toInt(),
                            lokasiId = lokasiId,
                            durasi = transaksiState.durasi,
                            totalPembayaran = transaksiState.totalPembayaran,
                            tanggalPenitipan = transaksiState.tanggalPenitipan,
                            tanggalPengambilan = transaksiState.tanggalPengambilan
                        )
                        transaksiId = transaksiViewModel.insertTransaksiPenitipan(transaksi)

                        Toast.makeText(
                            context,
                            "Data berhasil ditambahkan!",
                            Toast.LENGTH_SHORT
                        ).show()
                        pedagangViewModel.resetForm()
                        transaksiViewModel.resetForm()
                        dialogPayment = true
                    } catch (e: Exception) {
                        Log.e("CreateFormScreen", "Gagal menyimpan data: ${e.message}")
                        Toast.makeText(
                            context,
                            "Gagal menambahkan data. Error: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        ) {
            Text("Submit Data")
        }

        if (dialogPayment) {
            var metodeTextValue by remember(dialogPayment) { mutableStateOf(metodeItems[0]) }
            var statusTextValue by remember(dialogPayment) { mutableStateOf(statusItems[0]) }

            val selectedTransaksi by transaksiViewModel.selectedTransaksi.observeAsState()

            LaunchedEffect(transaksiId) {
                if (transaksiId != 0L) {
                    transaksiViewModel.getTransaksiPenitipanById(transaksiId)
                }
            }

            BasicAlertDialog(
                onDismissRequest = {},
                properties = DialogProperties(),
                content = {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, Color.Black)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Mockup Payment", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        LaporanPembayaranSection(
                            metodeItems = metodeItems,
                            statusItems = statusItems,
                            metodeTextValue = metodeTextValue,
                            statusTextValue = statusTextValue,
                            onMetodeClick = {
                                metodeTextValue = it
                            },
                            onStatusclick = {
                                statusTextValue = it
                            },
                            totalPembayaran = selectedTransaksi?.totalPembayaran.toString(),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(
                                onClick = {
                                    pedagangViewModel.deletePedagang(pedagangId)
                                    gerobakViewModel.deleteGerobak(gerobakId)
                                    transaksiViewModel.deleteTransaksiPenitipan(transaksiId)
                                    dialogPayment = false
                                },
                                colors = ButtonColors(
                                    containerColor = Color.Red,
                                    contentColor = Color.Red,
                                    disabledContainerColor = Color.Red,
                                    disabledContentColor = Color.Red
                                )
                            ) { Text(text = "Cancel", color = Color.White) }
                            Button(
                                onClick = {
                                    try {
                                        val payment = LaporanPembayaran(
                                            transaksiId = transaksiId.toInt(),
                                            metodePembayaran = metodeTextValue,
                                            statusPembayaran = statusTextValue,
                                            tanggalPembayaran = formattedDateTime
                                        )
                                        paymentViewModel.insertLaporanPembayaran(payment)
                                        Toast.makeText(
                                            context,
                                            "Data berhasil ditambahkan!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialogPayment = false
                                    } catch (e: Exception){
                                        Toast.makeText(
                                            context,
                                            "Gagal menambahkan data. Error: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        dialogPayment = false
                                    }
                                },
                                colors = ButtonColors(
                                    containerColor = Color.Blue,
                                    contentColor = Color.Blue,
                                    disabledContainerColor = Color.Blue,
                                    disabledContentColor = Color.Blue
                                )
                            ) { Text(text = "Simpan", color = Color.White) }
                        }
                    }
                }
            )
        }
    }
}