@file:Suppress("NOTHING_TO_INLINE")

package com.github.technoir42.android.extensions

import android.content.Context
import android.graphics.Paint
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

inline fun Int.dpToPx(displayMetrics: DisplayMetrics): Int {
    return toFloat().dpToPx(displayMetrics)
}

inline fun Float.dpToPx(displayMetrics: DisplayMetrics): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics).roundToInt()
}

inline fun <reified T : Any> Context.requireSystemService(): T {
    return checkNotNull(ContextCompat.getSystemService(this, T::class.java)) {
        "Required system service ${T::class.java.simpleName} is null"
    }
}

inline val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

inline fun <reified T : Parcelable> getParcelableCreator(): Parcelable.Creator<T> {
    val field = T::class.java.getDeclaredField("CREATOR")
    @Suppress("UNCHECKED_CAST")
    return field.get(null) as Parcelable.Creator<T>
}

var TextView.isStrikethrough: Boolean
    get() = (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) == Paint.STRIKE_THRU_TEXT_FLAG
    set(value) {
        paintFlags = if (value) {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

fun View.hideSoftwareKeyboard() {
    val inputMethodManager = context.requireSystemService<InputMethodManager>()
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

inline fun <reified T : ViewParent> View.findParentOfType(): T? {
    return findParentOfType(T::class.java)
}

fun <T : ViewParent> View.findParentOfType(type: Class<T>): T? {
    var p = parent
    while (p != null) {
        if (type.isInstance(p)) {
            return type.cast(p)
        }
        p = p.parent
    }
    return null
}

inline val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)

inline fun ViewGroup.inflate(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}
