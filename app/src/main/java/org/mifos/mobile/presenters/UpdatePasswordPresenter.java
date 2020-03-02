package org.mifos.mobile.presenters;

/*
 * Created by saksham on 13/July/2018
 */

import android.content.Context;

import org.mifos.mobile.api.BaseApiManager;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.api.local.PreferencesHelper;
import org.mifos.mobile.injection.ApplicationContext;
import org.mifos.mobile.models.UpdatePasswordPayload;
import org.mifos.mobile.presenters.base.BasePresenter;
import org.mifos.mobile.ui.views.UpdatePasswordView;
import org.mifos.mobile.utils.MFErrorParser;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.ResponseBody;

public class UpdatePasswordPresenter extends BasePresenter<UpdatePasswordView> {

    private CompositeDisposable compositeDisposable;
    private DataManager dataManager;
    private PreferencesHelper preferencesHelper;

    @Inject
    public UpdatePasswordPresenter(@ApplicationContext Context context, DataManager dataManager,
                                   PreferencesHelper preferencesHelper) {
        super(context);
        compositeDisposable = new CompositeDisposable();
        this.dataManager = dataManager;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(UpdatePasswordView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void updateAccountPassword(final UpdatePasswordPayload payload) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.updateAccountPassword(payload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        getMvpView().showPasswordUpdatedSuccessfully();
                        updateAuthenticationToken(payload.getPassword());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(MFErrorParser.errorMessage(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void updateAuthenticationToken(String password) {
        String authenticationToken = Credentials.basic(preferencesHelper.getUserName(), password);
        preferencesHelper.saveToken(authenticationToken);
        BaseApiManager.createService(preferencesHelper.getBaseUrl(),
                preferencesHelper.getTenant(),
                preferencesHelper.getToken());
    }
}
