package com.techventure.androidext.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat

fun Context.getFloatResources(id: Int): Float {
    val typedValue = TypedValue()
    resources.getValue(id, typedValue, true)
    return typedValue.float
}

fun Context.getCustomDrawable(id: Int?): Drawable? {
    return id?.let { ContextCompat.getDrawable(this, it) }
}