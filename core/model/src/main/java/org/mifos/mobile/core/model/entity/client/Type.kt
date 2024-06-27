package org.mifos.mobile.core.model.entity.client

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 23/02/17.
 */

@Parcelize
data class Type(
    var id: Int? = null,
    var code: String? = null,

    var value: String? = null,
) : Parcelable
