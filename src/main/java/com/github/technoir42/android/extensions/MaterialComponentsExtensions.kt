package com.github.technoir42.android.extensions

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.doOnLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import java.lang.reflect.Method

fun AppBarLayout.disableDragging() {
    val layoutParams = layoutParams as CoordinatorLayout.LayoutParams
    val behavior = layoutParams.behavior as AppBarLayout.Behavior?
    if (behavior != null) {
        behavior.setDragCallback(DisabledDragCallback)
    } else {
        doOnLayout {
            (layoutParams.behavior as AppBarLayout.Behavior).setDragCallback(DisabledDragCallback)
        }
    }
}

fun AppBarLayout.invalidateScrollRanges() {
    invalidateScrollRangesMethod(this)
}

inline fun TabLayout.doOnTabReselected(crossinline action: (TabLayout.Tab) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            action(tab)
        }
    })
}

private object DisabledDragCallback : AppBarLayout.Behavior.DragCallback() {
    override fun canDrag(appBarLayout: AppBarLayout): Boolean = false
}

private val invalidateScrollRangesMethod: Method by lazy(LazyThreadSafetyMode.NONE) {
    AppBarLayout::class.java.getDeclaredMethod("invalidateScrollRanges").apply { isAccessible = true }
}
