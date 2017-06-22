package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.accounts.loan.LoanAccountsMetaData;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccountsMetaData;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccountsMetaData;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by dilpreet on 19/6/17.
 */

public interface HomeView extends MVPView {

    void showUserInterface();

    void showLoanAccountDetails(LoanAccountsMetaData statusCount);

    void showSavingAccountDetails(SavingAccountsMetaData statusCount);

    void showShareAccountDetails(ShareAccountsMetaData statusCount);

    void showError(String errorMessage);

}
