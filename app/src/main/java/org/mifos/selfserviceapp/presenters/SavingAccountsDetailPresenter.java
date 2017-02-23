package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.SavingAccountsDetailView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Vishwajeet
 * @since 18/8/16.
 */

public class SavingAccountsDetailPresenter extends BasePresenter<SavingAccountsDetailView> {

    private final DataManager dataManager;
    private CompositeSubscription subscriptions;

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
    public SavingAccountsDetailPresenter(DataManager dataManager,
            @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(SavingAccountsDetailView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.unsubscribe();
    }

    /**
     * Load details of a particular saving account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     */
    public void loadSavingAccountDetails(long accountId) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getSavingAccountDetails(accountId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SavingAccount>() {
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
                    public void onNext(SavingAccount savingAccount) {
                        getMvpView().hideProgress();
                        if (savingAccount != null) {
                            getMvpView().showSavingAccountsDetail(savingAccount);
                        }
                    }
                })
        );
    }
}
