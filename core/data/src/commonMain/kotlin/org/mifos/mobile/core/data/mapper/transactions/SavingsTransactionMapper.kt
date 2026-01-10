package org.mifos.mobile.core.data.mapper.transactions

import org.mifos.mobile.core.model.entity.TransactionBalances
import org.mifos.mobile.core.model.entity.TransactionDetails
import org.mifos.mobile.core.network.dto.transaction.SavingsTransactionDetailsResponseDto

fun SavingsTransactionDetailsResponseDto.toModel(): TransactionDetails {
    val type = this.savingsType

    val code = type?.code?.lowercase().orEmpty()
    val value = type?.value?.lowercase().orEmpty()

    val isCreditResolved = when {
        type?.withdrawal == true -> false
        type?.feeDeduction == true -> false
        type?.initiateTransfer == true -> false
        type?.approveTransfer == true -> false
        type?.withdrawTransfer == true -> false

        type?.deposit == true -> true
        type?.rejectTransfer == true -> true

        (code.contains("transfer") || value.contains("transfer")) &&
                !value.contains("incoming") &&
                !value.contains("reject") -> false

        else -> true
    }

    return TransactionDetails(
        id = this.id ?: -1L,
        transactionName = type?.value ?: "Savings Transaction",
        isCredit = isCreditResolved,
        amount = this.amount ?: 0.0,
        currencyCode = this.currency?.code ?: "USD",
        date = this.date ?: emptyList(),
        accountNo = this.accountNo ?: "N/A",
        isReversed = this.reversed == true,
        balances = TransactionBalances(
            running = this.runningBalance,
        ),
    )
}