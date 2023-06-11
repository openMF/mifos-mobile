package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoanType(
    var id: Int? = null,

    var code: String? = null,

    var value: String? = null,
) : Parcelable
