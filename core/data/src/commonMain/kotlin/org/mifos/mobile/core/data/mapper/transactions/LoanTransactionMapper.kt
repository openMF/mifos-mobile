/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.transactions

import org.mifos.mobile.core.model.entity.TransactionBalances
import org.mifos.mobile.core.model.entity.TransactionDetails
import org.mifos.mobile.core.network.dto.transaction.LoanTransactionDetailsResponseDto

fun LoanTransactionDetailsResponseDto.toModel(): TransactionDetails {
    val type = this.loanType

    val isCreditResolved = when {
        type?.disbursement == true -> false
        type?.repayment == true -> false
        type?.value?.contains("Transfer", ignoreCase = true) == true -> false
        else -> true
    }

    return TransactionDetails(
        id = this.id ?: -1L,
        transactionName = type?.value ?: "Loan Transaction",
        isCredit = isCreditResolved,
        amount = this.amount ?: 0.0,
        currencyCode = this.currency?.code ?: "USD",
        date = this.date ?: emptyList(),
        accountNo = "N/A",
        isReversed = this.manuallyReversed == true,
        transferDescription = this.transfer?.transferDescription,
        balances = TransactionBalances(
            running = this.outstandingLoanBalance,
            principal = this.principalPortion,
            interest = this.interestPortion,
            fee = this.feeChargesPortion,
            penalty = this.penaltyChargesPortion,
        ),
    )
}
