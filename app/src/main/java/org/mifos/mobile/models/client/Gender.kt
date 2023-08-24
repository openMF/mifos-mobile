package org.mifos.mobile.models.client

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by dilpreet on 10/7/17.
 */
@Parcelize
data class Gender(
    var id: Int,
    var name: String? = null,
    var active: Boolean,
    var mandatory: Boolean,
) : Parcelable
