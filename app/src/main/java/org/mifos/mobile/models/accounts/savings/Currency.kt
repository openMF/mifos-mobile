package org.mifos.mobile.models.accounts.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
        var code: String? = null,

        var name: String? = null,

        var decimalPlaces: Int? = null,

        var inMultiplesOf: Int? = null,

        var displaySymbol: String? = null,

        var nameCode: String? = null,

        var displayLabel: String? = null
) : Parcelable