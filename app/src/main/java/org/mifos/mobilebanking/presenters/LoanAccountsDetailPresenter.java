package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.accounts.loan.LoanAccount;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.LoanAccountsDetailView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Vishwajeet
 * @since 19/08/16
 */

public class LoanAccountsDetailPresenter extends BasePresenter<LoanAccountsDetailView> {

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
    public LoanAccountsDetailPresenter(DataManager dataManager,
            @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(LoanAccountsDetailView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.clear();
    }

    /**
     * Load details of a particular loan account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     * @param loanId Id of Loan Account
     */
    public void loadLoanAccountDetails(long loanId) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getLoanAccountDetails(loanId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoanAccount>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingLoanAccountsDetail(
                                context.getString(R.string.error_loan_account_details_loading));
                    }

                    @Override
                    public void onNext(LoanAccount loanAccount) {
                        getMvpView().hideProgress();
                        if (loanAccount != null) {
                            getMvpView().showLoanAccountsDetail(loanAccount);
                        }
                    }
                })
        );
    }
}
