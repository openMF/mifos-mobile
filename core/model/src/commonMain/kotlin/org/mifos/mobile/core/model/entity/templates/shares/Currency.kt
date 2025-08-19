package org.mifos.mobile.core.model.entity.templates.shares

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class Currency(
    val code: String? = null,
    val decimalPlaces: Int? = null,
    val displayLabel: String? = null,
    val displaySymbol: String? = null,
    val inMultiplesOf: Int? = null,
    val name: String? = null,
    val nameCode: String? = null
) : Parcelable