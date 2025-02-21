/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.loan.loanAccount.LoanAccountsDetailViewModel
import org.mifos.mobile.feature.loan.loanAccountApplication.LoanApplicationViewModel
import org.mifos.mobile.feature.loan.loanAccountSummary.LoanAccountSummaryViewModel
import org.mifos.mobile.feature.loan.loanAccountTransaction.LoanAccountTransactionViewModel
import org.mifos.mobile.feature.loan.loanAccountWithdraw.LoanAccountWithdrawViewModel
import org.mifos.mobile.feature.loan.loanRepaymentSchedule.LoanRepaymentScheduleViewModel
import org.mifos.mobile.feature.loan.loanReview.ReviewLoanApplicationViewModel

val LoanModule = module {

    viewModelOf(::LoanAccountsDetailViewModel)
    viewModelOf(::LoanApplicationViewModel)
    viewModelOf(::LoanAccountSummaryViewModel)
    viewModelOf(::LoanAccountTransactionViewModel)
    viewModelOf(::LoanAccountWithdrawViewModel)
    viewModelOf(::LoanRepaymentScheduleViewModel)
    viewModelOf(::ReviewLoanApplicationViewModel)
}
