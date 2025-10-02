# 🛒 Sistem Informasi Penitipan Gerobak Dagang

## Latar Belakang

Sebuah **koperasi pasar tradisional** telah meluncurkan layanan vital berupa **penitipan gerobak dagang**. Saat ini, seluruh alur kerja operasional—mulai dari pendaftaran pedagang, alokasi lokasi, hingga pencatatan transaksi pembayaran bulanan—masih dilakukan **secara manual**.

Untuk mengatasi inefisiensi dan meningkatkan akurasi data, proyek ini bertujuan mengembangkan sebuah **aplikasi *mobile* berbasis Android Kotlin**. Aplikasi ini akan digunakan oleh staf administrasi koperasi untuk mendigitalisasi dan mengelola seluruh kegiatan penitipan gerobak dagang secara terpusat.

---

## ✨ Fitur Utama Aplikasi Administrasi

Aplikasi ini dirancang dengan empat modul utama untuk menunjang pengelolaan penitipan gerobak yang efisien:

### 1. Modul Pedagang
* **CRUD** (*Create, Read, Update, Delete*) data pedagang.
* Pencatatan detail seperti: Nama, Alamat, Nomor HP, dan Nomor KTP.

### 2. Modul Gerobak
* **CRUD** data gerobak yang dititipkan.
* Pencatatan detail gerobak: Ukuran, Nomor Seri, dan Foto.

### 3. Modul Lokasi
* **CRUD** data lokasi penitipan.
* Pengelolaan kapasitas lokasi penitipan yang tersedia.

### 4. Modul Transaksi
* Input transaksi penitipan **bulanan**.
* Fungsi **Perhitungan Biaya Otomatis** berdasarkan durasi penitipan dan ukuran gerobak.

---

## 🛠️ Detail Teknologi

### Arsitektur
Aplikasi dibangun menggunakan pola arsitektur **MVVM (Model-View-ViewModel)** untuk menjamin kode yang terstruktur, mudah diuji, dan skalabel.

### Tumpukan Teknologi (Tech Stack)

| Kategori | Teknologi | Deskripsi |
| :--- | :--- | :--- |
| **Bahasa Pemrograman** | **Kotlin** | Bahasa utama untuk pengembangan aplikasi Android. |
| **UI Toolkit** | **Jetpack Compose** | *Toolkit* UI deklaratif modern. |
| **Navigasi** | Android Jetpack Navigation | Pengelolaan navigasi antar layar. |
| **Penyimpanan Data Lokal** | **Room Database** | Lapisan abstraksi di atas SQLite. |
| **Asinkron & Konkurensi** | **Coroutines** | Manajemen tugas asinkron yang efisien. |
| **Perizinan** | Accompanist Permission | Pustaka untuk menangani perizinan runtime. |
| **Desain** | Material Design 3 | Pedoman desain terbaru dari Google. |
| **Komponen Tambahan** | Google Fonts, Animation API | Digunakan untuk kustomisasi tipografi dan efek visual. |
