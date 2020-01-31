package com.packetalk.utility

import android.R
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomButton(
    context: Context,
    attrs: AttributeSet?
) : AppCompatButton(context, attrs) {
    private var isEnable = true
    private var mRadius: Int
    private var mStrokeDashWidth: Int
    private var mStrokeDashGap: Int
    private var mStrokeColor = 0
    private var mStrokeWidth: Int
    private var mBackgroundColor: Int
    override fun setEnabled(enabled: Boolean) {
        isEnable = enabled
        notifyChanges()
        super.setEnabled(enabled)
    }

    private fun notifyChanges() {
        val factor = 0.8f
        val factorStorke = 0.9f
        if (!isEnable) { //handling for button disable state
            alpha = 0.6f
        }
        if (mStrokeColor == 0) {
            mStrokeColor = manipulateColor(mBackgroundColor, factorStorke)
        }
        val pressed: Drawable =
            getDrawable1(manipulateColor(mBackgroundColor, factor), mRadius.toFloat())
        val normal: Drawable =
            getDrawable1(mBackgroundColor, mRadius.toFloat())
        val states =
            StateListDrawable()
        states.addState(intArrayOf(R.attr.state_pressed), pressed)
        states.addState(intArrayOf(), normal)
        background = states
    }

    fun getDrawable1(
        backgroundColor: Int,
        radius: Float
    ): GradientDrawable {
        val colors = intArrayOf(
            backgroundColor,
            manipulateColor(backgroundColor, 0.8f)
        )
        val shape =
            GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                colors
            )
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = radius
        shape.gradientType = GradientDrawable.LINEAR_GRADIENT
        shape.setStroke(mStrokeWidth, mStrokeColor)
        if (mStrokeDashGap > 0 || mStrokeDashWidth > 0) {
            shape.setStroke(
                mStrokeWidth,
                mStrokeColor,
                mStrokeDashWidth.toFloat(),
                mStrokeDashGap.toFloat()
            )
        }
        return shape
    }

    private fun manipulateColor(color: Int, factor: Float): Int { //factor = 0.8f
        val a = Color.alpha(color)
        val r = Math.round(Color.red(color) * factor)
        val g = Math.round(Color.green(color) * factor)
        val b = Math.round(Color.blue(color) * factor)
        return Color.argb(
            a,
            Math.min(r, 255),
            Math.min(g, 255),
            Math.min(b, 255)
        )
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        mBackgroundColor = backgroundColor
        notifyChanges()
    }

    fun setCornerRadious(cornerRadious: Int) {
        mRadius = cornerRadious
        notifyChanges()
    }

    fun setStrokeDashGap(strokeDashGap: Int) {
        mStrokeDashGap = strokeDashGap
        notifyChanges()
    }

    fun setStrokeDashWidth(strokeDashWidth: Int) {
        mStrokeDashWidth = strokeDashWidth
        notifyChanges()
    }

    fun setStrokeColor(strokeColor: Int) {
        mStrokeColor = strokeColor
        notifyChanges()
    }

    fun setStrokeWidth(strokeWidth: Int) {
        mStrokeWidth = strokeWidth
        notifyChanges()
    }

    init {
        val a =
            context.obtainStyledAttributes(attrs, com.packetalk.R.styleable.CustomButton)
        mBackgroundColor = a.getColor(
            com.packetalk.R.styleable.CustomButton_qb_backgroundColor,
            resources.getColor(com.packetalk.R.color.colorPrimary)
        )
        mRadius = a.getInt(com.packetalk.R.styleable.CustomButton_qb_radius, 100)
        mStrokeDashGap = a.getInt(com.packetalk.R.styleable.CustomButton_qb_strokeDashGap, 0)
        mStrokeDashWidth =
            a.getInt(com.packetalk.R.styleable.CustomButton_qb_strokeDashWidth, 0)
        mStrokeWidth = a.getInt(com.packetalk.R.styleable.CustomButton_qb_strokeWidth, 0)
        mStrokeColor = a.getColor(
            com.packetalk.R.styleable.CustomButton_qb_strokeColor,
            mBackgroundColor
        )
        a.recycle()
        //        TypedArray a1 = getContext().obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
//        Drawable mForegroundDrawable = a1.getDrawable(0);
//        setForeground(mForegroundDrawable);
        notifyChanges()
    }
}