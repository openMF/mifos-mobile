package org.mifos.mobilebanking.utils;

import org.mifos.mobilebanking.models.notification.MifosNotification;

import java.util.Comparator;

/**
 * Created by dilpreet on 14/9/17.
 */

public class NotificationComparator implements Comparator<MifosNotification> {
    @Override
    public int compare(MifosNotification mifosNotification1, MifosNotification mifosNotification2) {
        return mifosNotification2.getTimeStamp() < mifosNotification1.getTimeStamp() ? -1 :
                mifosNotification1.getTimeStamp() > mifosNotification2.getTimeStamp() ? 1 : 0;
    }
}
