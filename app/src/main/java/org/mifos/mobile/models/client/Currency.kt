package org.mifos.mobile.models.client

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
        var code: String,

        var name: String,

        var decimalPlaces: Int? = null,

        var displaySymbol: String? = null,

        var nameCode: String,

        var displayLabel: String
) : Parcelable