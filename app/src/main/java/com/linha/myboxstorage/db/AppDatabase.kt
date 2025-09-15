package com.linha.courseadvdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.linha.myboxstorage.dao.GerobakDao
import com.linha.myboxstorage.dao.LaporanPembayaranDao
import com.linha.myboxstorage.dao.LokasiPenitipanDao
import com.linha.myboxstorage.dao.PedagangDao
import com.linha.myboxstorage.dao.TransaksiPenitipanDao
import com.linha.myboxstorage.data.Gerobak
import com.linha.myboxstorage.data.LaporanPembayaran
import com.linha.myboxstorage.data.LokasiPenitipan
import com.linha.myboxstorage.data.Pedagang
import com.linha.myboxstorage.data.TransaksiPenitipan
import com.linha.myboxstorage.utils.UriTypeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Pedagang::class,
        Gerobak::class,
        LokasiPenitipan::class,
        TransaksiPenitipan::class,
        LaporanPembayaran::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(UriTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pedagangDao(): PedagangDao
    abstract fun gerobakDao(): GerobakDao
    abstract fun lokasiPenitipanDao(): LokasiPenitipanDao
    abstract fun transaksiPenitipanDao(): TransaksiPenitipanDao
    abstract fun laporanPembayaranDao(): LaporanPembayaranDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addCallback(
                    callback = AppDatabaseCallback(context)
                ).build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE user_table ADD COLUMN age INTEGER DEFAULT NULL")
            }
        }
    }

    private class AppDatabaseCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                val userDao = database.lokasiPenitipanDao()
                prePopulateDbLokasi(userDao)
            }
        }

        suspend fun prePopulateDbLokasi(lokasiPenitipanDao: LokasiPenitipanDao) {
            val areaA = LokasiPenitipan(namaArea = 'A', kapasitasMaks = 10)
            val areaB = LokasiPenitipan(namaArea = 'B', kapasitasMaks = 15)
            val areaC = LokasiPenitipan(namaArea = 'C', kapasitasMaks = 18)
            lokasiPenitipanDao.insertLokasiPenitipan(areaA)
            lokasiPenitipanDao.insertLokasiPenitipan(areaB)
            lokasiPenitipanDao.insertLokasiPenitipan(areaC)
        }
    }
}