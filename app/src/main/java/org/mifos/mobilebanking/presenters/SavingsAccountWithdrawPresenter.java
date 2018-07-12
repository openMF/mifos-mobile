package org.mifos.mobilebanking.presenters;

/*
 * Created by saksham on 02/July/2018
 */

import android.content.Context;

import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.accounts.savings.SavingsAccountWithdrawPayload;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.SavingsAccountWithdrawView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class SavingsAccountWithdrawPresenter extends BasePresenter<SavingsAccountWithdrawView> {

    private DataManager dataManager;
    private CompositeDisposable compositeDisposable;

    @Inject
    protected SavingsAccountWithdrawPresenter(DataManager dataManager,
                                              @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(SavingsAccountWithdrawView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void submitWithdrawSavingsAccount(String accountId,
                                             SavingsAccountWithdrawPayload payload) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.submitWithdrawSavingsAccount(accountId, payload)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBodyObservable) {
                        getMvpView().hideProgress();
                        getMvpView().showSavingsAccountWithdrawSuccessfully();
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
