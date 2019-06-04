package org.mifos.mobile.ui.views;

/*
 * Created by saksham on 16/June/2018
 */

import org.mifos.mobile.ui.views.base.MVPView;

public interface SurveyQuestionView extends MVPView {

    void showError(String error);
    void showMessage(String message);
}
