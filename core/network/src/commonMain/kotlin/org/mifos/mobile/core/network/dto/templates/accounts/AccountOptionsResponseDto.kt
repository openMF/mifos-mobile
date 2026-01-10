package org.mifos.mobile.core.network.dto.templates.accounts

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.common.TypeResponseDto


@Serializable
data class AccountOptionsResponseDto(
    val accountId: Int? = null,

    val accountNo: String? = null,

    val accountType: TypeResponseDto? = null,

    val clientId: Long? = null,

    val clientName: String? = null,

    val officeId: Int? = null,

    val officeName: String? = null,

)