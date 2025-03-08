package org.mifos.mobile.feature.savings.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.savings.savingsMakeTransfer.SavingsMakeTransferViewModel
import org.mifos.mobile.feature.savings.savingsAccount.SavingAccountsDetailViewModel
import org.mifos.mobile.feature.savings.savingsAccountApplication.SavingsAccountApplicationViewModel
import org.mifos.mobile.feature.savings.savingsAccountTransaction.SavingAccountsTransactionViewModel
import org.mifos.mobile.feature.savings.savingsAccountWithdraw.SavingsAccountWithdrawViewModel

val SavingsModule = module {

    viewModelOf(::SavingsMakeTransferViewModel)
    viewModelOf(::SavingAccountsDetailViewModel)
    viewModelOf(::SavingsAccountApplicationViewModel)
    viewModelOf(::SavingAccountsTransactionViewModel)
    viewModelOf(::SavingsAccountWithdrawViewModel)
}
