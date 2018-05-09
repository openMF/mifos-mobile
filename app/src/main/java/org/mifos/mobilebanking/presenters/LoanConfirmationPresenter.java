package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.payload.LoansPayload;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.LoanConfirmationView;
import org.mifos.mobilebanking.utils.MFErrorParser;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/*
 * Created by saksham on 09/May/2018
 */

public class LoanConfirmationPresenter extends BasePresenter<LoanConfirmationView> {

    DataManager dataManager;
    CompositeDisposable compositeDisposable;

    @Inject
    public LoanConfirmationPresenter(DataManager dataManager, @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoanConfirmationView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void createLoansAccount(LoansPayload loansPayload) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.createLoansAccount(loansPayload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(MFErrorParser.errorMessage(e));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        getMvpView().showLoanAccountCreatedSuccessfully();
                    }
                })
        );
    }

    /**
     * Used for updating LoanAccount using the {@code loansPayload} and specified {@code loanId} and
     * notifies the view after successful updation of a LoanAccount. And in case of any error during
     * updation, it notifies the view.
     * @param loanId Id of Loan which needs to be updated
     * @param loansPayload {@link LoansPayload} required for Loan Updation
     */
    public void updateLoanAccount(long loanId, LoansPayload loansPayload) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.updateLoanAccount(loanId, loansPayload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(MFErrorParser.errorMessage(e));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        getMvpView().showLoanAccountUpdatedSuccessfully();
                    }
                })
        );
    }
}
