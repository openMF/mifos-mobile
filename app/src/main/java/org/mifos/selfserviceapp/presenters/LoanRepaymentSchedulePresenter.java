package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.LoanRepaymentScheduleMvpView;
import org.mifos.selfserviceapp.utils.Constants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 03/03/17.
 */
public class LoanRepaymentSchedulePresenter extends BasePresenter<LoanRepaymentScheduleMvpView> {

    private final DataManager dataManager;
    private CompositeSubscription subscriptions;

    /**
     * Initialises the AccountsPresenter by automatically injecting an instance of
     * {@link Context} and {@link DataManager} .
     *
     * @param context     Context of the view attached to the presenter.
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     */
    @Inject
    public LoanRepaymentSchedulePresenter(@ApplicationContext Context context,
            DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(LoanRepaymentScheduleMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.clear();
    }

    /**
     * Load details of a particular loan account with its Repayment Schedule as
     * {@link LoanWithAssociations} from the server and notify the view to display it. Notify the
     * view, in case there is any error in fetching the details from server.
     * @param loanId Id of Loan Account
     */
    public void loanLoanWithAssociations(long loanId) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, loanId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoanWithAssociations>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context
                                .getString(R.string.error_fetching_repayment_schedule));
                    }

                    @Override
                    public void onNext(LoanWithAssociations loanWithAssociations) {
                        getMvpView().hideProgress();
                        if (!loanWithAssociations.getRepaymentSchedule().getPeriods().isEmpty()) {
                            getMvpView().showLoanRepaymentSchedule(loanWithAssociations);
                        } else {
                            getMvpView().showEmptyRepaymentsSchedule(loanWithAssociations);
                        }
                    }
                })
        );
    }
}