package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.data.accounts.SavingAccount;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import retrofit2.Response;

/**
 * Created by vjs3 on 21/6/16.
 */
public interface SavingAccountsListView extends MVPView{

    void showSavingAccounts(Response<SavingAccount> response);

    void showErrorFetchingSavingAccounts(String string);

    void showProgress();

    void hideProgress();

}
