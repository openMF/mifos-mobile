package org.mifos.mobilebanking.ui.views;

import org.mifos.mobilebanking.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobilebanking.models.accounts.savings.Transactions;
import org.mifos.mobilebanking.ui.views.base.MVPView;

import java.util.List;

/**
 * Created by dilpreet on 6/3/17.
 */

public interface SavingAccountsTransactionView extends MVPView {

    void showUserInterface();

    void showSavingAccountsDetail(SavingsWithAssociations savingsWithAssociations);

    void showErrorFetchingSavingAccountsDetail(String message);

    void showFilteredList(List<Transactions> list);

}
