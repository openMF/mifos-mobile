package org.mifos.mobile.presenters;

import android.content.Context;

import org.mifos.mobile.R;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.injection.ApplicationContext;
import org.mifos.mobile.models.templates.loans.LoanTemplate;
import org.mifos.mobile.presenters.base.BasePresenter;
import org.mifos.mobile.ui.enums.LoanState;
import org.mifos.mobile.ui.views.LoanApplicationMvpView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


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
     *                    it is that of an {@link androidx.appcompat.app.AppCompatActivity}
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
     *
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
     *
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
}
