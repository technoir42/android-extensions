@file:Suppress("NOTHING_TO_INLINE")

package com.github.technoir42.android.extensions

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import java.lang.reflect.Field

fun AppCompatActivity.setActionBar(@IdRes id: Int, init: ActionBar.() -> Unit = {}): Toolbar {
    val toolbar = ActivityCompat.requireViewById<Toolbar>(this, id)
    setSupportActionBar(toolbar)
    supportActionBar!!.init()
    return toolbar
}

inline fun <reified T : Any> Fragment.getCallback(): T? {
    return (parentFragment ?: requireActivity()) as? T
}

fun DialogFragment.showAllowingStateLoss(fragmentManager: FragmentManager, tag: String) {
    fragmentManager.beginTransaction()
        .add(this, tag)
        .commitAllowingStateLoss()
}

inline fun <reified T : Fragment> FragmentFactory.instantiate(context: Context, args: Bundle? = null): T {
    return instantiate(context, T::class.java, args)
}

inline fun <T : Fragment> FragmentFactory.instantiate(context: Context, fragmentClass: Class<T>, args: Bundle? = null): T {
    @Suppress("UNCHECKED_CAST")
    return instantiate(context.classLoader, fragmentClass.name).apply { arguments = args } as T
}

fun Toolbar.setTitleOnClickListener(onClickListener: View.OnClickListener) {
    var titleView = toolbarTitleField.get(this) as View?
    if (titleView == null) {
        val title = title
        this.title = " " // Force Toolbar to create mTitleTextView
        this.title = title
        titleView = toolbarTitleField.get(this) as View
    }
    titleView.setOnClickListener(onClickListener)
}

fun Toolbar.setSubtitleOnClickListener(onClickListener: View.OnClickListener) {
    var subtitleView = toolbarSubtitleField.get(this) as View?
    if (subtitleView == null) {
        val subtitle = subtitle
        this.subtitle = " " // Force Toolbar to create mSubtitleTextView
        this.subtitle = subtitle
        subtitleView = toolbarSubtitleField.get(this) as View
    }
    subtitleView.setOnClickListener(onClickListener)
}

inline fun <reified T : CoordinatorLayout.Behavior<*>> View.getLayoutBehavior(): T {
    val layoutParams = layoutParams as CoordinatorLayout.LayoutParams
    return layoutParams.behavior as T
}

private val toolbarTitleField: Field by lazy(LazyThreadSafetyMode.NONE) {
    Toolbar::class.java.getDeclaredField("mTitleTextView").apply { isAccessible = true }
}

private val toolbarSubtitleField: Field by lazy(LazyThreadSafetyMode.NONE) {
    Toolbar::class.java.getDeclaredField("mSubtitleTextView").apply { isAccessible = true }
}
