package com.techventure.androidext.ext

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Build
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.animation.Interpolator
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat

fun TextView.setTextSize(dimension: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension)
}

fun TextView.setTextViewAppearance(resId: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        setTextAppearance(context, resId)
    } else {
        setTextAppearance(resId)
    }
}

fun TextView.setTextColor(colorId: Int) =
    setTextColor(ResourcesCompat.getColor(context.resources, colorId, null))

fun TextView.setTextColor(colorTo: Int, durationMillis: Long, interpolator: Interpolator) {
    if (durationMillis <= 0) {
        setTextColor(colorTo)
        return
    }
    val colorFrom = textColors.defaultColor
    val animation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    animation.duration = durationMillis
    animation.interpolator = interpolator
    animation.addUpdateListener { animator -> setTextColor(animator.animatedValue as Int) }
    animation.start()
}


fun TextView.absoluteGravity() = Gravity.getAbsoluteGravity(gravity, layoutDirection)

fun TextView.horizontalGravity(): Int = absoluteGravity() and Gravity.HORIZONTAL_GRAVITY_MASK

fun TextView.verticalGravity(): Int = absoluteGravity() and Gravity.VERTICAL_GRAVITY_MASK

fun TextView.isGravityRight(): Boolean = horizontalGravity() == Gravity.RIGHT

fun TextView.isGravityCenter(): Boolean = horizontalGravity() == Gravity.CENTER_HORIZONTAL

fun TextView.startOffset(): Int =
    compoundDrawables[0]?.let { it.intrinsicWidth + compoundDrawablePadding } ?: 0

fun TextView.endOffset(): Int =
    compoundDrawables[2]?.let { it.intrinsicWidth + compoundDrawablePadding } ?: 0

fun AppCompatTextView.makeScrollableInsideScrollView() {
    movementMethod = ScrollingMovementMethod()
    setOnTouchListener { view, event ->
        view.apply {
            parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> {
                    parent.requestDisallowInterceptTouchEvent(false)
                    performClick()
                }
            }
        }
        false
    }
}

fun TextView.setCustomTextColor(colorId: Int) =
    setTextColor(ResourcesCompat.getColor(context.resources, colorId, null))