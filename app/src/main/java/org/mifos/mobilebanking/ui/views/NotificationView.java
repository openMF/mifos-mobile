package org.mifos.mobilebanking.ui.views;

import org.mifos.mobilebanking.models.notification.MifosNotification;
import org.mifos.mobilebanking.ui.views.base.MVPView;

import java.util.List;

/**
 * Created by dilpreet on 14/9/17.
 */

public interface NotificationView extends MVPView {

    void showNotifications(List<MifosNotification> notifications);

    void showError(String msg);
}
