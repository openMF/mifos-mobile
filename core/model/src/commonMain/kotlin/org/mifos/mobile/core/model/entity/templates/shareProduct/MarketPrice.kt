package org.mifos.mobile.core.model.entity.templates.shareProduct

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class MarketPrice(
    val id: Int? = null,
    val fromDate: String? = null,
    val shareValue: Int? = null
) : Parcelable