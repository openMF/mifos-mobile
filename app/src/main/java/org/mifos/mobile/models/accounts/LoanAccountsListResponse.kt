package org.mifos.mobile.models.accounts

import org.mifos.mobile.models.accounts.loan.LoanAccount

import java.util.ArrayList

/**
 * @author Vishwajeet
 * @since 13/08/16
 */
data class LoanAccountsListResponse (
        var loanAccounts: List<LoanAccount> = ArrayList())
