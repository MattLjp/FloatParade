package com.itc.floatparade

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * @ Author : 廖健鹏
 * @ Date : 2021/7/1
 * @ e-mail : 329524627@qq.com
 * @ Description :
 */
class SignView : ConstraintLayout {
    private val TAG = SignView::class.java.simpleName

    private var view: View
    private var signIv: ImageView
    private var signNameTv: TextView
    private var signStateTv: TextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        view = LayoutInflater.from(context).inflate(R.layout.sign_view, this, true)
        signIv = view.findViewById(R.id.iv_sign)
        signNameTv = view.findViewById(R.id.tv_sign_name)
        signStateTv = view.findViewById(R.id.tv_sign_state)
    }

    /**
     * 设置坐标信息
     * @param signBean SignBean
     */
    fun setData(signBean: SignBean) {
        signNameTv.text = signBean.name
        signStateTv.text = signBean.state
    }


    /**
     * 计算坐标图标在整个视图的偏移量
     * @return IntArray
     */
    fun getSignOffset(): IntArray {
        val w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        signIv.measure(w, h)

        val offset = IntArray(2)
        val signImageWidth = signIv.measuredWidth
        val signImageHeight = signIv.measuredHeight
        offset[0] = signImageWidth / 2
        offset[1] = 20 + signImageHeight - offset[0]
        Log.d(TAG, "getSignOffset: x:${offset[0]}, y:${offset[1]}")
        return offset
    }
}