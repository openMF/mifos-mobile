package org.mifos.mobile.core.model.entity.accounts.loan.calendardata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class Frequency(
    var id: Int? = null,

    var code: String? = null,

    var value: String? = null,
) : Parcelable
