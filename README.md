# Bapak Ima

Aplikasi gambar Android yang sengaja dibuat sederhana: **hanya brush dan pen**, plus pilihan warna, tombol hapus, dan simpan ke galeri. Layar dikunci landscape.

## Unduh APK

APK dibangun otomatis oleh GitHub Actions setiap kali kode di branch `main` berubah.

**Link langsung (selalu versi terbaru):**

https://github.com/aprijal-psrb/bapak-ima/releases/download/apk-terbaru/bapak-ima.apk

Alternatif: buka tab **Actions** → jalankan workflow **Build APK** → unduh artifact `bapak-ima-apk`.

## Cara pasang di HP

1. Buka link APK di atas dari HP.
2. Setelah terunduh, ketuk berkasnya.
3. Kalau muncul peringatan, izinkan **Instal aplikasi tak dikenal** untuk browser/File manager yang dipakai.
4. Selesai — ikon **Bapak Ima** muncul di laci aplikasi.

Minimal Android 10 (API 29).

## Isi proyek

```
app/src/main/java/com/bapakima/gambar/
  DrawingView.kt   kanvas: simpan goresan, gambar, undo, hapus, ekspor bitmap
  MainActivity.kt  toolbar: pilih alat, pilih warna, hapus, simpan ke galeri
app/src/main/res/layout/activity_main.xml  tata letak toolbar + kanvas
.github/workflows/build-apk.yml            build APK otomatis
```

Teknologi: Kotlin, Android View (bukan Compose), AGP 8.5.2, Gradle 8.7, compileSdk 34, minSdk 29.

## Status fitur

Lihat [PANDUAN.md](PANDUAN.md).
