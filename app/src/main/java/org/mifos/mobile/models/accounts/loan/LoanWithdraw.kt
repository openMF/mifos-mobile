package org.mifos.mobile.models.accounts.loan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by dilpreet on 7/6/17.
 */

@Parcelize
data class LoanWithdraw(
        var withdrawnOnDate: String? = null,

        var note: String? = null,

        internal var dateFormat : String = "dd MMMM yyyy",
        internal var locale : String = "en"
) : Parcelable