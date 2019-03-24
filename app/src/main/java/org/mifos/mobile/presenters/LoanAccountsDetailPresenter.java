package org.mifos.mobile.presenters;

import android.content.Context;

import org.mifos.mobile.R;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.injection.ApplicationContext;
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations;
import org.mifos.mobile.presenters.base.BasePresenter;
import org.mifos.mobile.ui.views.LoanAccountsDetailView;
import org.mifos.mobile.utils.Constants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * @author Vishwajeet
 * @since 19/08/16
 */

public class LoanAccountsDetailPresenter extends BasePresenter<LoanAccountsDetailView> {

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
    public LoanAccountsDetailPresenter(DataManager dataManager,
            @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoanAccountsDetailView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    /**
     * Load details of a particular loan account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     *
     * @param loanId Id of Loan Account
     */
    public void loadLoanAccountDetails(long loanId) {
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
                        getMvpView().showErrorFetchingLoanAccountsDetail(
                                context.getString(R.string.loan_account_details));
                    }

                    @Override
                    public void onNext(LoanWithAssociations loanWithAssociations) {
                        getMvpView().hideProgress();
                        if (loanWithAssociations != null) {
                            getMvpView().showLoanAccountsDetail(loanWithAssociations);
                        }
                    }
                })
        );
    }
}
