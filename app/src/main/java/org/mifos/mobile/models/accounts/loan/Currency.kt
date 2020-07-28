package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by dilpreet on 27/2/17.
 */

@Parcelize
data class Currency(
        var code: String,

        var name: String,

        var decimalPlaces: Double? = null,

        var inMultiplesOf: Double? = null,

        var displaySymbol: String,

        var nameCode: String,

        var displayLabel: String

) : Parcelable