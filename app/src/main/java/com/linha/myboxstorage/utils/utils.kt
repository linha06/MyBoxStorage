package com.linha.myboxstorage.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage
import java.text.NumberFormat
import java.util.Locale

fun saveImageToInternalStorage(context: Context, bitMap: Bitmap): Uri? {
    val filename = "photo_${System.currentTimeMillis()}.jpg"
    val outputStream = context.openFileOutput(filename, MODE_PRIVATE)
    bitMap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.close()
    return Uri.fromFile(context.getFileStreamPath(filename))
}

@Suppress("DEPRECATION")
fun Long.toRupiahFormat(): String {
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    return formatRupiah.format(this).replace("Rp", "").trim()
}

@Composable
fun DisplayImageFromUri(imageUri: Uri?) {
    imageUri?.let {
        AsyncImage(
            model = it,
            contentDescription = "Gambar yang dipilih"
        )
    }
}