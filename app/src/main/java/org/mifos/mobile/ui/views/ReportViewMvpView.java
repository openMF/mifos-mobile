package org.mifos.mobile.ui.views;

/*
 * Created by saksham on 02/June/2018
 */

import org.mifos.mobile.ui.views.base.MVPView;

public interface ReportViewMvpView extends MVPView {

    void downloadReport();

    void showError(String message);

    void showMessage(String message);
}
