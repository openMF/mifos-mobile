package org.mifos.mobilebanking.models.templates.account

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class AccountOption(
        @SerializedName("accountId")
        var accountId: Int? = null,

        @SerializedName("accountNo")
        var accountNo: String? = null,

        @SerializedName("accountType")
        var accountType: AccountType? = null,

        @SerializedName("clientId")
        var clientId: Long? = null,

        @SerializedName("clientName")
        var clientName: String? = null,

        @SerializedName("officeId")
        var officeId: Int? = null,

        @SerializedName("officeName")
        var officeName: String? = null

) : Parcelable