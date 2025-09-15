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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.linha.myboxstorage.data.Gerobak
import com.linha.myboxstorage.data.LaporanPembayaran
import com.linha.myboxstorage.data.TransaksiPenitipan
import com.linha.myboxstorage.repo.GerobakRepository
import com.linha.myboxstorage.repo.LaporanPembayaranRepository
import com.linha.myboxstorage.repo.LokasiPenitipanRepository
import com.linha.myboxstorage.repo.PedagangRepository
import com.linha.myboxstorage.repo.TransaksiPenitipanRepository
import com.linha.myboxstorage.utils.DisplayImageFromUri
import com.linha.myboxstorage.utils.Screen
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
@Composable
fun ShowPedagangScreen(
    pedagangRepo: PedagangRepository,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel: PedagangViewModel =
        viewModel(factory = PedagangViewModel.PedagangViewModelFactory(pedagangRepo))

    val pedagang by viewModel.pedagang.observeAsState(emptyList())
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()

    var dialog by remember { mutableStateOf(false) }

    var id by remember { mutableLongStateOf(0) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshPedagang() },
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Daftar Lengkap Pedagang", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(items = pedagang) { index, it ->
                    CardPedagang(
                        textId = "${index + 1}",
                        textNama = it.nama,
                        textNoKtp = it.noKTP,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        onCardClick = {
                            navController.navigate(Screen.DetailPedagang.createRoute(it.pedagangId))
                        },
                        onCardLongClick = {
                            dialog = true
                            id = it.pedagangId
                        }
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
                        Text(text = "Test Alert Dialog Hapus Pedagang")
                        Button(onClick = {
                            viewModel.deletePedagang(id)
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
fun CardPedagang(
    modifier: Modifier = Modifier,
    textId: String,
    textNama: String,
    textNoKtp: String,
    onCardClick: () -> Unit,
    onCardLongClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(Color(0xFF47A1AF), shape = RoundedCornerShape(12.dp))
            .border(1.dp, Color.White, shape = RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = onCardClick,
                onLongClick = onCardLongClick
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = textId,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = textNama,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            Text(
                text = textNoKtp,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}

data class TabItem(val title: String, val screen: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPedagangScreen(
    id: Long,
    navController: NavController,
    pedagangRepo: PedagangRepository,
    gerobakRepo: GerobakRepository,
    lokasiPenitipanRepo: LokasiPenitipanRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val viewModel: LokasiPenitipanViewModel = viewModel(
        factory = LokasiPenitipanViewModel.LokasiPenitipanViewModelFactory(lokasiPenitipanRepo))
    val pedagangViewModel: PedagangViewModel =
        viewModel(factory = PedagangViewModel.PedagangViewModelFactory(pedagangRepo))
    val gerobakViewModel: GerobakViewModel =
        viewModel(factory = GerobakViewModel.GerobakViewModelFactory(gerobakRepo))

    // State edit
    var isEditing by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showConfirmDialogDelete by remember { mutableStateOf(false) }
    var gerobakIdToDelete by remember { mutableStateOf<Int?>(null) }

    // State form input edit
    var namaInput by remember { mutableStateOf("") }
    var alamatInput by remember { mutableStateOf("") }
    var noHPInput by remember { mutableStateOf("") }
    var noKTPInput by remember { mutableStateOf("") }

    // State ViewModel
    val selectedPedagang by pedagangViewModel.selectedPedagang.observeAsState()
    val userWithGerobak by gerobakViewModel.userWithGerobak.observeAsState()
    val lokasi by viewModel.lokasiList.observeAsState(initial = emptyList())
    var editedGerobak by remember { mutableStateOf(mapOf<Int, Gerobak>()) }
    val radioOptionsGerobak = listOf("Besar", "Kecil")

    // State bobot kolom
    val firstColumnWeight = if (isEditing) 0.5f else 0.6f
    val secondColumnWeight = if (isEditing) 0.3f else 0.4f

    LaunchedEffect(id) {
        pedagangViewModel.getPedagangById(id)
        gerobakViewModel.getUserWithGerobak(id)
        viewModel.getAllLokasiPenitipan()
    }

    LaunchedEffect(selectedPedagang) {
        selectedPedagang?.let {
            namaInput = it.nama
            alamatInput = it.alamat
            noHPInput = it.noHP
            noKTPInput = it.noKTP
        }
    }

    LaunchedEffect(userWithGerobak) {
        userWithGerobak?.gerobak?.forEach { gerobak ->
            editedGerobak = editedGerobak.toMutableMap().apply {
                this[gerobak.gerobakId.toInt()] = gerobak
            }
        }
    }

    val tabItems = listOf(
        TabItem("Pedagang") {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                CustomTextFieldWithPlaceholder(
                    textPlaceholder = "Nama",
                    textValue = if (isEditing) namaInput else selectedPedagang?.nama ?: "Memuat...",
                    onChange = { namaInput = it }
                )
                CustomTextFieldWithPlaceholder(
                    textPlaceholder = "Alamat",
                    textValue = if (isEditing) alamatInput else selectedPedagang?.alamat
                        ?: "Memuat...",
                    onChange = { alamatInput = it }
                )
                CustomTextFieldWithPlaceholder(
                    textPlaceholder = "No HP",
                    textValue = if (isEditing) noHPInput else selectedPedagang?.noHP ?: "Memuat...",
                    onChange = { noHPInput = it }
                )
                CustomTextFieldWithPlaceholder(
                    textPlaceholder = "No KTP",
                    textValue = if (isEditing) noKTPInput else selectedPedagang?.noKTP
                        ?: "Memuat...",
                    onChange = { noKTPInput = it }
                )
            }
        },
        TabItem("Gerobak") {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                if (userWithGerobak != null && userWithGerobak!!.gerobak.isNotEmpty()) {
                    items(
                        items = userWithGerobak!!.gerobak,
                        key = { gerobak: Gerobak -> gerobak.gerobakId }
                    ) { gerobak ->
                        val currentGerobakState = editedGerobak[gerobak.gerobakId.toInt()] ?: gerobak

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(
                                modifier = Modifier.weight(firstColumnWeight)
                            ) {
                                CustomTextFieldWithPlaceholder(
                                    textPlaceholder = "Nomor Seri",
                                    textValue = if (isEditing) currentGerobakState.nomorSeri.toString() else gerobak.nomorSeri.toString(),
                                    onChange = {
                                        editedGerobak = editedGerobak.toMutableMap().apply {
                                            this[gerobak.gerobakId.toInt()] = currentGerobakState.copy(nomorSeri = it.toIntOrNull() ?: 0)
                                        }
                                    }
                                )
                                if (isEditing) {
                                    Column(modifier = Modifier.selectableGroup()) {
                                        radioOptionsGerobak.forEach { text ->
                                            Row(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .selectable(
                                                        selected = (text == currentGerobakState.ukuran),
                                                        onClick = {
                                                            editedGerobak = editedGerobak.toMutableMap().apply {
                                                                this[gerobak.gerobakId.toInt()] = currentGerobakState.copy(ukuran = text)
                                                            }
                                                        },
                                                        role = Role.RadioButton
                                                    ),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(selected = (text == currentGerobakState.ukuran), onClick = {})
                                                Text(
                                                    text = text,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    modifier = Modifier.padding(start = 16.dp)
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    CustomTextFieldWithPlaceholder(
                                        textPlaceholder = "Ukuran",
                                        textValue = gerobak.ukuran,
                                        onChange = {}
                                    )
                                }
                                if (isEditing) {
                                    Column(modifier = Modifier.selectableGroup()) {
                                        lokasi.forEach { item ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .selectable(
                                                        selected = (item.lokasiId == currentGerobakState.lokasiId.toLong()),
                                                        onClick = {
                                                            editedGerobak = editedGerobak.toMutableMap().apply {
                                                                this[gerobak.gerobakId.toInt()] = currentGerobakState.copy(lokasiId = item.lokasiId.toInt())
                                                            }
                                                        },
                                                        role = Role.RadioButton
                                                    ),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = (item.lokasiId == currentGerobakState.lokasiId.toLong()),
                                                    onClick = { }
                                                )
                                                Text(
                                                    text = "Area : " + item.namaArea.toString(),
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    CustomTextFieldWithPlaceholder(
                                        textPlaceholder = "Lokasi Penitipan",
                                        textValue = lokasi.find { it.lokasiId.toInt() == gerobak.lokasiId }?.namaArea.toString(),
                                        onChange = {}
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier
                                    .weight(secondColumnWeight),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (gerobak.fotoUri != null) {
                                    DisplayImageFromUri(gerobak.fotoUri)
                                } else {
                                    Text(text = "Tidak ada foto tersedia.")
                                }
                            }
                            if (isEditing) {
                                Column(
                                    modifier = Modifier
                                        .weight(0.2f)
                                        .background(Color.Red)
                                        .clickable {
                                            gerobakIdToDelete = gerobak.gerobakId.toInt()
                                            showConfirmDialogDelete = true
                                        }
                                        .fillMaxHeight()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        tint = Color.White,
                                        contentDescription = "Hapus Gerobak",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(20.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                    }
                } else {
                    item {
                        Text(text = "Memuat data gerobak...", modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center))
                    }
                }
            }
        }
    )

    val pagerState = rememberPagerState(pageCount = { tabItems.size })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail Pedagang") },
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
                            selectedPedagang?.let {
                                namaInput = it.nama
                                alamatInput = it.alamat
                                noHPInput = it.noHP
                                noKTPInput = it.noKTP
                            }
                            userWithGerobak?.gerobak?.forEach { gerobak ->
                                editedGerobak = editedGerobak.toMutableMap().apply {
                                    this[gerobak.gerobakId.toInt()] = gerobak
                                }
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.CreateGerobak.createRoute(id))
            },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(item.title) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
            ) { page ->
                tabItems[page].screen()
            }
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
                            pedagangViewModel.updateNama(namaInput)
                            pedagangViewModel.updateAlamat(alamatInput)
                            pedagangViewModel.updateNoHp(noHPInput)
                            pedagangViewModel.updateNoKTP(noKTPInput)
                            pedagangViewModel.updatePedagang(id = id)
                            editedGerobak.values.forEach { gerobak ->
                                gerobakViewModel.updateGerobak(gerobak)
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

    // dialog konfirmasi delete gerobak
    if (showConfirmDialogDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDialogDelete = false },
            title = { Text(text = "Hapus Gerobak") },
            text = { Text(text = "Apakah Anda yakin ingin hapus gerobak ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        gerobakIdToDelete?.let {
                            gerobakViewModel.deleteGerobak(it.toLong())
                            Toast.makeText(context, "Gerobak berhasil dihapus!", Toast.LENGTH_SHORT).show()
                        }
                        showConfirmDialogDelete = false
                    }
                ) {
                    Text("Ya, Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialogDelete = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateGerobakScreen(
    id: Long,
    pedagangRepo: PedagangRepository,
    gerobakRepo: GerobakRepository,
    lokasiRepo: LokasiPenitipanRepository,
    transaksiRepo: TransaksiPenitipanRepository,
    laporanPembayaranRepo: LaporanPembayaranRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val pedagangViewModel: PedagangViewModel =
        viewModel(factory = PedagangViewModel.PedagangViewModelFactory(pedagangRepo))

    val selectedPedagang by pedagangViewModel.selectedPedagang.observeAsState()

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

    LaunchedEffect(id) {
        pedagangViewModel.getPedagangById(id)
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
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Create Gerobak", style = MaterialTheme.typography.titleLarge)
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
                        val gerobak = Gerobak(
                            pedagangId = selectedPedagang?.pedagangId?.toInt() ?: 0,
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
                            pedagangId = selectedPedagang?.pedagangId?.toInt() ?: 0,
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
                                    pedagangViewModel.deletePedagang(selectedPedagang?.pedagangId ?: 0)
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

@Preview(showBackground = true)
@Composable
fun PedagangReview() {
//    val context = LocalContext.current
//    val repo = PedagangRepository(AppDatabase.getInstance(context).pedagangDao())
//    MinimalistAppTheme {
//        Column(Modifier.padding(20.dp)) {
//            CustomBasicTextFieldWithPlaceholder()
//        }
//    }
}