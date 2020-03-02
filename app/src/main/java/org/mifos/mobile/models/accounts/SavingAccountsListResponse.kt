package org.mifos.mobile.models.accounts

import org.mifos.mobile.models.accounts.savings.SavingAccount

import java.util.ArrayList

/**
 * @author Vishwajeet
 * @since 13/08/16
 */
data class SavingAccountsListResponse (
    var savingsAccounts: List<SavingAccount> = ArrayList()
)
