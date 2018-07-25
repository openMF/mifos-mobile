package org.mifos.mobilebanking.models.guarantor

/*
 * Created by saksham on 24/July/2018
 */

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GuarantorPayload(

        var id: Long = 0,

        var officeName: String? = null,

        @SerializedName("lastname")
        var lastname: String? = null,

        var guarantorType: @RawValue GuarantorType? = null,

        @SerializedName("firstname")
        var firstname: String? = null,

        var joinedDate: List<Int>? = null,

        var loanId: Long? = null
) : Parcelable