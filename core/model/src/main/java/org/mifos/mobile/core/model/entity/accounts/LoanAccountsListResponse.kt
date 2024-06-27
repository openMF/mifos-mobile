package org.mifos.mobile.core.model.entity.accounts

import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount

/**
 * @author Vishwajeet
 * @since 13/08/16
 */
data class LoanAccountsListResponse(
    var loanAccounts: List<LoanAccount> = ArrayList(),
)
