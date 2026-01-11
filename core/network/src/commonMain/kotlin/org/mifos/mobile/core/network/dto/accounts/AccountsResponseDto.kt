/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
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
