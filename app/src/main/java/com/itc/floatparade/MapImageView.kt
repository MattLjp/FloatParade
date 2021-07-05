package com.itc.floatparade

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView

/**
 * @ Author : 廖健鹏
 * @ Date : 2021/7/5
 * @ e-mail : 329524627@qq.com
 * @ Description :
 */
class MapImageView : AppCompatImageView {

    private var paint: Paint? = null

    private var offset = IntArray(2)


    private var signOffsetList = listOf<IntArray>()
    private var drawRouteFlag = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (drawRouteFlag) {
            drawRouteFlag = false
            getRoute(canvas)
        }
    }

    fun createRoute(list: List<IntArray>) {
        drawRouteFlag = true
        signOffsetList = list
        invalidate()
    }


    /**
     * 画出坐标点的连线
     * @param canvas Canvas
     */
    private fun getRoute(canvas: Canvas) {
        if (paint == null) {
            paint = Paint()
            paint?.color = Color.BLUE
            paint?.style = Paint.Style.FILL
            paint?.strokeWidth = 3f
        }


        for (i in 0 until signOffsetList.size - 1) {
            canvas.drawLine(
                (signOffsetList[i][0] + offset[0]).toFloat(),
                (signOffsetList[i][1] + offset[1]).toFloat(),
                (signOffsetList[i + 1][0] + offset[0]).toFloat(),
                (signOffsetList[i + 1][1] + offset[1]).toFloat(),
                paint!!
            )
        }
    }


    /**
     * 计算图像在ImageView的位移量
     * @param img ImageView
     * @param includeLayout Boolean
     * @return IntArray?
     */
    fun getBitmapOffset(includeLayout: Boolean): IntArray {
        val values = FloatArray(9)
        val m: Matrix = imageMatrix
        m.getValues(values)
        offset[0] = values[2].toInt()
        offset[1] = values[5].toInt()

        if (includeLayout) {
            val lp = layoutParams as ViewGroup.MarginLayoutParams
            offset[0] += paddingLeft + lp.leftMargin
            offset[1] += paddingTop + lp.topMargin
        }
        return offset
    }

}