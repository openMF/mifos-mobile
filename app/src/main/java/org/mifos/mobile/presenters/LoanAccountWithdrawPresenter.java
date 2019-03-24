package org.mifos.mobile.presenters;

import android.content.Context;

import org.mifos.mobile.R;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.injection.ApplicationContext;
import org.mifos.mobile.models.accounts.loan.LoanWithdraw;
import org.mifos.mobile.presenters.base.BasePresenter;
import org.mifos.mobile.ui.views.LoanAccountWithdrawView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by dilpreet on 7/6/17.
 */

public class LoanAccountWithdrawPresenter extends BasePresenter<LoanAccountWithdrawView> {

    private final DataManager dataManager;
    private CompositeDisposable compositeDisposable;

    /**
     * Initialises the LoanAccountDetailsPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link androidx.appcompat.app.AppCompatActivity}
     */
    @Inject
    public LoanAccountWithdrawPresenter(DataManager dataManager,
            @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoanAccountWithdrawView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    /**
     * Used for withdrawing a LoanAccount using the given {@code loanId} and notifies the view after
     * successful withdrawing of a LoanAccount. And in case of any error during withdrawing, it
     * notifies the view.
     *
     * @param loanId       Id of LoanAccount which you want to delete
     * @param loanWithdraw {@link LoanWithdraw} for the Withdrawing LoanAccount
     */
    public void withdrawLoanAccount(long loanId, LoanWithdraw loanWithdraw) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.withdrawLoanAccount(loanId, loanWithdraw)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onComplete() {

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
