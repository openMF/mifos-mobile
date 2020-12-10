package org.mifos.mobile.models.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Amount(
        var currency: String? = null,
        var amount: String? = null) : Parcelable