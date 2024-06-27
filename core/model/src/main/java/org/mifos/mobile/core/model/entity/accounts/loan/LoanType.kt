package org.mifos.mobile.core.model.entity.accounts.loan

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoanType(
    var id: Int? = null,

    var code: String? = null,

    var value: String? = null,
) : Parcelable
