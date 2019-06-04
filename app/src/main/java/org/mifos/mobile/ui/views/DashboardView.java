package org.mifos.mobile.ui.views;

/*
 * Created by saksham on 16/July/2018
 */

import org.mifos.mobile.ui.views.base.MVPView;

import java.util.HashMap;

public interface DashboardView extends MVPView {

    void showTotalAccountsDetail(HashMap<String, Integer> list);

    void showAccountsOverview(HashMap<String, Integer> savingAccountStatus,
                              HashMap<String, Integer> loanAccountStatus,
                              HashMap<String, Integer> shareAccountStatus);
}
