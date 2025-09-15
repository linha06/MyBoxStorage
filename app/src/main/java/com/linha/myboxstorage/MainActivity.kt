package com.linha.myboxstorage

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.linha.courseadvdb.db.AppDatabase
import com.linha.myboxstorage.repo.GerobakRepository
import com.linha.myboxstorage.repo.LaporanPembayaranRepository
import com.linha.myboxstorage.repo.LokasiPenitipanRepository
import com.linha.myboxstorage.repo.PedagangRepository
import com.linha.myboxstorage.repo.TransaksiPenitipanRepository
import com.linha.myboxstorage.ui.MainScreen
import com.linha.myboxstorage.ui.theme.MyBoxStorageTheme
import com.linha.myboxstorage.ui.theme.minimalist.MinimalistAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var pedagangRepo: PedagangRepository
    private lateinit var gerobakRepo: GerobakRepository
    private lateinit var lokasiRepo: LokasiPenitipanRepository
    private lateinit var transaksiPenitipanRepo: TransaksiPenitipanRepository
    private lateinit var laporanPembayaranRepo: LaporanPembayaranRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pedagangRepo = PedagangRepository(AppDatabase.getInstance(applicationContext).pedagangDao())
        gerobakRepo = GerobakRepository(AppDatabase.getInstance(applicationContext).gerobakDao())
        lokasiRepo = LokasiPenitipanRepository(AppDatabase.getInstance(applicationContext).lokasiPenitipanDao())
        transaksiPenitipanRepo = TransaksiPenitipanRepository(AppDatabase.getInstance(applicationContext).transaksiPenitipanDao())
        laporanPembayaranRepo = LaporanPembayaranRepository(AppDatabase.getInstance(applicationContext).laporanPembayaranDao())

        enableEdgeToEdge()
        setContent {
            MinimalistAppTheme(dynamicColor = false) {
                MainScreen(
                    pedagangRepo = pedagangRepo,
                    gerobakRepo = gerobakRepo,
                    lokasiPenitipanRepo = lokasiRepo,
                    transaksiPenitipanRepo = transaksiPenitipanRepo,
                    laporanPembayaranRepo = laporanPembayaranRepo
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyBoxStorageTheme {
//        Greeting("Android")
    }
}