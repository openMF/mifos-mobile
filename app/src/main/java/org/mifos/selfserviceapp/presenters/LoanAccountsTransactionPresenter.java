package org.mifos.selfserviceapp.presenters;
/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.LoanAccountsTransactionView;
import org.mifos.selfserviceapp.utils.Constants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 4/3/17.
 */

public class LoanAccountsTransactionPresenter extends BasePresenter<LoanAccountsTransactionView> {

    private final DataManager dataManager;
    private CompositeSubscription subscriptions;

    /**
     * Initialises the LoginPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */
    @Inject
    public LoanAccountsTransactionPresenter(DataManager dataManager,
                                            @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(LoanAccountsTransactionView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.unsubscribe();
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
        subscriptions.add(dataManager.getLoanWithAssociations(Constants.TRANSACTIONS, loanId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoanWithAssociations>() {
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
                    public void onNext(LoanWithAssociations loanWithAssociations) {
                        getMvpView().hideProgress();
                        if (!loanWithAssociations.getTransactions().isEmpty()) {
                            getMvpView().showLoanTransactions(loanWithAssociations);
                        } else {
                            getMvpView().showEmptyTransactions(loanWithAssociations);
                        }
                    }
                })
        );
    }

}
