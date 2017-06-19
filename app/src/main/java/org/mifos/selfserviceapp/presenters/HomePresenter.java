package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccountsMetaData;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccountsMetaData;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccount;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccountsMetaData;
import org.mifos.selfserviceapp.models.client.ClientAccounts;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.HomeView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 19/6/17.
 */

public class HomePresenter extends BasePresenter<HomeView> {

    private DataManager dataManager;
    private CompositeSubscription subscription;

    @Inject
    public HomePresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void attachView(HomeView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    public void loadClientAccountDetails() {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.getClientAccounts()
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
                        getMvpView().showLoanAccountDetails(getLoanAccountDetails(clientAccounts
                                .getLoanAccounts()));
                        getMvpView().showSavingAccountDetails(getSavingAccountDetails(clientAccounts
                                .getSavingsAccounts()));
                        getMvpView().showShareAccountDetails(getShareAccountDetails(clientAccounts
                                .getShareAccounts()));
                    }
                })
        );
    }

    private LoanAccountsMetaData getLoanAccountDetails(List<LoanAccount> loanAccountList) {
        LoanAccountsMetaData accountsMetaData = new LoanAccountsMetaData();
        for (LoanAccount loanAccount : loanAccountList) {
            if (loanAccount.getStatus().getActive()) {
                accountsMetaData.incActive();
            } else if (loanAccount.getStatus().getWaitingForDisbursal()) {
                accountsMetaData.incWaitingForDisbursal();
            } else if (loanAccount.getStatus().getPendingApproval()) {
                accountsMetaData.incPendingApproval();
            } else if (loanAccount.getStatus().getOverpaid()) {
                accountsMetaData.incOverpaid();
            } else if (loanAccount.getStatus().getClosed()) {
                accountsMetaData.incClosed();
            }
            accountsMetaData.addAmount(loanAccount.getLoanBalance());
        }
        return accountsMetaData;
    }

    private SavingAccountsMetaData getSavingAccountDetails(List<SavingAccount> savingAccountList) {
        SavingAccountsMetaData accountsMetaData = new SavingAccountsMetaData();
        for (SavingAccount savingAccount : savingAccountList) {
            if (savingAccount.getStatus().getActive()) {
                accountsMetaData.incActive();
            } else if (savingAccount.getStatus().getApproved()) {
                accountsMetaData.incApproved();
            } else if (savingAccount.getStatus().getSubmittedAndPendingApproval()) {
                accountsMetaData.incPendingApproval();
            } else if (savingAccount.getStatus().getMatured()) {
                accountsMetaData.incMatured();
            } else if (savingAccount.getStatus().getClosed()) {
                accountsMetaData.incClosed();
            }
            accountsMetaData.addAmount(savingAccount.getAccountBalance());
        }
        return accountsMetaData;
    }

    private ShareAccountsMetaData getShareAccountDetails(List<ShareAccount> shareAccountList) {
        ShareAccountsMetaData accountsMetaData = new ShareAccountsMetaData();
        for (ShareAccount shareAccount : shareAccountList) {
            if (shareAccount.getStatus().getActive()) {
                accountsMetaData.incActive();
            } else if (shareAccount.getStatus().getApproved()) {
                accountsMetaData.incApproved();
            } else if (shareAccount.getStatus().getSubmittedAndPendingApproval()) {
                accountsMetaData.incPendingApproval();
            } else if (shareAccount.getStatus().getRejected()) {
                accountsMetaData.incRejected();
            } else if (shareAccount.getStatus().getClosed()) {
                accountsMetaData.incClosed();
            }
            accountsMetaData.addShare(shareAccount.getTotalApprovedShares());
        }
        return accountsMetaData;
    }
}
