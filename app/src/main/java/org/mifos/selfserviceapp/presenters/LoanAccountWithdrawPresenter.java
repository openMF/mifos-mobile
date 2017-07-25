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

    /**
     * Initialises the LoanAccountDetailsPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */
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

    /**
     * Used for withdrawing a LoanAccount using the given {@code loanId} and notifies the view after
     * successful withdrawing of a LoanAccount. And in case of any error during withdrawing, it
     * notifies the view.
     * @param loanId Id of LoanAccount which you want to delete
     * @param loanWithdraw {@link LoanWithdraw} for the Withdrawing LoanAccount
     */
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
