package org.mifos.mobile.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * @author Vishwajeet
 * @since 10/08/16
 */
class DividerItemDecoration(context: Context?, orientation: Int) : ItemDecoration() {
    private var mDivider: Drawable? = null
    private var mOrientation = 0
    private fun setOrientation(orientation: Int) {
        require(!(orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)) { "invalid orientation" }
        mOrientation = orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + if (mDivider != null) mDivider!!.intrinsicHeight else 0
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + if (mDivider != null) mDivider!!.intrinsicHeight else 0
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }

    override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
    ) {
        if (mOrientation == VERTICAL_LIST) {
            outRect[0, 0, 0] = if (mDivider != null) mDivider!!.intrinsicHeight else 0
        } else {
            outRect[0, 0, if (mDivider != null) mDivider!!.intrinsicWidth else 0] = 0
        }
    }

    companion object {
        const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    init {
        val a = context?.obtainStyledAttributes(ATTRS)
        if (a != null) {
            mDivider = a.getDrawable(0)
        }
        a?.recycle()
        setOrientation(orientation)
    }
}