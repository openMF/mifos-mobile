/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.account.utils

import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount

sealed class AccountState {
    data object Error : AccountState()
    data object Loading : AccountState()
    data class ShowSavingsAccounts(val savingAccounts: List<SavingAccount>?) : AccountState()
    data class ShowLoanAccounts(val loanAccounts: List<LoanAccount>?) : AccountState()
    data class ShowShareAccounts(val shareAccounts: List<ShareAccount>?) : AccountState()
}
