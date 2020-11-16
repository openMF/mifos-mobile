package org.mifos.mobile.ui.views

import android.graphics.Bitmap
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 19/6/17.
 */
interface HomeOldView : MVPView {
    fun showUserInterface()
    fun showLoanAccountDetails(totalLoanAmount: Double)
    fun showSavingAccountDetails(totalSavingAmount: Double)
    fun showUserDetails(client: Client?)
    fun showUserImage(bitmap: Bitmap?)
    fun showNotificationCount(count: Int)
    fun showError(errorMessage: String?)
}