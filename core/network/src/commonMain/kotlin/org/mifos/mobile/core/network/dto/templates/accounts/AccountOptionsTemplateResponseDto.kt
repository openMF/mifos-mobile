package org.mifos.mobile.core.network.dto.templates.accounts

import kotlinx.serialization.Serializable

@Serializable
data class AccountOptionsTemplateResponseDto(

    val fromAccountOptions: List<AccountOptionsResponseDto> = emptyList(),

    val toAccountOptions: List<AccountOptionsResponseDto> = emptyList(),

)
