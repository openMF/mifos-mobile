package org.mifos.mobile.presenters;

/*
 * Created by saksham on 24/July/2018
 */

import android.content.Context;

import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.injection.ApplicationContext;
import org.mifos.mobile.models.guarantor.GuarantorPayload;
import org.mifos.mobile.presenters.base.BasePresenter;
import org.mifos.mobile.ui.views.GuarantorListView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class GuarantorListPresenter extends BasePresenter<GuarantorListView> {

    DataManager dataManager;
    CompositeDisposable compositeDisposable;

    @Inject
    public GuarantorListPresenter(@ApplicationContext Context context, DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(GuarantorListView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void getGuarantorList(long loanId) {
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.getGuarantorList(loanId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<GuarantorPayload>>() {
                    @Override
                    public void onNext(List<GuarantorPayload> payload) {
                        getMvpView().hideProgress();
                        getMvpView().showGuarantorListSuccessfully(payload);
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
