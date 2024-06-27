package org.mifos.mobile.core.model.entity.accounts.loan

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by dilpreet on 27/2/17.
 */

@Parcelize
data class Currency(
    var code: String? = null,

    var name: String? = null,

    var decimalPlaces: Double? = null,

    var inMultiplesOf: Double? = null,

    var displaySymbol: String? = null,

    var nameCode: String? = null,

    var displayLabel: String? = null,
) : Parcelable
