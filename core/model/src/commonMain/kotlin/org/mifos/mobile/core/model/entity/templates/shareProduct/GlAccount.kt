package org.mifos.mobile.core.model.entity.templates.shareProduct

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class GlAccount(
    val id: Int? = null,
    val name: String? = null,
    val glCode: String? = null
) : Parcelable
