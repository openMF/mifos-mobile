package org.mifos.mobilebanking.presenters;

/*
 * Created by saksham on 30/June/2018
 */

import android.content.Context;

import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.accounts.savings.SavingsAccountApplicationPayload;
import org.mifos.mobilebanking.models.accounts.savings.SavingsAccountUpdatePayload;
import org.mifos.mobilebanking.models.templates.savings.SavingsAccountTemplate;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.enums.SavingsAccountState;
import org.mifos.mobilebanking.ui.views.SavingsAccountApplicationView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class SavingsAccountApplicationPresenter
        extends BasePresenter<SavingsAccountApplicationView> {

    private CompositeDisposable compositeDisposable;
    private DataManager dataManager;

    @Inject
    public SavingsAccountApplicationPresenter(DataManager dataManager,
                                              @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(SavingsAccountApplicationView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void loadSavingsAccountApplicationTemplate(long clientId,
                                                      final SavingsAccountState state) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.getSavingAccountApplicationTemplate(clientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SavingsAccountTemplate>() {

                    @Override
                    public void onNext(SavingsAccountTemplate template) {
                        getMvpView().hideProgress();

                        if (state == SavingsAccountState.CREATE) {
                            getMvpView().showUserInterfaceSavingAccountApplication(template);
                        } else {
                            getMvpView().showUserInterfaceSavingAccountUpdate(template);
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

    public void submitSavingsAccountApplication(SavingsAccountApplicationPayload payload) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.submitSavingAccountApplication(payload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showSavingsAccountApplicationSuccessfully();

                    }
                }));
    }

    public void updateSavingsAccount(String accountId, SavingsAccountUpdatePayload payload) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.updateSavingsAccount(accountId, payload)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showSavingsAccountUpdateSuccessfully();
                    }
                }));

    }
}
