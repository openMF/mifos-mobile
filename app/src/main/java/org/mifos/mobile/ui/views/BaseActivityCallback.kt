package org.mifos.mobile.ui.views

/**
 * @author Rajan Maurya
 */
interface BaseActivityCallback {

    fun showProgressDialog(message: String?)

    fun hideProgressDialog()

    fun setToolbarTitle(title: String?)
}