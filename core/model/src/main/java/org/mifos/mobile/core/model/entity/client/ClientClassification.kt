package org.mifos.mobile.core.model.entity.client

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by dilpreet on 10/7/17.
 */
@Parcelize
data class ClientClassification(
    var id: Int,
    var name: String? = null,
    var active: Boolean,
    var mandatory: Boolean,
) : Parcelable
