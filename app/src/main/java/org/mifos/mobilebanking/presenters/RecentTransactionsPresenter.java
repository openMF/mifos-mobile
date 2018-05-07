package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.Page;
import org.mifos.mobilebanking.models.Transaction;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.RecentTransactionsView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Vishwajeet
 * @since 10/08/16
 */
public class RecentTransactionsPresenter extends BasePresenter<RecentTransactionsView> {

    private final DataManager dataManager;
    private CompositeDisposable compositeDisposables;

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
    public RecentTransactionsPresenter(DataManager dataManager,
            @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposables = new CompositeDisposable();
    }

    @Override
    public void attachView(RecentTransactionsView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposables.clear();
    }

    /**
     * Used to call function {@code loadRecentTransactions(int offset, int limit)} which is used for
     * fetching RecentTransaction from server.
     * @param loadmore Set {@code false} if calling First time and {@code true} if you need to fetch
     *                 more {@link Transaction}
     * @param offset Set {@code 0} if calling first time or set length of {@code totalItemsCount} if
     *               you need to fetch more {@link Transaction}
     */
    public void loadRecentTransactions(boolean loadmore, int offset) {
        this.loadmore = loadmore;
        loadRecentTransactions(offset, limit);
    }

    /**
     * Used to load List of {@link Transaction} from server depending upon the {@code offset} and
     * the max {@code limit} and notifies the view to display it. And in case of any
     * error during fetching the required details it notifies the view.
     * @param offset Starting position for fetching the list of {@link Transaction}
     * @param limit Maximum size of List of {@link Transaction} which is fetched from server
     */
    public void loadRecentTransactions(int offset, int limit) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposables.add(dataManager.getRecentTransactions(offset, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Page<Transaction>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingRecentTransactions(
                                context.getString(R.string.recent_transactions));
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
