package org.mifos.mobile.ui.views

import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.accounts.savings.Transactions
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 6/3/17.
 */
interface SavingAccountsTransactionView : MVPView {
    fun showUserInterface()
    fun showSavingAccountsDetail(savingsWithAssociations: SavingsWithAssociations?)
    fun showErrorFetchingSavingAccountsDetail(message: String?)
    fun showFilteredList(list: List<Transactions?>?)
    fun showEmptyTransactions()
}