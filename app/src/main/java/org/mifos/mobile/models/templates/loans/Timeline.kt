package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class Timeline(
        var expectedDisbursementDate: List<Int>
) : Parcelable