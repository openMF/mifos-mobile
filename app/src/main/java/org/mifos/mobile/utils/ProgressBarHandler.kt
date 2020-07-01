package org.mifos.mobile.utils

import android.R.attr
import android.R.id
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout

/**
 * @author Rajan Maurya
 */
class ProgressBarHandler(private val mContext: Context) {
    private val mProgressBar: ProgressBar
    fun show() {
        mProgressBar.visibility = View.VISIBLE
    }

    fun hide() {
        mProgressBar.visibility = View.INVISIBLE
    }

    init {
        val layout = (mContext as Activity).findViewById<View>(id.content).rootView as ViewGroup
        mProgressBar = ProgressBar(mContext, null, attr.progressBarStyleInverse)
        mProgressBar.isIndeterminate = true
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        val rl = RelativeLayout(mContext)
        rl.gravity = Gravity.CENTER
        rl.addView(mProgressBar)
        layout.addView(rl, params)
        hide()
    }
}