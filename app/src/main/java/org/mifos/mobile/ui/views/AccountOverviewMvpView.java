package org.mifos.mobile.ui.views;

import org.mifos.mobile.ui.views.base.MVPView;

/**
 * @author Rajan Maurya
 *         On 16/10/17.
 */
public interface AccountOverviewMvpView extends MVPView {

    void showTotalLoanSavings(double totalLoan, double totalSavings);

    void showError(String message);
}
