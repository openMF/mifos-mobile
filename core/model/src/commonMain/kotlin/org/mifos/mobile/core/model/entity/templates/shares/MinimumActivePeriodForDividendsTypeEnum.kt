package org.mifos.mobile.core.model.entity.templates.shares

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class MinimumActivePeriodForDividendsTypeEnum(
    val code: String? = null,
    val id: Int? = null,
    val value: String? = null
) : Parcelable
