package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class Currency(

        var code: String,

        var name: String,

        var decimalPlaces: Double? = null,

        var inMultiplesOf: Int? = null,

        var displaySymbol: String,

        var nameCode: String,

        var displayLabel: String
) : Parcelable