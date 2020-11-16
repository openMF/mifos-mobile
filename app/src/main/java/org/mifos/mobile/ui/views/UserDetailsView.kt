package org.mifos.mobile.ui.views

import android.graphics.Bitmap
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by naman on 07/04/17.
 */
interface UserDetailsView : MVPView {
    fun showUserDetails(client: Client?)
    fun showUserImage(bitmap: Bitmap?)
    fun showError(message: String?)
}