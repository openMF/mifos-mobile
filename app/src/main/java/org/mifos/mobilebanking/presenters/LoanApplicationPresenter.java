package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.payload.LoansPayload;
import org.mifos.mobilebanking.models.templates.loans.LoanTemplate;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.enums.LoanState;
import org.mifos.mobilebanking.ui.views.LoanApplicationMvpView;
import org.mifos.mobilebanking.utils.MFErrorParser;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * Created by Rajan Maurya on 06/03/17.
 */
public class LoanApplicationPresenter extends BasePresenter<LoanApplicationMvpView> {

    private final DataManager dataManager;
    private CompositeDisposable compositeDisposable;

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
    public LoanApplicationPresenter(DataManager dataManager, @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoanApplicationMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    /**
     * Loads LoanApplicationTemplate from the server as {@link LoanTemplate} and notifies the view
     * depending upon the {@code loanState}. And in case of any error during fetching the required
     * details it notifies the view.
     * @param loanState State of Loan i.e.  {@code LoanState.CREATE} or  {@code LoanState.UPDATE}
     */
    public void loadLoanApplicationTemplate(final LoanState loanState) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.getLoanTemplate()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<LoanTemplate>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context.getString(R.string.error_fetching_template));
                    }

                    @Override
                    public void onNext(LoanTemplate loanTemplate) {
                        getMvpView().hideProgress();
                        if (loanState == LoanState.CREATE) {
                            getMvpView().showLoanTemplate(loanTemplate);
                        } else {
                            getMvpView().showUpdateLoanTemplate(loanTemplate);
                        }
                    }
                })
        );
    }

    /**
     * Loads LoanApplicationTemplate from the server as {@link LoanTemplate} and notifies the view
     * depending upon the {@code productId} and {@code loanState}. And in case of any error during
     * fetching the required details it notifies the view.
     * @param productId ProductId required for Fetching loan template according to it.
     * @param loanState State of Loan i.e.  {@code LoanState.CREATE} or  {@code LoanState.UPDATE}
     */
    public void loadLoanApplicationTemplateByProduct(int productId, final LoanState loanState) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.getLoanTemplateByProduct(productId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<LoanTemplate>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context.getString(R.string.error_fetching_template));
                    }

                    @Override
                    public void onNext(LoanTemplate loanTemplate) {
                        getMvpView().hideProgress();
                        if (loanState == LoanState.CREATE) {
                            getMvpView().showLoanTemplateByProduct(loanTemplate);
                        } else {
                            getMvpView().showUpdateLoanTemplateByProduct(loanTemplate);
                        }
                    }
                })
        );
    }

    /**
     * Used for creating LoanAccount using the {@code loansPayload} and notifies the view after
     * successful creating of a LoanAccount. And in case of any error during creation, it
     * notifies the view.
     * @param loansPayload {@link LoansPayload} required for Loan Creation
     */
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
