package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.models.CheckboxStatus;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccount;
import org.mifos.selfserviceapp.models.client.ClientAccounts;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.AccountsView;
import org.mifos.selfserviceapp.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 23/10/16.
 */

public class AccountsPresenter extends BasePresenter<AccountsView> {

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

    /**
     * Loads savings, loan and share accounts associated with the Client from the server
     * and notifies the view to display it. And in case of any error during fetching the required
     * details it notifies the view.
     */
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

    /**
     * Loads savings, loan or share account depending upon {@code accountType} provided from the
     * server and notifies the view to display it.And in case of any error during fetching the
     * required details it notifies the view.
     * @param accountType Type of account for which we need to fetch details
     */
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

    /**
     * Filters {@link List} of {@link SavingAccount}
     * @param accounts {@link List} of {@link SavingAccount}
     * @param input {@link String} which is used for filtering
     * @return Returns {@link List} of filtered {@link SavingAccount} according to the {@code input}
     * provided.
     */
    public List<SavingAccount> searchInSavingsList(List<SavingAccount> accounts,
                                            final String input) {
        return Observable.from(accounts)
                .filter(new Func1<SavingAccount, Boolean>() {
                    @Override
                    public Boolean call(SavingAccount savingAccount) {
                        return savingAccount.getProductName().toLowerCase().contains(input.
                                toLowerCase());
                    }
                }).toList().toBlocking().single();
    }

    /**
     * Filters {@link List} of {@link LoanAccount}
     * @param accounts {@link List} of {@link LoanAccount}
     * @param input {@link String} which is used for filtering
     * @return Returns {@link List} of filtered {@link LoanAccount} according to the {@code input}
     * provided.
     */
    public List<LoanAccount> searchInLoanList(List<LoanAccount> accounts,
                                                   final String input) {
        return Observable.from(accounts)
                .filter(new Func1<LoanAccount, Boolean>() {
                    @Override
                    public Boolean call(LoanAccount loanAccount) {
                        return loanAccount.getProductName().toLowerCase().contains(input.
                                toLowerCase());
                    }
                }).toList().toBlocking().single();
    }

    /**
     * Filters {@link List} of {@link ShareAccount}
     * @param accounts {@link List} of {@link ShareAccount}
     * @param input {@link String} which is used for filtering
     * @return Returns {@link List} of filtered {@link ShareAccount} according to the {@code input}
     * provided.
     */
    public List<ShareAccount> searchInSharesList(List<ShareAccount> accounts,
                                                   final String input) {
        return Observable.from(accounts)
                .filter(new Func1<ShareAccount, Boolean>() {
                    @Override
                    public Boolean call(ShareAccount shareAccount) {
                        return shareAccount.getProductName().toLowerCase().contains(input.
                                toLowerCase());
                    }
                }).toList().toBlocking().single();
    }

    /**
     * Filters {@link List} of {@link CheckboxStatus}
     * @param statusModelList {@link List} of {@link CheckboxStatus}
     * @return Returns {@link List} of {@link CheckboxStatus} which have
     * {@code checkboxStatus.isChecked()} as true.
     */
    public List<CheckboxStatus> getCheckedStatus(List<CheckboxStatus> statusModelList) {
        return Observable.from(statusModelList)
                .filter(new Func1<CheckboxStatus, Boolean>() {
                    @Override
                    public Boolean call(CheckboxStatus checkboxStatus) {
                        return checkboxStatus.isChecked();
                    }
                }).toList().toBlocking().single();
    }

    /**
     * Filters {@link List} of {@link SavingAccount} according to {@link CheckboxStatus}
     * @param accounts {@link List} of filtered {@link SavingAccount}
     * @param status Used for filtering the {@link List}
     * @return Returns {@link List} of filtered {@link SavingAccount} according to the
     * {@code status} provided.
     */
    public List<SavingAccount> getFilteredSavingsAccount(List<SavingAccount> accounts,
                                                         final CheckboxStatus status) {
        return Observable.from(accounts)
                .filter(new Func1<SavingAccount, Boolean>() {
                    @Override
                    public Boolean call(SavingAccount account) {
                        if (status.getStatus().compareTo(context.getString(R.string.none)) == 0) {
                            return true;
                        }
                        if (status.getStatus().compareTo(context.getString(R.string.active)) == 0 &&
                                account.getStatus().getActive()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                approved)) == 0 && account.getStatus().getApproved()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                approval_pending)) == 0 && account.getStatus().
                                getSubmittedAndPendingApproval()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                matured)) == 0 && account.getStatus().getMatured()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                closed)) == 0 && account.getStatus().getClosed()) {
                            return true;
                        }
                        return false;
                    }
                }).toList().toBlocking().single();
    }

    /**
     * Filters {@link List} of {@link LoanAccount} according to {@link CheckboxStatus}
     * @param accounts {@link List} of filtered {@link LoanAccount}
     * @param status Used for filtering the {@link List}
     * @return Returns {@link List} of filtered {@link LoanAccount} according to the
     * {@code status} provided.
     */
    public List<LoanAccount> getFilteredLoanAccount(List<LoanAccount> accounts,
                                                         final CheckboxStatus status) {
        return Observable.from(accounts)
                .filter(new Func1<LoanAccount, Boolean>() {
                    @Override
                    public Boolean call(LoanAccount account) {
                        if (status.getStatus().compareTo(context.getString(R.string.none)) == 0) {
                            return true;
                        }
                        if (status.getStatus().compareTo(context.getString(R.string.inArrears)) == 0
                                && account.getInArrears()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                active)) == 0 && account.getStatus().getActive()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                waiting_for_disburse)) == 0 && account.getStatus().
                                getWaitingForDisbursal()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                approval_pending)) == 0 && account.getStatus().
                                getPendingApproval()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                overpaid)) == 0 && account.getStatus().getOverpaid()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                closed)) == 0 && account.getStatus().getClosed()) {
                            return true;
                        }
                        return false;
                    }
                }).toList().toBlocking().single();
    }

    /**
     * Filters {@link List} of {@link ShareAccount} according to {@link CheckboxStatus}
     * @param accounts {@link List} of filtered {@link ShareAccount}
     * @param status Used for filtering the {@link List}
     * @return Returns {@link List} of filtered {@link ShareAccount} according to the
     * {@code status} provided.
     */
    public List<ShareAccount> getFilteredShareAccount(List<ShareAccount> accounts,
                                                        final CheckboxStatus status) {
        return Observable.from(accounts)
                .filter(new Func1<ShareAccount, Boolean>() {
                    @Override
                    public Boolean call(ShareAccount account) {
                        if (status.getStatus().compareTo(context.getString(R.string.none)) == 0) {
                            return true;
                        }
                        if (status.getStatus().compareTo(context.getString(R.string.active)) == 0 &&
                                account.getStatus().getActive()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                approved)) == 0 && account.getStatus().getApproved()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                approval_pending)) == 0 && account.getStatus().
                                getSubmittedAndPendingApproval()) {
                            return true;
                        } else if (status.getStatus().compareTo(context.getString(R.string.
                                closed)) == 0 && account.getStatus().getClosed()) {
                            return true;
                        }
                        return false;
                    }
                }).toList().toBlocking().single();
    }

}
