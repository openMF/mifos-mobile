package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.accounts.savings.SavingsWithAssociations;
import org.mifos.selfserviceapp.models.accounts.savings.Transactions;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.SavingAccountsTransactionView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DateHelper;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 6/3/17.
 */


public class SavingAccountsTransactionPresenter extends
        BasePresenter<SavingAccountsTransactionView> {

    private final DataManager dataManager;
    private CompositeSubscription subscriptions;


    @Inject
    public SavingAccountsTransactionPresenter(DataManager dataManager,
                                              @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(SavingAccountsTransactionView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.unsubscribe();
    }

    public void loadSavingsWithAssociations(long accountId) {
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
                        getMvpView().showErrorFetchingSavingAccountsDetail(
                                context.getString(R.string.error_saving_account_details_loading));
                    }

                    @Override
                    public void onNext(SavingsWithAssociations savingAccount) {
                        getMvpView().hideProgress();
                        getMvpView().showSavingAccountsDetail(savingAccount);
                    }
                })
        );
    }

    public void filterTransactionList(List<Transactions> savingAccountsTransactionList,
                                      final long startDate , final long lastDate) {
        List<Transactions> list = Observable.from(savingAccountsTransactionList)
                .filter(new Func1<Transactions, Boolean>() {
                    @Override
                    public Boolean call(Transactions transactions) {
                        return startDate <= DateHelper.getDateAsLongFromList(transactions.getDate())
                                && lastDate >= DateHelper.
                                getDateAsLongFromList(transactions.getDate());
                    }
                })
                .toList().toBlocking().single();

        getMvpView().showFilteredList(list);
    }


}
