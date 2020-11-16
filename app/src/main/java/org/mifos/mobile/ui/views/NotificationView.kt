package org.mifos.mobile.ui.views

import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 14/9/17.
 */
interface NotificationView : MVPView {
    fun showNotifications(notifications: List<MifosNotification?>?)
    fun showError(msg: String?)
}