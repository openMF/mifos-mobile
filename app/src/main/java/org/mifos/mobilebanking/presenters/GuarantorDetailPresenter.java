package org.mifos.mobilebanking.presenters;

/*
 * Created by saksham on 25/July/2018
 */

import android.content.Context;
import android.util.Log;

import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.GuarantorDetailView;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class GuarantorDetailPresenter extends BasePresenter<GuarantorDetailView> {

    public static final String TAG = GuarantorDetailPresenter.class.getSimpleName();

    CompositeDisposable compositeDisposable;
    DataManager dataManager;

    @Override
    public void attachView(GuarantorDetailView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Inject
    protected GuarantorDetailPresenter(@ApplicationContext Context context,
                                       DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    public void deleteGuarantor(long loanId, long guarantorId) {
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.deleteGuarantor(loanId, guarantorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        try {
                            getMvpView().guarantorDeletedSuccessfully(responseBody.string());
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
