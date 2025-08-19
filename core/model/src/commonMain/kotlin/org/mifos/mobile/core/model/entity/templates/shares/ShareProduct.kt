package org.mifos.mobile.core.model.entity.templates.shares

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class ShareProduct(
    val pageItems: List<SharePageItem>? = null,
    val totalFilteredRecords: Int? = null
) : Parcelable