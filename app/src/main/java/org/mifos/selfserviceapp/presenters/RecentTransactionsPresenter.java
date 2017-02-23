package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.TransactionsListResponse;
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

    public void loadRecentTransactions(long clientId) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getRecentTransactions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TransactionsListResponse>() {
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
                    public void onNext(TransactionsListResponse transactionsListResponse) {
                        getMvpView().hideProgress();
                        if (transactionsListResponse != null) {
                            getMvpView().showRecentTransactions(
                                    transactionsListResponse.getPageItems());
                        }
                    }
                })
        );
    }
}

