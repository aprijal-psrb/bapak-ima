package com.bapakima.gambar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Kanvas gambar sederhana: menyimpan daftar goresan (Path + Paint) lalu
 * menggambarnya ulang setiap kali ada perubahan. Hanya ada dua jenis alat:
 * BRUSH (tebal, tepi lembut) dan PEN (tipis, tajam).
 */
class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class Tool { BRUSH, PEN }

    /** Satu goresan: path-nya plus paint inti dan paint halo (khusus brush). */
    private class Stroke(
        val path: Path,
        val core: Paint,
        val halo: Paint?
    )

    private val strokes = ArrayList<Stroke>()
    private var activeStroke: Stroke? = null
    private var activePath: Path? = null
    private var lastX = 0f
    private var lastY = 0f

    private val density = resources.displayMetrics.density
    private val paperColor = Color.WHITE

    /** Alat yang sedang dipakai. */
    var tool: Tool = Tool.PEN

    /** Warna tinta yang sedang dipakai. */
    var inkColor: Int = Color.BLACK

    private fun dp(value: Float): Float = value * density

    private fun newPaint(color: Int, widthDp: Float, alphaValue: Int): Paint =
        Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            strokeWidth = dp(widthDp)
            this.color = color
            alpha = alphaValue
        }

    // ---- Goresan -----------------------------------------------------------

    private fun startStroke(x: Float, y: Float) {
        val core: Paint
        val halo: Paint?
        if (tool == Tool.BRUSH) {
            // Brush: inti tebal semi-pekat + halo lebih lebar beralfa rendah
            // supaya tepinya terlihat lembut tanpa perlu BlurMaskFilter.
            core = newPaint(inkColor, 20f, 225)
            halo = newPaint(inkColor, 30f, 70)
        } else {
            // Pen: tipis dan tajam.
            core = newPaint(inkColor, 2.6f, 255)
            halo = null
        }

        val path = Path().apply {
            moveTo(x, y)
            lineTo(x + 0.01f, y + 0.01f) // titik kecil saat jari hanya mengetuk
        }
        val stroke = Stroke(path, core, halo)
        strokes.add(stroke)
        activeStroke = stroke
        activePath = path
        lastX = x
        lastY = y
        invalidate()
    }

    private fun extendStroke(x: Float, y: Float) {
        val path = activePath ?: return
        // quadTo ke titik tengah membuat garis terlihat halus, tidak patah-patah.
        path.quadTo(lastX, lastY, (lastX + x) / 2f, (lastY + y) / 2f)
        lastX = x
        lastY = y
        invalidate()
    }

    private fun finishStroke(x: Float, y: Float) {
        activePath?.lineTo(x, y)
        activeStroke = null
        activePath = null
        invalidate()
    }

    // ---- Menggambar --------------------------------------------------------

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(paperColor)
        drawStrokes(canvas)
    }

    private fun drawStrokes(canvas: Canvas) {
        for (stroke in strokes) {
            stroke.halo?.let { canvas.drawPath(stroke.path, it) }
            canvas.drawPath(stroke.path, stroke.core)
        }
    }

    // ---- Aksi tombol -------------------------------------------------------

    /** Hapus goresan terakhir. Mengembalikan false kalau kanvas sudah kosong. */
    fun undoLast(): Boolean {
        if (strokes.isEmpty()) return false
        strokes.removeAt(strokes.size - 1)
        activeStroke = null
        activePath = null
        invalidate()
        return true
    }

    /** Bersihkan seluruh kanvas. */
    fun clearCanvas() {
        strokes.clear()
        activeStroke = null
        activePath = null
        invalidate()
    }

    /** Gambar ulang isi kanvas ke Bitmap untuk disimpan ke galeri. */
    fun exportBitmap(): Bitmap {
        val w = if (width > 0) width else 1
        val h = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(paperColor)
        drawStrokes(canvas)
        return bitmap
    }

    // ---- Sentuhan ----------------------------------------------------------

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                startStroke(event.x, event.y)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                // Pakai titik-titik historis supaya gerakan cepat tetap mulus.
                for (i in 0 until event.historySize) {
                    extendStroke(event.getHistoricalX(i), event.getHistoricalY(i))
                }
                extendStroke(event.x, event.y)
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                finishStroke(event.x, event.y)
                parent?.requestDisallowInterceptTouchEvent(false)
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
