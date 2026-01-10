package org.mifos.mobile.core.network.dto.accounts

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.loanAccount.LoanAccountResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsAccountResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareAccountResponseDto


@Serializable
data class AccountsResponseDto(
    val loanAccounts: List<LoanAccountResponseDto> = emptyList(),
    val savingsAccounts: List<SavingsAccountResponseDto>? = emptyList(),
    val shareAccounts: List<ShareAccountResponseDto> = emptyList(),
)