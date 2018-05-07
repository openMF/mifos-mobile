package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.accounts.loan.LoanWithAssociations;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.LoanRepaymentScheduleMvpView;
import org.mifos.mobilebanking.utils.Constants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Rajan Maurya on 03/03/17.
 */
public class LoanRepaymentSchedulePresenter extends BasePresenter<LoanRepaymentScheduleMvpView> {

    private final DataManager dataManager;
    private CompositeDisposable compositeDisposable;

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
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoanRepaymentScheduleMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
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
        compositeDisposable.add(dataManager.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE,
                                                                    loanId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<LoanWithAssociations>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context
                                .getString(R.string.repayment_schedule));
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
