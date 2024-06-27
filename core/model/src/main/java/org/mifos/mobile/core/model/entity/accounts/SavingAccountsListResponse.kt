package org.mifos.mobile.core.model.entity.accounts

import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount

/**
 * @author Vishwajeet
 * @since 13/08/16
 */
data class SavingAccountsListResponse(
    var savingsAccounts: List<SavingAccount> = ArrayList(),
)
