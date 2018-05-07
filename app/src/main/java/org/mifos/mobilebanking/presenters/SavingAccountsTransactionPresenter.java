package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobilebanking.models.accounts.savings.Transactions;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.SavingAccountsTransactionView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DateHelper;

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
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
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
     * @param savingAccountsTransactionList {@link List} of {@link Transactions}
     * @param startDate Starting date for filtering
     * @param lastDate Last date for filtering
     */
    public void filterTransactionList(List<Transactions> savingAccountsTransactionList,
                                      final long startDate , final long lastDate) {
        List<Transactions> list = Observable.fromIterable(savingAccountsTransactionList)
                .filter(new Predicate<Transactions>() {
                    @Override
                    public boolean test(Transactions transactions) {
                        return startDate <= DateHelper.getDateAsLongFromList(transactions.getDate())
                                && lastDate >= DateHelper.
                                getDateAsLongFromList(transactions.getDate());
                    }
                })
                .toList().blockingGet();

        getMvpView().showFilteredList(list);
    }


}
