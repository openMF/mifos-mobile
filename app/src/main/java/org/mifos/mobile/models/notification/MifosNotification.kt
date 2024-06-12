package org.mifos.mobile.models.notification

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import org.mifos.mobile.api.local.SelfServiceDatabase

/**
 * Created by dilpreet on 13/9/17.
 */
@Table(database = SelfServiceDatabase::class)
class MifosNotification : BaseModel() {

    @JvmField
    @PrimaryKey
    var timeStamp: Long = 0

    @JvmField
    @Column
    var msg: String? = null

    @JvmField
    @Column
    var read: Boolean? = null
    fun getTimeStamp(): Long {
        return timeStamp
    }

    fun setTimeStamp(timeStamp: Long) {
        this.timeStamp = timeStamp
    }

    fun isRead(): Boolean {
        return read ?: false
    }

    fun setRead(read: Boolean?) {
        this.read = read
    }
}
