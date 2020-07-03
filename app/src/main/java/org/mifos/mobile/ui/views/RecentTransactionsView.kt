package org.mifos.mobile.ui.views

import org.mifos.mobile.models.Transaction
import org.mifos.mobile.ui.views.base.MVPView

/**
 * @author Vishwajeet
 * @since 10/08/16
 */
interface RecentTransactionsView : MVPView {
    /**
     * Use to setup basic UI elements like RecyclerView and SwipeToRefresh etc.
     */
    fun showUserInterface()

    /**
     * Should be called if there is any error from client side in loading the recent transactions
     * from server.
     * Reason for error should be mentioned clearly to the user.
     *
     * @param message Error message to display showing reason of failure in loading recent
     * transactions.
     */
    fun showErrorFetchingRecentTransactions(message: String?)

    /**
     * Use to display List of recent transactions for the respective client.
     *
     * @param transactions List containing recent transactions of a particular client
     */
    fun showRecentTransactions(transactions: List<Transaction?>?)
    fun showLoadMoreRecentTransactions(transactions: List<Transaction?>?)
    fun resetUI()
    fun showMessage(message: String?)

    /**
     * Use to indicate user that there is no transaction to show;
     */
    fun showEmptyTransaction()

    /**
     * Use to show and hide the swipe Layout;
     * @param show Boolean
     */
    fun showSwipeRefreshLayout(show: Boolean)
}