package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by dilpreet on 7/6/17.
 */

public interface LoanAccountWithdrawView extends MVPView {

    public void showLoanAccountWithdrawSuccess();

    public void showLoanAccountWithdrawError(String message);
}
