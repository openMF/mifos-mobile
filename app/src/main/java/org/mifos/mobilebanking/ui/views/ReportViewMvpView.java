package org.mifos.mobilebanking.ui.views;

/*
 * Created by saksham on 02/June/2018
 */

import org.mifos.mobilebanking.ui.views.base.MVPView;

public interface ReportViewMvpView extends MVPView {

    void downloadReport();

    void showError(String message);

    void showMessage(String message);
}
