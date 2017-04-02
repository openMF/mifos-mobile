package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.Transaction;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.RecentTransactionsView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Vishwajeet
 * @since 10/08/16
 */
public class RecentTransactionsPresenter extends BasePresenter<RecentTransactionsView> {

    private final DataManager dataManager;
    private CompositeSubscription subscriptions;

    private int limit = 50;
    private boolean loadmore;

    /**
     * Initialises the RecentTransactionsPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */

    @Inject
    public RecentTransactionsPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(RecentTransactionsView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.unsubscribe();
    }

    public void loadRecentTransactions(boolean loadmore, int offset) {
        this.loadmore = loadmore;
        loadRecentTransactions(offset, limit);
    }

    public void loadRecentTransactions(int offset, int limit) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getRecentTransactions(offset, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Page<Transaction>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingRecentTransactions(
                                context.getString(R.string.error_recent_transactions_loading));
                    }

                    @Override
                    public void onNext(Page<Transaction> transactions) {
                        getMvpView().hideProgress();
                        if (transactions.getTotalFilteredRecords() == 0) {
                            getMvpView().showEmptyTransaction();
                        } else if (loadmore && !transactions.getPageItems().isEmpty()) {
                            getMvpView()
                                    .showLoadMoreRecentTransactions(transactions.getPageItems());
                        } else if (!transactions.getPageItems().isEmpty()) {
                            getMvpView().showRecentTransactions(transactions.getPageItems());
                        } else {
                            getMvpView().showMessage(
                                    context.getString(R.string.no_more_transactions_available));
                        }
                    }
                })
        );
    }
}

