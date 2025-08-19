package org.mifos.mobile.core.model.entity.templates.shareProduct

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class LockPeriodTypeEnum(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null
) : Parcelable
