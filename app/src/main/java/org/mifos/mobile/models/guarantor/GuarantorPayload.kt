package org.mifos.mobile.models.guarantor

/*
 * Created by saksham on 24/July/2018
 */

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GuarantorPayload(

    var id: Long? = 0,

    var city: String? = null,

    var lastname: String? = null,

    var guarantorType: @RawValue GuarantorType? = null,

    var firstname: String? = null,

    var joinedDate: List<Int>? = null,

    var loanId: Long? = null,

    var status: Boolean? = true
) : Parcelable
