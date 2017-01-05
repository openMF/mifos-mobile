package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.ui.views.base.MVPView;
import java.util.List;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public interface HomeView extends MVPView {

    void showLoanAccounts(List<LoanAccount> loanAccounts);

    void showSavingsAccounts(List<SavingAccount> savingAccounts);

    void showError(String errorMessage);
}
