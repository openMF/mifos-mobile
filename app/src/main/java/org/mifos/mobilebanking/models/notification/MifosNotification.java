package org.mifos.mobilebanking.models.notification;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.mifos.mobilebanking.api.local.SelfServiceDatabase;

/**
 * Created by dilpreet on 13/9/17.
 */
@Table(database = SelfServiceDatabase.class)
public class MifosNotification extends BaseModel {

    @PrimaryKey
    Long timeStamp;

    @Column
    String msg;

    @Column
    Boolean read;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
