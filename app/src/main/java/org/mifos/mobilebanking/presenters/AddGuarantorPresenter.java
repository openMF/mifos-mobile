package org.mifos.mobilebanking.presenters;

/*
 * Created by saksham on 23/July/2018
 */

import android.content.Context;
import android.util.Log;

import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ActivityContext;
import org.mifos.mobilebanking.models.guarantor.GuarantorApplicationPayload;
import org.mifos.mobilebanking.models.guarantor.GuarantorTemplatePayload;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.enums.GuarantorState;
import org.mifos.mobilebanking.ui.views.AddGuarantorView;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class AddGuarantorPresenter extends BasePresenter<AddGuarantorView> {

    public static final String TAG = AddGuarantorPresenter.class.getSimpleName();
    DataManager dataManager;
    CompositeDisposable compositeDisposable;

    @Inject
    protected AddGuarantorPresenter(@ActivityContext Context context, DataManager dataManager) {
        super(context);
        compositeDisposable = new CompositeDisposable();
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(AddGuarantorView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void getGuarantorTemplate(final GuarantorState state, long loanId) {
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.getGuarantorTemplate(loanId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<GuarantorTemplatePayload>() {
                    @Override
                    public void onNext(GuarantorTemplatePayload payload) {
                        getMvpView().hideProgress();
                        if (state == GuarantorState.CREATE) {
                            getMvpView().showGuarantorApplication(payload);

                        } else if (state == GuarantorState.UPDATE) {
                            getMvpView().showGuarantorUpdation(payload);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void createGuarantor(long loanId, final GuarantorApplicationPayload payload) {
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.createGuarantor(loanId, payload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        try {
                            getMvpView().submittedSuccessfully(responseBody.string(), payload);
                        } catch (IOException e) {
                            Log.d(TAG, e.getMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void updateGuarantor(GuarantorApplicationPayload payload, long loanId,
                                long guarantorId) {
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.updateGuarantor(payload, loanId, guarantorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        try {
                            getMvpView().updatedSuccessfully(responseBody.string());
                        } catch (IOException e) {
                            Log.d(TAG, e.getMessage());
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
}
