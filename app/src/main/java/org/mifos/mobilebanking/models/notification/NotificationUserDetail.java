package org.mifos.mobilebanking.models.notification;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 17/12/17.
 */

public class NotificationUserDetail {

    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
