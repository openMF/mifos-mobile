package org.mifos.mobile.core.model.entity.templates.shareProduct

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize


@Serializable
@Parcelize
data class Currency(
    val code: String? = null,
    val name: String? = null,
    val decimalPlaces: Int? = null,
    val inMultiplesOf: Int? = null,
    val displaySymbol: String? = null,
    val nameCode: String? = null,
    val displayLabel: String? = null
) : Parcelable
