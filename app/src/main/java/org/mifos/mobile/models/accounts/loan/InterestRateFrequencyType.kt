package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class InterestRateFrequencyType(
    var id: Int? = null,

    var code: String? = null,

    var value: String? = null,
) : Parcelable
