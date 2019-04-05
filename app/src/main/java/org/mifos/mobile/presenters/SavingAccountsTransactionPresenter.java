package org.mifos.mobile.presenters;

import android.content.Context;

import org.mifos.mobile.R;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.injection.ApplicationContext;
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobile.models.accounts.savings.Transactions;
import org.mifos.mobile.presenters.base.BasePresenter;
import org.mifos.mobile.ui.views.SavingAccountsTransactionView;
import org.mifos.mobile.utils.Constants;
import org.mifos.mobile.utils.DateHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by dilpreet on 6/3/17.
 */


public class SavingAccountsTransactionPresenter extends
        BasePresenter<SavingAccountsTransactionView> {

    private final DataManager dataManager;
    private CompositeDisposable compositeDisposables;

    /**
     * Initialises the SavingAccountsDetailPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link androidx.appcompat.app.AppCompatActivity}
     */
    @Inject
    public SavingAccountsTransactionPresenter(DataManager dataManager,
            @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposables = new CompositeDisposable();
    }

    @Override
    public void attachView(SavingAccountsTransactionView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposables.clear();
    }

    /**
     * Load details of a particular saving account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     *
     * @param accountId Id of Savings Account
     */
    public void loadSavingsWithAssociations(long accountId) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposables.add(dataManager.getSavingsWithAssociations(accountId,
                Constants.TRANSACTIONS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<SavingsWithAssociations>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingSavingAccountsDetail(
                                context.getString(R.string.saving_account_details));
                    }

                    @Override
                    public void onNext(SavingsWithAssociations savingAccount) {
                        getMvpView().hideProgress();
                        getMvpView().showSavingAccountsDetail(savingAccount);
                    }
                })
        );
    }

    /**
     * Used for filtering {@link List} of {@link Transactions} according to {@code startDate} and
     * {@code lastDate}
     *
     * @param savingAccountsTransactionList {@link List} of {@link Transactions}
     * @param startDate                     Starting date for filtering
     * @param lastDate                      Last date for filtering
     * @param type                          Deposit/Withdrawal type for filtering
     */
    public void filterTransactionList(List<Transactions> savingAccountsTransactionList,
                                      final long startDate, final long lastDate, final int type) {
        List<Transactions> list;

        if (startDate == -1 && type == -1) {
            list = savingAccountsTransactionList;
        } else if (startDate != -1 && type == -1) {
            list = Observable.fromIterable(savingAccountsTransactionList)
                    .filter(new Predicate<Transactions>() {
                        @Override
                        public boolean test(Transactions transactions) {
                            return startDate <= DateHelper.
                                    getDateAsLongFromList(transactions.getDate())
                                    && lastDate >= DateHelper.
                                    getDateAsLongFromList(transactions.getDate());
                        }
                    })
                    .toList().blockingGet();
        } else if (startDate == -1 && type != -1) {
            list = Observable.fromIterable(savingAccountsTransactionList)
                    .filter(new Predicate<Transactions>() {
                        @Override
                        public boolean test(Transactions transactions) {
                            if (type == 0) {
                                return transactions.getTransactionType().getDeposit();
                            } else {
                                return transactions.getTransactionType().getWithdrawal();
                            }
                        }
                    })
                    .toList().blockingGet();
        } else {
            list = Observable.fromIterable(savingAccountsTransactionList)
                    .filter(new Predicate<Transactions>() {
                        @Override
                        public boolean test(Transactions transactions) {
                            if (type == 0) {
                                return startDate <= DateHelper.
                                        getDateAsLongFromList(transactions.getDate())
                                        && lastDate >= DateHelper.
                                        getDateAsLongFromList(transactions.getDate())
                                        && transactions.getTransactionType().getDeposit();
                            } else {
                                return startDate <= DateHelper.
                                        getDateAsLongFromList(transactions.getDate())
                                        && lastDate >= DateHelper.
                                        getDateAsLongFromList(transactions.getDate())
                                        && transactions.getTransactionType().getWithdrawal();
                            }
                        }
                    })
                    .toList().blockingGet();
        }
        getMvpView().showFilteredList(list);
    }


}
