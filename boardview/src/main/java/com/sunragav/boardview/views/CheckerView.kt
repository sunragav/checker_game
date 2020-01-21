package com.sunragav.boardview.views

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import com.sunragav.boardview.R
import com.sunragav.boardview.domain.models.Frame
import kotlin.math.min


class CheckerView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    companion object {
        private const val DEFAULT_EVEN_COLOR = Color.WHITE
        private const val DEFAULT_ODD_COLOR = "#D6DEE5"
        private const val DEFAULT_TILE_SIZE_IN_DP = 40.0f
        private const val DEFAULT_BOARD_SIZE = 7

    }

    private val defaultOddColor = Color.parseColor(DEFAULT_ODD_COLOR)
    private var coinCoordScaleOffset: Float = 0f
    private var coinCoordScaleFactor: Float = 1f
    private var oddColor = defaultOddColor
    private var evenColor = DEFAULT_EVEN_COLOR
    private var tileSizeInDP = DEFAULT_TILE_SIZE_IN_DP
    private var coinR = BitmapFactory.decodeResource(resources, R.drawable.player_r)
    private var coinG = BitmapFactory.decodeResource(resources, R.drawable.player_g)
    private var coinB = BitmapFactory.decodeResource(resources, R.drawable.player_b)
    private var coinSize: Float = coinB.width.toFloat()
    private val paint = Paint()
    private var size = 0
    private var tileSize = 0
    private lateinit var checkerBitmap: Bitmap

    init {
        paint.isAntiAlias = true
        setupAttributes(attrs)
    }

    var currentFrame: Int? = null
        set(value) {
            field = value
            invalidate()
        }

    var rFrame: Frame? = null
    var gFrame: Frame? = null
    var bFrame: Frame? = null

    private fun setupAttributes(attrs: AttributeSet?) {
        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.CheckerBoardView,
            0, 0
        )

        // Extract custom attributes into member variables
        oddColor = typedArray.getColor(R.styleable.CheckerBoardView_oddCellColor, defaultOddColor)
        evenColor =
            typedArray.getColor(R.styleable.CheckerBoardView_evenCellColor, DEFAULT_EVEN_COLOR)
        tileSizeInDP =
            typedArray.getDimension(R.styleable.CheckerBoardView_tileSize, DEFAULT_TILE_SIZE_IN_DP)
        size = typedArray.getInt(R.styleable.CheckerBoardView_boardSize, DEFAULT_BOARD_SIZE)

        typedArray.recycle()

    }


    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        drawCheckerBoard(canvas)
        currentFrame?.let {
            drawPieces(canvas)
        }
    }

    private fun dpToPx(dp: Float) = dp * context.resources.displayMetrics.density

    private fun drawCheckerBoard(canvas: Canvas) {
        canvas.drawBitmap(checkerBitmap, 0f, 0f, paint)
    }

    private fun configureTilesPaint() {
        val bitmap = Bitmap.createBitmap(tileSize * 2, tileSize * 2, Bitmap.Config.ARGB_8888)

        val bitmapPaint = Paint(ANTI_ALIAS_FLAG)
        bitmapPaint.style = Paint.Style.FILL

        val canvas = Canvas(bitmap)

        val rect = Rect(0, 0, tileSize, tileSize)
        bitmapPaint.color = oddColor
        canvas.drawRect(rect, bitmapPaint)

        rect.offset(tileSize, tileSize)
        canvas.drawRect(rect, bitmapPaint)

        bitmapPaint.color = evenColor
        rect.offset(-tileSize, 0)
        canvas.drawRect(rect, bitmapPaint)

        rect.offset(tileSize, -tileSize)
        canvas.drawRect(rect, bitmapPaint)

        val checkerPaint = Paint(ANTI_ALIAS_FLAG)
        checkerPaint.shader = BitmapShader(
            bitmap,
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        )


        val checkerBoardCanvas = Canvas(checkerBitmap)

        checkerBoardCanvas.drawPaint(checkerPaint)
    }

    private fun drawPieces(canvas: Canvas) {
        rFrame?.let { drawPiece(canvas, coinR, it) }
        gFrame?.let { drawPiece(canvas, coinG, it) }
        bFrame?.let { drawPiece(canvas, coinB, it) }
    }

    private fun drawPiece(canvas: Canvas, coin: Bitmap, currentFrame: Frame) {
        val x = currentFrame.x * coinCoordScaleFactor + coinCoordScaleOffset
        val y = (size - 1 - currentFrame.y) * coinCoordScaleFactor + coinCoordScaleOffset

        canvas.drawBitmap(coin, x, y, paint)
    }


    private fun getResizedBitmap(bm: Bitmap, newSize: Float): Bitmap {
        val width = bm.width
        val height = bm.height
        if (width == newSize.toInt() && height == newSize.toInt()) return bm
        val scaleSize = newSize / width
        val matrix = Matrix()
        matrix.postScale(scaleSize, scaleSize)
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val screenSize = min(measuredWidth, measuredHeight)

        setMeasuredDimension(screenSize, screenSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        tileSize = if (tileSizeInDP == 0f)
            w / size
        else
            dpToPx(tileSizeInDP).toInt()
        checkerBitmap =
            Bitmap.createBitmap(tileSize * size, tileSize * size, Bitmap.Config.ARGB_8888)



        configureTilesPaint()
        coinSize = dpToPx(min(coinSize, tileSizeInDP - 10))


        coinCoordScaleFactor = tileSize.toFloat()
        coinCoordScaleOffset = (tileSize - coinSize) / 2f
        coinR = getResizedBitmap(coinR, coinSize)
        coinG = getResizedBitmap(coinG, coinSize)
        coinB = getResizedBitmap(coinB, coinSize)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coinB.recycle()
        coinR.recycle()
        coinG.recycle()
        checkerBitmap.recycle()
    }
}
