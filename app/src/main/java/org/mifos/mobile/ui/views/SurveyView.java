package org.mifos.mobile.ui.views;

/*
 * Created by saksham on 16/June/2018
 */

import org.mifos.mobile.survey.Survey;
import org.mifos.mobile.ui.views.base.MVPView;

public interface SurveyView extends MVPView {

    void showSurvey(Survey list);
    void showError(String error);
    void showMessage(String message);
    void finish();
}
