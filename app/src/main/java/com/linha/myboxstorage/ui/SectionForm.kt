package com.linha.myboxstorage.ui

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.linha.myboxstorage.repo.LokasiPenitipanRepository
import com.linha.myboxstorage.utils.toRupiahFormat
import com.linha.myboxstorage.viewmodel.LokasiPenitipanViewModel
import com.linha.myboxstorage.viewmodel.PedagangFormState
import com.linha.myboxstorage.viewmodel.TransaksiPenitipanViewModel

@Composable
fun CustomTextFieldWithPlaceholder(
    textPlaceholder: String,
    textValue: String,
    onChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = textValue,
        onValueChange = onChange,
        label = { Text(textPlaceholder) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        )
    )
}

@Composable
fun PedagangBiodataSection(
    uiState: PedagangFormState,
    onNamaChange: (String) -> Unit,
    onAlamatChange: (String) -> Unit,
    onNoHpChange: (String) -> Unit,
    onNoKTPChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(374.dp)
            .background(Color(0xFFF9F3F3), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, Color.White, shape = RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(text = "Biodata Pedagang :", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextFieldWithPlaceholder(
                textPlaceholder = "Nama",
                textValue = uiState.nama,
                onChange = onNamaChange
            )
            CustomTextFieldWithPlaceholder(
                textPlaceholder = "Alamat",
                textValue = uiState.alamat,
                onChange = onAlamatChange
            )
            CustomTextFieldWithPlaceholder(
                textPlaceholder = "Nomor HP",
                textValue = uiState.noHp,
                onChange = onNoHpChange,
                keyboardType = KeyboardType.Number
            )
            CustomTextFieldWithPlaceholder(
                textPlaceholder = "Nomor KTP",
                textValue = uiState.noKTP,
                onChange = onNoKTPChange,
                keyboardType = KeyboardType.Number
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GerobakInfoSection(
    nomorSeri: String,
    onNomorSeriChange: (String) -> Unit,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    textPath: Uri,
    modifier: Modifier = Modifier
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val radioOptions = listOf("Besar", "Kecil")

    Box(
        modifier = modifier
            .width(374.dp)
            .background(Color(0xFFF9F3F3), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, Color.White, shape = RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(text = "Informasi Gerobak :", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextFieldWithPlaceholder(
                textValue = nomorSeri,
                textPlaceholder = "Nomor Seri",
                onChange = onNomorSeriChange,
                keyboardType = KeyboardType.Number
            )
            Text(text = "Ukuran Gerobak :", modifier = Modifier.padding(top = 8.dp))
            Column(modifier = Modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (text == selectedOption), onClick = {})
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Ambil Foto Gerobak :")

            if (cameraPermissionState.status.isGranted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .border(width = 1.dp, Color.Black)
                            .clickable(
                                onClick = onCameraClick
                            )
                            .padding(8.dp)

                    ) {
                        Text(text = "Camera")
                    }
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .border(width = 1.dp, Color.Black)
                            .clickable(
                                onClick = onGalleryClick
                            )
                            .padding(8.dp)
                    ) {
                        Text(text = "Galeri")
                    }
                }
            } else {
                Text(text = "Camera Permission is required", color = Color.Red)
                Spacer(modifier = modifier.height(8.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text(text = "Request Camera Permission")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = if (textPath.path != null) textPath.path.toString() else "No image selected")
        }
    }
}

@Composable
fun LokasiPenitipanSection(
    selectedOption: Char?,
    onOptionSelected: (Char) -> Unit,
    repo: LokasiPenitipanRepository,
    modifier: Modifier = Modifier
) {
    val viewModel: LokasiPenitipanViewModel = viewModel(
        factory = LokasiPenitipanViewModel.LokasiPenitipanViewModelFactory(repo)
    )
    val lokasi by viewModel.lokasiList.observeAsState(initial = emptyList())

    Box(
        modifier = modifier
            .width(374.dp)
            .background(Color(0xFFF9F3F3), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(text = "Lokasi Penitipan :", style = MaterialTheme.typography.titleMedium)
            Column(modifier = Modifier.selectableGroup()) {
                lokasi.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (item.namaArea == selectedOption),
                                onClick = { onOptionSelected(item.namaArea) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (item.namaArea == selectedOption),
                            onClick = { onOptionSelected(item.namaArea) }
                        )
                        Text(
                            text = "Area : " + item.namaArea.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaksiPenitipanSection(
    modifier: Modifier = Modifier,
    viewModel: TransaksiPenitipanViewModel
) {
    val menuItems = listOf(1, 2, 3, 4, 5, 6)

    var expanded by remember { mutableStateOf(false) }

    val uiState by viewModel.formState.collectAsState()

    Box(
        modifier = modifier
            .width(374.dp)
            .background(Color(0xFFF9F3F3), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(text = "Transaksi Penitipan :", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = uiState.durasi.toString(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Durasi Penitipan (dalam bulan) :") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.toString()) },
                            onClick = {
                                viewModel.updateDurasi(item)
                                viewModel.updateTanggalPengambilan()
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Total Pembayaran :")
            Text(text = "Rp. " + uiState.totalPembayaran.toRupiahFormat())
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.Start) {
                Text(text = "Tanggal Penitipan :")
                Text(text = uiState.tanggalPenitipan)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.Start) {
                Text(text = "Tanggal Pengambilan :")
                Text(text = uiState.tanggalPengambilan)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanPembayaranSection(
    modifier: Modifier = Modifier,
    metodeItems: List<String>,
    statusItems: List<String>,
    statusTextValue: String,
    metodeTextValue: String,
    totalPembayaran: String,
    onMetodeClick: (String) -> Unit,
    onStatusclick: (String) -> Unit,
    isEditing: Boolean = true
) {
    var expandedMetode by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(Color(0xFFF9F3F3), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Column {
            ExposedDropdownMenuBox(
                expanded = expandedMetode,
                onExpandedChange = { expandedMetode = !expandedMetode }
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = metodeTextValue,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Metode Pembayaran :") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMetode)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    enabled = isEditing
                )

                ExposedDropdownMenu(
                    expanded = expandedMetode,
                    onDismissRequest = { expandedMetode = false }
                ) {
                    metodeItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                onMetodeClick(item)
                                expandedMetode = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expandedStatus,
                onExpandedChange = { expandedStatus = !expandedStatus }
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = statusTextValue,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status Pembayaran :") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    enabled = isEditing
                )

                ExposedDropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false }
                ) {
                    statusItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                onStatusclick(item)
                                expandedStatus = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Jumlah yang harus dibayar :")
            val total = totalPembayaran.toLongOrNull()
            if (total != null) {
                Text(text = "Rp. " + total.toRupiahFormat())
            } else {
                Text(text = "Rp. 0")
            }
        }
    }
}