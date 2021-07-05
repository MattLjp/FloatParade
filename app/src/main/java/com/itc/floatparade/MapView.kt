package com.itc.floatparade

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.customview.widget.ViewDragHelper


/**
 * @ Author : 廖健鹏
 * @ Date : 2021/7/1
 * @ e-mail : 329524627@qq.com
 * @ Description :
 */
class MapView : FrameLayout {
    private val TAG = MapView::class.java.simpleName

    //地图图片
    private var mapImage = ImageView(context)

    private var mapWidth = 0

    private var mapHeight = 0

    private var mapLeft = 0

    private var mapTop = 0

    private var signBeanList = listOf<SignBean>()

    private var signOffsetList = mutableListOf<IntArray>()

    private var signViewList = mutableListOf<SignView>()

    private var capturedViewIndex = 0

    private val mDragger: ViewDragHelper =
        ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return child != mapImage
            }

            override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
                signViewList.forEachIndexed { index, signView ->
                    if (signView == capturedChild) {
                        capturedViewIndex = index
                        return@forEachIndexed
                    }
                }
            }

            override fun onViewPositionChanged(
                changedView: View,
                left: Int,
                top: Int,
                dx: Int,
                dy: Int
            ) {
                signOffsetList[capturedViewIndex][0] += dx
                signOffsetList[capturedViewIndex][1] += dy
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                val move = if (left <= mapLeft)
                    mapLeft
                else if (left >= mapWidth + mapLeft)
                    mapWidth + mapLeft
                else
                    left
                return move
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                val move = if (top <= mapTop)
                    mapTop
                else if (top >= mapHeight + mapTop)
                    mapHeight + mapLeft
                else
                    top
                return move
            }
        })

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context, attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    /**
     * 添加地图图片
     * @param resId Int
     */
    fun setMapImage(@DrawableRes resId: Int) {
        removeAllViews()
        mapImage.setImageResource(resId)
        addView(mapImage)
    }


    /**
     * 设置坐标列表
     * @param list List<SignBean>
     */
    fun setSignData(list: List<SignBean>) {
        val mapOffset = getBitmapOffset(mapImage, true)
        mapLeft = mapOffset[0]
        mapTop = mapOffset[1]

        mapWidth = mapImage.width - mapLeft * 2
        mapHeight = mapImage.height - mapTop * 2

        var signOffset = IntArray(2)
        var boolean = true

        Log.d(TAG, "mapWidth:$mapWidth, mapHeight:$mapHeight, mapLeft:$mapLeft, mapTop:$mapTop")

        signBeanList = list
        removeViews(1, childCount - 1)
        signViewList.clear()
        signOffsetList.clear()
        list.forEach {
            val signView = SignView(context).apply {
                setData(it)
            }
            // 只需要计算一次
            if (boolean) {
                boolean = false
                signOffset = signView.getSignOffset()
            }
            signView.layoutParams = getParams(it, signOffset)
            addView(signView)
            signViewList.add(signView)
            signOffsetList.add(intArrayOf((it.x * mapWidth).toInt(), (it.y * mapHeight).toInt()))
        }
    }


    /**
     * 获取移动后的坐标信息
     * @return List<SignBean>
     */
    fun getMoveSignData(): List<SignBean> {
        val data = mutableListOf<SignBean>()
        signOffsetList.forEachIndexed { index, ints ->
            val signBean = signBeanList[index]
            data.add(
                SignBean(
                    signBean.name,
                    signBean.state,
                    ints[0] / mapWidth.toFloat(),
                    ints[1] / mapHeight.toFloat()
                )
            )
        }
        return data
    }


    /**
     * 计算坐标位置
     * @param signBean SignBean
     * @return LayoutParams
     */
    private fun getParams(signBean: SignBean, signOffset: IntArray): LayoutParams {
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(
            (signBean.x * mapWidth + mapLeft - signOffset[0]).toInt(),
            (signBean.y * mapHeight + mapTop - signOffset[1]).toInt(),
            0,
            0
        )
        return params
    }


    /**
     * 计算图像在ImageView的位移量
     * @param img ImageView
     * @param includeLayout Boolean
     * @return IntArray?
     */
    private fun getBitmapOffset(img: ImageView, includeLayout: Boolean): IntArray {
        val offset = IntArray(2)
        val values = FloatArray(9)
        val m: Matrix = img.imageMatrix
        m.getValues(values)
        offset[0] = values[2].toInt()
        offset[1] = values[5].toInt()

        if (includeLayout) {
            val lp = img.layoutParams as MarginLayoutParams
            offset[0] += img.paddingLeft + lp.leftMargin
            offset[1] += img.paddingTop + lp.topMargin
        }
        return offset
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return mDragger.shouldInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragger.processTouchEvent(event)
        return true
    }

}