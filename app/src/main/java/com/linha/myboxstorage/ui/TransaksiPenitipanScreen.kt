package com.linha.myboxstorage.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.linha.myboxstorage.data.LaporanPembayaran
import com.linha.myboxstorage.data.TransaksiPenitipan
import com.linha.myboxstorage.repo.LaporanPembayaranRepository
import com.linha.myboxstorage.repo.TransaksiPenitipanRepository
import com.linha.myboxstorage.utils.Screen
import com.linha.myboxstorage.utils.toRupiahFormat
import com.linha.myboxstorage.viewmodel.LaporanPembayaranViewModel
import com.linha.myboxstorage.viewmodel.TransaksiPenitipanViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTransaksiPenitipanScreen(
    transaksiRepo: TransaksiPenitipanRepository,
    laporanPembayaranRepo: LaporanPembayaranRepository,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel: TransaksiPenitipanViewModel =
        viewModel(factory = TransaksiPenitipanViewModel.TransaksiPenitipanViewModelFactory(transaksiRepo, laporanPembayaranRepo))

    val transaksiDisplayList by viewModel.transaksiDisplayList.observeAsState(emptyList())
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()

    var dialog by remember { mutableStateOf(false) }

    var id by remember { mutableLongStateOf(0) }

    LaunchedEffect(key1 = true) {
        viewModel.getAllTransaksiPenitipanForDisplay()
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshTransaksiPenitipan() },
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Riwayat Transaksi", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(items = transaksiDisplayList) { index, it ->
                    CardTransaksi(
                        textNama = it.namaPedagang,
                        textNomorSeri = it.nomorSeriGerobak,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        onCardClick = {
                            navController.navigate(Screen.DetailTransaksi.createRoute(it.id))
                        },
                        onCardLongClick = {
                            dialog = true
                            id = it.id
                        },
                        textStatusPayment = it.statusPembayaran,
                        textTotalPembayaran = it.totalPembayaran.toString(),
                        textTanggalPenitipan = it.tanggalPenitipan,
                        textTanggalPengambilan = it.tanggalPengambilan
                    )
                }
            }
        }

        if (dialog) {
            BasicAlertDialog(
                onDismissRequest = { dialog = false },
                properties = DialogProperties(),
                content = {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, Color.Black)
                            .padding(20.dp)
                    ) {
                        Text(text = "Test Alert Dialog Hapus Riwayat Transaksi")
                        Button(onClick = {
                            viewModel.deleteTransaksiPenitipan(id)
                        }) {
                            Text(text = "Delete")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CardTransaksi(
    modifier: Modifier = Modifier,
    textNama: String,
    textNomorSeri: String,
    textStatusPayment: String,
    textTotalPembayaran: String,
    textTanggalPenitipan: String,
    textTanggalPengambilan: String,
    onCardClick: () -> Unit,
    onCardLongClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = onCardClick,
                onLongClick = onCardLongClick
            )
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = textNama,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = textNomorSeri,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = textStatusPayment,
                        color = if (textStatusPayment == "Lunas") Color.Green else if (textStatusPayment == "Pending") Color.Gray else Color.Red,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Rp. ${textTotalPembayaran.toLong().toRupiahFormat()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textTanggalPenitipan,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = textTanggalPengambilan,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTransaksiPenitipanScreen(
    id: Long,
    transaksiPenitipanRepo: TransaksiPenitipanRepository,
    laporanPembayaranRepo: LaporanPembayaranRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val transaksiPenitipanViewModel: TransaksiPenitipanViewModel =
        viewModel(factory = TransaksiPenitipanViewModel.TransaksiPenitipanViewModelFactory(transaksiPenitipanRepo, laporanPembayaranRepo))

    val laporanPembayaranViewModel: LaporanPembayaranViewModel =
        viewModel(factory = LaporanPembayaranViewModel.LaporanPembayaranViewModelFactory(laporanPembayaranRepo))

    // State edit
    var isEditing by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // State form input edit
    var durasi by remember { mutableIntStateOf(0) }
    var tanggalPenitipan by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // State for the exposed dropdown menu
    var expanded by remember { mutableStateOf(false) }
    val menuItems = listOf(1, 2, 3, 4, 5, 6)

    // State form input for LaporanPembayaran
    var metodePembayaranInput by remember { mutableStateOf("") }
    var statusPembayaranInput by remember { mutableStateOf("") }
    var totalPembayaranInput by remember { mutableStateOf("") }

    // State ViewModel
    val selectedTransaksi by transaksiPenitipanViewModel.selectedTransaksi.observeAsState()
    val userWithTransaksi by transaksiPenitipanViewModel.userWithTransaksi.observeAsState()
    val gerobakWithTransaksi by transaksiPenitipanViewModel.gerobakWithTransaksi.observeAsState()
    val lokasiWithTransaksi by transaksiPenitipanViewModel.lokasiWithTransaksi.observeAsState()
    val uiSelectedTransaksi by transaksiPenitipanViewModel.formState.collectAsState()
    val selectedLaporanPembayaran by laporanPembayaranViewModel.selectedLaporan.observeAsState()

    LaunchedEffect(id) {
        transaksiPenitipanViewModel.getTransaksiPenitipanById(id)
        laporanPembayaranViewModel.getLaporanPembayaranById(id)
    }

    LaunchedEffect(selectedTransaksi) {
        selectedTransaksi?.let {
            durasi = it.durasi
            tanggalPenitipan = LocalDate.parse(it.tanggalPenitipan, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            transaksiPenitipanViewModel.getUserWithTransaksi(it.pedagangId.toLong())
            transaksiPenitipanViewModel.getGerobakWithTransaksi(it.gerobakId.toLong())
            transaksiPenitipanViewModel.getLokasiWithTransaksi(it.lokasiId.toLong())
            totalPembayaranInput = it.totalPembayaran.toString()
        }
    }
    LaunchedEffect(selectedLaporanPembayaran) {
        selectedLaporanPembayaran?.let {
            metodePembayaranInput = it.metodePembayaran
            statusPembayaranInput = it.statusPembayaran
        }
    }

    LaunchedEffect(gerobakWithTransaksi) {
        val perkalianBaru = if (gerobakWithTransaksi?.gerobak?.ukuran == "Besar") 200000 else 300000
        transaksiPenitipanViewModel.updatePerkalian(perkalianBaru)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail Transaksi") },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = { showConfirmDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Simpan Perubahan"
                            )
                        }
                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Data"
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (isEditing) {
                        IconButton(onClick = {
                            isEditing = false
                            selectedTransaksi?.let {
                                durasi = it.durasi
                                tanggalPenitipan = LocalDate.parse(it.tanggalPenitipan, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                            }
                            selectedLaporanPembayaran?.let {
                                metodePembayaranInput = it.metodePembayaran
                                statusPembayaranInput = it.statusPembayaran
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Batal"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Nama Pedagang: ${userWithTransaksi?.user?.nama ?: "Memuat..."}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Nomor Seri Gerobak: ${gerobakWithTransaksi?.gerobak?.nomorSeri ?: "Memuat..."}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Area Lokasi: ${lokasiWithTransaksi?.lokasi?.namaArea ?: "Memuat..."}",
                style = MaterialTheme.typography.bodyLarge
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { if (isEditing) expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = durasi.toString(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Durasi Penitipan (dalam bulan) :") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    enabled = isEditing
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.toString()) },
                            onClick = {
                                durasi = item
                                transaksiPenitipanViewModel.updateDurasi(item)
                                transaksiPenitipanViewModel.updateTanggalPengambilan()
                                expanded = false
                            }
                        )
                    }
                }
            }
            Text(
                text = "Tanggal Penitipan: ${tanggalPenitipan.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = if (isEditing) Modifier.clickable { showDatePicker = true } else Modifier
            )
            Text(
                text = "Tanggal Pengambilan: ${tanggalPenitipan.plusMonths(durasi.toLong()).format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy"))}",
                style = MaterialTheme.typography.bodyLarge
            )
            LaporanPembayaranSection(
                metodeItems = listOf("QRIS","Tunai", "Transfer"),
                statusItems = listOf("Lunas", "Belum Lunas"),
                metodeTextValue = if (isEditing) metodePembayaranInput else selectedLaporanPembayaran?.metodePembayaran ?: "Memuat...",
                statusTextValue = if (isEditing) statusPembayaranInput else selectedLaporanPembayaran?.statusPembayaran ?: "Memuat...",
                totalPembayaran = totalPembayaranInput,
                onMetodeClick = { if (isEditing) metodePembayaranInput = it },
                onStatusclick = { if (isEditing) statusPembayaranInput = it },
                isEditing = isEditing
            )
        }
    }

    // Dialog Konfirmasi editing
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(text = "Konfirmasi Pembaruan") },
            text = { Text(text = "Apakah Anda yakin ingin menyimpan perubahan ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            transaksiPenitipanViewModel.updateTransaksiPenitipan(
                                TransaksiPenitipan(
                                    id = selectedTransaksi?.id ?: 0,
                                    pedagangId = selectedTransaksi?.pedagangId ?: 0,
                                    gerobakId = selectedTransaksi?.gerobakId ?: 0,
                                    lokasiId = selectedTransaksi?.lokasiId ?: 0,
                                    durasi = durasi,
                                    totalPembayaran = uiSelectedTransaksi.totalPembayaran,
                                    tanggalPenitipan = tanggalPenitipan.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                    tanggalPengambilan = tanggalPenitipan.plusMonths(durasi.toLong()).format(
                                        DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                )
                            )
                            selectedLaporanPembayaran?.let {
                                laporanPembayaranViewModel.updateLaporanPembayaran(
                                    LaporanPembayaran(
                                        id = it.id,
                                        transaksiId = it.transaksiId,
                                        metodePembayaran = metodePembayaranInput,
                                        statusPembayaran = statusPembayaranInput,
                                        tanggalPembayaran = tanggalPenitipan.toString()
                                    )
                                )
                            }
                            Toast.makeText(context, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                            isEditing = false
                            showConfirmDialog = false
                        }
                    }
                ) {
                    Text("Ya, Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = tanggalPenitipan.atStartOfDay(
            ZoneId.systemDefault()).toInstant().toEpochMilli())
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            tanggalPenitipan = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}