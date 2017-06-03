package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithdraw;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.LoanAccountWithdrawView;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 7/6/17.
 */

public class LoanAccountWithdrawPresenter extends BasePresenter<LoanAccountWithdrawView> {

    private final DataManager dataManager;
    private CompositeSubscription subscriptions;

    @Inject
    public LoanAccountWithdrawPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(LoanAccountWithdrawView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.clear();
    }


    public void withdrawLoanAccount(long loanId, LoanWithdraw loanWithdraw) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.withdrawLoanAccount(loanId, loanWithdraw)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showLoanAccountWithdrawError(
                                context.getString(R.string.error_loan_account_withdraw));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        getMvpView().showLoanAccountWithdrawSuccess();
                    }
                }));
    }
}
