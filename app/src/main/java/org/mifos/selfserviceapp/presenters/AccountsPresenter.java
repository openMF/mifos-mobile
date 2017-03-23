package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.models.client.ClientAccounts;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.AccountsView;
import org.mifos.selfserviceapp.utils.Constants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 23/10/16.
 */

public class AccountsPresenter extends BasePresenter<AccountsView> {

    private final DataManager dataManager;
    private CompositeSubscription subscriptions;

    @Inject
    public AccountsPresenter(@ApplicationContext Context context, DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(AccountsView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.unsubscribe();
    }

    public void loadClientAccounts() {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getClientAccounts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ClientAccounts>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context.getString(R.string.error_fetching_accounts));
                    }

                    @Override
                    public void onNext(ClientAccounts clientAccounts) {
                        getMvpView().hideProgress();
                        getMvpView().showSavingsAccounts(clientAccounts.getSavingsAccounts());
                        getMvpView().showLoanAccounts(clientAccounts.getLoanAccounts());
                        getMvpView().showShareAccounts(clientAccounts.getShareAccounts());
                    }
                })
        );
    }

    public void loadAccounts(final String accountType) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getAccounts(accountType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ClientAccounts>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context.getString(R.string.error_fetching_accounts));
                    }

                    @Override
                    public void onNext(ClientAccounts clientAccounts) {
                        getMvpView().hideProgress();
                        switch (accountType) {
                            case Constants.SAVINGS_ACCOUNTS:
                                getMvpView().showSavingsAccounts(
                                        clientAccounts.getSavingsAccounts());
                                break;
                            case Constants.LOAN_ACCOUNTS:
                                getMvpView().showLoanAccounts(clientAccounts.getLoanAccounts());
                                break;
                            case Constants.SHARE_ACCOUNTS:
                                getMvpView().showShareAccounts(clientAccounts.getShareAccounts());
                                break;
                        }
                    }
                })
        );
    }
}
