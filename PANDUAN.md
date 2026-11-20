# Panduan Pemakaian — Bapak Ima

Aplikasi gambar untuk Android 10 ke atas. Layar otomatis landscape dan bilah sistem disembunyikan supaya kanvas lega. Untuk memunculkan bilah sistem sementara, geser dari tepi layar.

## Bagian layar

- **Bar atas** berisi: tombol **Brush**, tombol **Pen**, deretan bulatan warna, tombol **Hapus**, tombol **Simpan**.
- **Sisanya kanvas putih** seluas layar.

## Memilih alat

- Ketuk **Pen** untuk garis tipis dan tajam. Cocok untuk sketsa, tulisan, dan detail.
- Ketuk **Brush** untuk garis tebal dengan tepi lembut. Cocok untuk mewarnai bidang besar.
- Tombol alat yang aktif tampil penuh; yang tidak aktif tampak pudar.

Ukuran goresan sudah ditetapkan per alat (pen tipis, brush tebal), jadi tidak ada pengatur ukuran — sesuai permintaan agar tetap simpel.

## Memilih warna

Ketuk salah satu bulatan warna. Bulatan yang dipilih diberi cincin tebal. Ada 8 warna: hitam, merah, oranye, kuning, hijau, biru, ungu, coklat. Warna berlaku untuk goresan berikutnya; goresan yang sudah ada tidak berubah.

## Menghapus

- **Ketuk Hapus** = membatalkan satu goresan terakhir (undo). Bisa diketuk berkali-kali.
- **Tekan lama Hapus** = membersihkan seluruh kanvas, mulai dari kanvas kosong.

## Menyimpan ke galeri

1. Ketuk **Simpan**.
2. Tunggu pesan “Tersimpan di galeri (Pictures/Bapak Ima)”.
3. Buka aplikasi Galeri/Foto → album **Pictures/Bapak Ima**. Berkas bernama `bapak-ima-TAHUNBULANTANGGAL-JAMMENITDETIK.png`.

Gambar disimpan sebagai PNG dengan latar putih. Tidak perlu izin penyimpanan tambahan di Android 10 ke atas.

## Tips

- Gambar tetap ada selama aplikasi belum ditutup; kalau aplikasi ditutup, kanvas mulai kosong lagi. Simpan dulu sebelum keluar.
- Rotasi layar tidak mengubah gambar karena aplikasi dikunci landscape.

---

## Daftar fitur

### Sudah dikerjakan

| Fitur | Keterangan |
|---|---|
| Dua alat gambar | Brush (tebal, tepi lembut) dan Pen (tipis, tajam) |
| Pilihan warna | 8 warna, penanda warna aktif |
| Hapus goresan terakhir | Ketuk tombol Hapus |
| Hapus seluruh kanvas | Tekan lama tombol Hapus |
| Simpan ke galeri | PNG ke `Pictures/Bapak Ima` via MediaStore |
| Goresan halus | Kurva kuadratik + titik historis sentuhan |
| Kunci landscape | `sensorLandscape`, bilah sistem disembunyikan |
| APK otomatis | GitHub Actions membangun APK setiap push ke `main` |

### Belum dikerjakan (sengaja ditahan)

| Fitur | Alasan |
|---|---|
| Pengatur ukuran goresan | Tidak diminta; ukuran tetap per alat |
| Pemilih warna bebas (color picker) | Dibatasi ke 8 warna preset agar UI tetap bersih |
| Layer, bentuk/geometri, teks, penghapus khusus, kuas cat air | Di luar cakupan “hanya brush dan pen” |
| Menyimpan/membuka proyek yang bisa diedit lagi | Hasil hanya diekspor sebagai PNG |
| Undo bertingkat dengan tombol terpisah | Undo menempel pada tombol Hapus agar toolbar tetap ringkas |
| Mode portrait | Aplikasi dikunci landscape sesuai permintaan |
| Ikon launcher kustom | Memakai ikon adaptif sederhana bawaan proyek |
