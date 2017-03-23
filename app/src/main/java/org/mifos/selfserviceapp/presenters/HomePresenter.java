package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingsWithAssociations;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.client.ClientAccounts;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.HomeView;
import org.mifos.selfserviceapp.utils.Constants;
import javax.inject.Inject;
import rx.subscriptions.CompositeSubscription;
import rx.schedulers.Schedulers;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public class HomePresenter extends BasePresenter<HomeView> {

    private final DataManager dataManager;
    private CompositeSubscription subscriptions;

    @Inject
    public HomePresenter(@ApplicationContext Context context, DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(HomeView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public long getClientId() {
        return dataManager.getClientId();
    }

    /**
     * This method fetching the Client, associated with current Access Token.
     */
    public void loadClient() {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getClients()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Page<Client>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onNext(Page<Client> pageClient) {
                        getMvpView().hideProgress();
                        if (pageClient.getPageItems().size() != 0) {
                            getMvpView().showClientInfo(pageClient.getPageItems().get(0));
                        }
                    }
                })
        );
    }

    public void loadInfo() {
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
                    }

                    @Override
                    public void onNext(ClientAccounts clientAccounts) {
                        getMvpView().hideProgress();
                        getMvpView().showInfo(clientAccounts.getLoanAccounts(),
                                                clientAccounts.getSavingsAccounts(),
                                                clientAccounts.getShareAccounts());
                    }
                })
        );
    }

        /**
         * Load details of a particular loan account from the server and notify the view
         * to display it. Notify the view, in case there is any error in fetching
         * the details from server.
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


    /**
     * Load details of a particular saving account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     */
    public void loadSavingAccountDetails(long accountId) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getSavingsWithAssociations(accountId, Constants.TRANSACTIONS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SavingsWithAssociations>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onNext(SavingsWithAssociations savingAccount) {
                        getMvpView().hideProgress();
                        if (savingAccount != null) {
                            getMvpView().showSavingAccountsDetail(savingAccount);
                        }
                    }
                })
        );
    }

}
