package org.mifos.mobile.ui.views.base

import android.view.View

/**
 * Created by Rajan Maurya on 23/02/17.
 */
interface OnClickItem {
    fun onItemClick(childView: View?, position: Int)
    fun onItemLongPress(childView: View?, position: Int)
}