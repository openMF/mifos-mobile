package org.mifos.mobilebanking.presenters;

/*
 * Created by saksham on 17/June/2018
 */

import android.content.Context;

import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.survey.SubmitSurveyPayload;
import org.mifos.mobilebanking.models.survey.Survey;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.SurveyView;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class SurveyPresenter extends BasePresenter<SurveyView> {

    CompositeDisposable compositeDisposable;
    DataManager dataManager;

    @Inject
    protected SurveyPresenter(@ApplicationContext Context context, DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(SurveyView mvpView) {
        super.attachView(mvpView);
    }

    public void loadSurveyQuestion() {
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.getSurveyQuestions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<Survey>>() {
                    @Override
                    public void onNext(List<Survey> surveys) {
                        getMvpView().hideProgress();
                        getMvpView().showSurvey(surveys.get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void submitSurvey(SubmitSurveyPayload payload) {
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.submitSurvey(payload).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        try {
                            getMvpView().showMessage(responseBody.string());
                            getMvpView().finish();
                        } catch (IOException e) {
                            getMvpView().showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }


    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }
}
