package org.mifos.mobilebanking.ui.views;

import org.mifos.mobilebanking.models.Transaction;
import org.mifos.mobilebanking.ui.views.base.MVPView;

import java.util.List;

/**
 * @author Vishwajeet
 * @since 10/08/16
 */

public interface RecentTransactionsView extends MVPView {


    /**
     * Use to setup basic UI elements like RecyclerView and SwipeToRefresh etc.
     */
    void showUserInterface();

    /**
     * Should be called if there is any error from client side in loading the recent transactions
     * from server.
     * Reason for error should be mentioned clearly to the user.
     *
     * @param message Error message to display showing reason of failure in loading recent
     *                transactions.
     */
    void showErrorFetchingRecentTransactions(String message);

    /**
     * Use to display List of recent transactions for the respective client.
     *
     * @param transactions List containing recent transactions of a particular client
     */
    void showRecentTransactions(List<Transaction> transactions);


    void showLoadMoreRecentTransactions(List<Transaction> transactions);

    void resetUI();

    void showMessage(String message);

    /**
     * Use to indicate user that there is no transaction to show;
     */
    void showEmptyTransaction();

    /**
     * Use to show and hide the swipe Layout;
     * @param show Boolean
     */
    void showSwipeRefreshLayout(boolean show);
}
