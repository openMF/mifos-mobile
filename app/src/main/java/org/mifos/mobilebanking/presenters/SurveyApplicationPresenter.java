package org.mifos.mobilebanking.presenters;

/*
 * Created by saksham on 20/May/2018
 */

import android.content.Context;

import org.mifos.mobilebanking.models.SurveyQuestion;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.SurveyApplicationView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyApplicationPresenter extends BasePresenter<SurveyApplicationView> {

    CompositeDisposable compositeDisposable;

    protected SurveyApplicationPresenter(Context context) {
        super(context);
    }

    @Override
    public void attachView(SurveyApplicationView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public List<SurveyQuestion> getSurveyQuestions() {
        return null;
    }

    public void submitSurveyQuestions(List<SurveyQuestion> list) {

    }
}
