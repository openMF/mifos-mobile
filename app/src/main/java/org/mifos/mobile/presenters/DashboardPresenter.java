package org.mifos.mobile.presenters;

/*
 * Created by saksham on 16/July/2018
 */

import android.content.Context;

import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.injection.ApplicationContext;
import org.mifos.mobile.models.accounts.loan.LoanAccount;
import org.mifos.mobile.models.accounts.savings.SavingAccount;
import org.mifos.mobile.models.accounts.share.ShareAccount;
import org.mifos.mobile.models.client.ClientAccounts;
import org.mifos.mobile.presenters.base.BasePresenter;
import org.mifos.mobile.ui.views.DashboardView;
import org.mifos.mobile.utils.Constants;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DashboardPresenter extends BasePresenter<DashboardView> {

    CompositeDisposable compositeDisposable;
    DataManager dataManager;

    @Inject
    protected DashboardPresenter(@ApplicationContext Context context, DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    public void getAccountsDetails() {
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.getClientAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ClientAccounts>() {
                    @Override
                    public void onNext(ClientAccounts clientAccounts) {
                        getMvpView().hideProgress();
                        getMvpView().showTotalAccountsDetail(getTotalAccounts(clientAccounts));
                        getMvpView().showAccountsOverview(getSavingAccountOverview(clientAccounts
                                        .getSavingsAccounts()),
                                getLoanAccountOverview(clientAccounts.getLoanAccounts()),
                                getShareAccountOverview(clientAccounts.getShareAccounts()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    private HashMap<String, Integer> getTotalAccounts(ClientAccounts clientAccounts) {
        HashMap<String, Integer> rv = new HashMap<>();
        rv.put(Constants.SAVINGS_ACCOUNTS, clientAccounts.getSavingsAccounts().size());
        rv.put(Constants.LOAN_ACCOUNTS, clientAccounts.getLoanAccounts().size());
        rv.put(Constants.SHARE_ACCOUNTS, clientAccounts.getShareAccounts().size());
        return rv;
    }

    private HashMap<String, Integer> getSavingAccountOverview(List<SavingAccount> savingAccounts) {
        HashMap<String, Integer> rv = new HashMap<>();
        for (SavingAccount savingAccount : savingAccounts) {
            String currentKey = savingAccount.getStatus().getValue();
            if (rv.containsKey(currentKey)) {
                int previousValue = rv.get(currentKey);
                rv.put(currentKey, previousValue + 1);
            } else {
                rv.put(currentKey, 1);
            }
        }
        return rv;
    }

    private HashMap<String, Integer> getLoanAccountOverview(List<LoanAccount> loanAccounts) {
        HashMap<String, Integer> rv = new HashMap<>();
        for (LoanAccount loanAccount : loanAccounts) {
            String currentKey = loanAccount.getStatus().getValue();
            if (rv.containsKey(currentKey)) {
                int previousValue = rv.get(currentKey);
                rv.put(currentKey, previousValue + 1);
            } else {
                rv.put(currentKey, 1);
            }
        }
        return rv;
    }

    private HashMap<String, Integer> getShareAccountOverview(List<ShareAccount> shareAccounts) {
        HashMap<String, Integer> rv = new HashMap<>();
        for (ShareAccount shareAccount : shareAccounts) {
            String currentKey = shareAccount.getStatus().getValue();
            if (rv.containsKey(currentKey)) {
                int previousValue = rv.get(currentKey);
                rv.put(currentKey, previousValue + 1);
            } else {
                rv.put(currentKey, 1);
            }
        }
        return rv;
    }
}
