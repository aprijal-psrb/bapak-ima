package com.bapakima.gambar

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var brushButton: Button
    private lateinit var penButton: Button

    private val swatches = mutableListOf<View>()

    /** Warna yang tersedia. Pilihan warna = satu-satunya fitur tambahan selain hapus. */
    private val palette = listOf(
        Color.parseColor("#111111"),
        Color.parseColor("#E53935"),
        Color.parseColor("#FB8C00"),
        Color.parseColor("#FDD835"),
        Color.parseColor("#43A047"),
        Color.parseColor("#1E88E5"),
        Color.parseColor("#8E24AA"),
        Color.parseColor("#8D6E63")
    )

    private val density: Float get() = resources.displayMetrics.density

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sembunyikanBilahSistem()

        drawingView = findViewById(R.id.drawingView)
        brushButton = findViewById(R.id.btnBrush)
        penButton = findViewById(R.id.btnPen)
        val hapusButton = findViewById<Button>(R.id.btnHapus)
        val simpanButton = findViewById<Button>(R.id.btnSimpan)
        val colorRow = findViewById<LinearLayout>(R.id.colorRow)

        brushButton.setOnClickListener { pilihAlat(DrawingView.Tool.BRUSH) }
        penButton.setOnClickListener { pilihAlat(DrawingView.Tool.PEN) }

        // Ketuk = hapus goresan terakhir. Tekan lama = bersihkan seluruh kanvas.
        hapusButton.setOnClickListener {
            val terhapus = drawingView.undoLast()
            tampilkanPesan(
                if (terhapus) "Goresan terakhir dihapus" else "Kanvas sudah kosong"
            )
        }
        hapusButton.setOnLongClickListener {
            drawingView.clearCanvas()
            tampilkanPesan("Kanvas dibersihkan")
            true
        }

        simpanButton.setOnClickListener { simpanKeGaleri() }

        buatSwatch(colorRow)
        pilihAlat(DrawingView.Tool.PEN)
        pilihWarna(0)
    }

    private fun sembunyikanBilahSistem() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun pilihAlat(tool: DrawingView.Tool) {
        drawingView.tool = tool
        brushButton.alpha = if (tool == DrawingView.Tool.BRUSH) 1f else 0.45f
        penButton.alpha = if (tool == DrawingView.Tool.PEN) 1f else 0.45f
    }

    private fun buatSwatch(row: LinearLayout) {
        val ukuran = (34 * density).toInt()
        val jarak = (8 * density).toInt()
        palette.forEachIndexed { index, _ ->
            val bulatan = View(this)
            val lp = LinearLayout.LayoutParams(ukuran, ukuran)
            lp.marginEnd = jarak
            bulatan.layoutParams = lp
            bulatan.setOnClickListener { pilihWarna(index) }
            row.addView(bulatan)
            swatches.add(bulatan)
        }
    }

    private fun pilihWarna(index: Int) {
        drawingView.inkColor = palette[index]
        swatches.forEachIndexed { i, bulatan ->
            bulatan.background = swatchDrawable(palette[i], i == index)
        }
    }

    private fun swatchDrawable(warna: Int, terpilih: Boolean): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(warna)
            if (terpilih) {
                setStroke((3 * density).toInt(), Color.parseColor("#37474F"))
            } else {
                setStroke((1 * density).toInt(), Color.parseColor("#B0BEC5"))
            }
        }

    /** Simpan kanvas ke galeri lewat MediaStore (tanpa izin tambahan di Android 10+). */
    private fun simpanKeGaleri() {
        val bitmap = drawingView.exportBitmap()
        val nama = "bapak-ima-" +
            SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date()) + ".png"

        Thread {
            var pesan = "Gagal menyimpan gambar"
            try {
                val nilai = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, nama)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Bapak Ima")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
                val resolver = contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, nilai)
                if (uri == null) {
                    pesan = "Gagal menyimpan: galeri tidak bisa diakses"
                } else {
                    resolver.openOutputStream(uri)?.use { keluaran ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, keluaran)
                    }
                    val selesai = ContentValues().apply {
                        put(MediaStore.Images.Media.IS_PENDING, 0)
                    }
                    resolver.update(uri, selesai, null, null)
                    pesan = "Tersimpan di galeri (Pictures/Bapak Ima)"
                }
            } catch (e: Exception) {
                pesan = "Gagal menyimpan: " + (e.message ?: "kesalahan tidak dikenal")
            }
            runOnUiThread { tampilkanPesan(pesan) }
        }.start()
    }

    private fun tampilkanPesan(teks: String) {
        Toast.makeText(this, teks, Toast.LENGTH_SHORT).show()
    }
}
