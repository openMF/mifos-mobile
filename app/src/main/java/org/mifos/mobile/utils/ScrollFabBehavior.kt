package org.mifos.mobile.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton.OnVisibilityChangedListener

/**
 * Created by dilpreet on 23/8/17.
 */
class ScrollFabBehavior(context: Context?, attrs: AttributeSet?) : FloatingActionButton.Behavior() {
    override fun onStartNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: FloatingActionButton, directTargetChild: View,
            target: View, nestedScrollAxes: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes)
    }

    override fun onNestedScroll(
            coordinatorLayout: CoordinatorLayout, child: FloatingActionButton,
            target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
            dyUnconsumed: Int
    ) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed)
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            // Reason to set fab visiblity as INVISIBLE :
            // https://stackoverflow.com/a/41386278/4672688
            child.hide(object : OnVisibilityChangedListener() {
                override fun onHidden(fab: FloatingActionButton) {
                    super.onHidden(fab)
                    fab.visibility = View.INVISIBLE
                }
            })
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
        }
    }
}