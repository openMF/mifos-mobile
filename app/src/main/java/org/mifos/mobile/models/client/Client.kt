package org.mifos.mobile.models.client

import android.os.Parcel
import android.os.Parcelable
import org.mifos.mobile.models.Timeline
import java.util.*

/**
 * @author Vishwajeet
 * @since 20/06/16
 */
class Client : Parcelable {
    var id = 0

    var accountNo: String? = null

    private var status: Status? = null

    private var active: Boolean? = null

    var activationDate: List<Int?>? = ArrayList()

    var dobDate: List<Int?> = ArrayList()

    var firstname: String? = null

    var middlename: String? = null

    var lastname: String? = null

    var displayName: String? = null

    var fullname: String? = null

    private var officeId: Int? = null

    var officeName: String? = null

    private var staffId: Int? = null

    private var staffName: String? = null

    private var timeline: Timeline? = null

    var imageId = 0

    var isImagePresent = false

    private var externalId: String? = null

    var mobileNo: String? = null

    var clientClassification: ClientClassification? = null

    var clientType: ClientType? = null

    var gender: Gender? = null

    var groups: List<Group> = ArrayList()
    override fun toString(): String {
        return "Client{" +
                "id=" + id +
                ", accountNo='" + accountNo + '\'' +
                ", activationDate=" + activationDate +
                ", firstname='" + firstname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", lastname='" + lastname + '\'' +
                ", displayName='" + displayName + '\'' +
                ", fullname='" + fullname + '\'' +
                '}'
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(accountNo)
        dest.writeParcelable(status, flags)
        dest.writeValue(active)
        dest.writeList(activationDate)
        dest.writeList(dobDate)
        dest.writeString(firstname)
        dest.writeString(middlename)
        dest.writeString(lastname)
        dest.writeString(displayName)
        dest.writeString(fullname)
        dest.writeValue(officeId)
        dest.writeString(officeName)
        dest.writeValue(staffId)
        dest.writeString(staffName)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        accountNo = `in`.readString()
        status = `in`.readParcelable(Status::class.java.classLoader)
        active = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        activationDate = ArrayList()
        `in`.readList(activationDate, Int::class.java.classLoader)
        dobDate = ArrayList()
        `in`.readList(dobDate, Int::class.java.classLoader)
        firstname = `in`.readString()
        middlename = `in`.readString()
        lastname = `in`.readString()
        displayName = `in`.readString()
        fullname = `in`.readString()
        officeId = `in`.readValue(Int::class.java.classLoader) as Int
        officeName = `in`.readString()
        staffId = `in`.readValue(Int::class.java.classLoader) as Int
        staffName = `in`.readString()
        timeline = `in`.readParcelable(Timeline::class.java.classLoader)
        fullname = `in`.readString()
        imageId = `in`.readInt()
        isImagePresent = `in`.readByte().toInt() != 0
        externalId = `in`.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<Client?> = object : Parcelable.Creator<Client?> {
            override fun createFromParcel(source: Parcel): Client? {
                return Client(source)
            }

            override fun newArray(size: Int): Array<Client?> {
                return arrayOfNulls(size)
            }
        }
    }
}