package org.mifos.mobile.core.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by dilpreet on 12/8/17.
 */

@Parcelize

data class FAQ @JvmOverloads constructor(
    var question: String? = null,
    var answer: String? = null,
    var isSelected: Boolean = false,
) : Parcelable
