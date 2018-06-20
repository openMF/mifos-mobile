package org.mifos.mobilebanking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by dilpreet on 12/8/17.
 */

@Parcelize

data class FAQ @JvmOverloads constructor(
        var question: String? = null,
        var answer: String? = null,
        var isSelected: Boolean = false
) : Parcelable