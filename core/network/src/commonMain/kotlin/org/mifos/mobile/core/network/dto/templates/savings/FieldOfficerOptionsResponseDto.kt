package org.mifos.mobile.core.network.dto.templates.savings

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
data class FieldOfficerOptionsResponseDto(
    val id: Int,
    val firstname: String? = null,
    val lastname: String? = null,
    val displayName: String? = null,
    val officeId: Int? = null,
    val officeName: String? = null,
    val isLoanOfficer: Boolean? = null,
    val isActive: Boolean? = null,
)
