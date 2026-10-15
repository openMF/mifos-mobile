/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.pocket

import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.model.entity.pocket.PocketAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.network.dto.pocket.PocketAccountDto
import org.mifos.mobile.core.network.dto.pocket.PocketResponseDto
import org.mifos.mobile.core.model.entity.accounts.loan.Status as LoanStatus
import org.mifos.mobile.core.model.entity.accounts.savings.Status as SavingsStatus
import org.mifos.mobile.core.model.entity.accounts.share.Status as ShareStatus

fun PocketResponseDto.toDomainList(): List<PocketAccount> {
    val all = mutableListOf<PocketAccount>()

    loanAccounts.forEach { all.add(it.toDomain(AccountType.LOAN)) }
    savingsAccounts.forEach { all.add(it.toDomain(AccountType.SAVINGS)) }
    shareAccounts.forEach { all.add(it.toDomain(AccountType.SHARE)) }

    return all
}

private fun PocketAccountDto.toDomain(type: AccountType) = PocketAccount(
    id = this.id,
    pocketId = this.pocketId,
    accountId = this.accountId,
    accountType = type,
    accountNumber = this.accountNumber,
)

fun LoanStatus.toAccountStatus(): AccountStatus =
    when {
        active == true -> AccountStatus.ACTIVE
        pendingApproval == true -> AccountStatus.PENDING
        waitingForDisbursal == true -> AccountStatus.APPROVED
        overpaid == true -> AccountStatus.OVERPAID
        closed == true ||
            closedObligationsMet == true ||
            closedWrittenOff == true ||
            closedRescheduled == true -> AccountStatus.CLOSED
        else -> AccountStatus.UNKNOWN
    }

fun SavingsStatus.toAccountStatus(): AccountStatus =
    when {
        active == true -> AccountStatus.ACTIVE
        submittedAndPendingApproval == true -> AccountStatus.PENDING
        approved == true -> AccountStatus.APPROVED
        rejected == true -> AccountStatus.REJECTED
        withdrawnByApplicant == true -> AccountStatus.WITHDRAWN
        matured == true -> AccountStatus.MATURED
        closed == true || prematureClosed == true -> AccountStatus.CLOSED
        else -> AccountStatus.UNKNOWN
    }

fun ShareStatus.toAccountStatus(): AccountStatus =
    when {
        active == true -> AccountStatus.ACTIVE
        submittedAndPendingApproval == true -> AccountStatus.PENDING
        approved == true -> AccountStatus.APPROVED
        rejected == true -> AccountStatus.REJECTED
        closed == true -> AccountStatus.CLOSED
        else -> AccountStatus.UNKNOWN
    }
