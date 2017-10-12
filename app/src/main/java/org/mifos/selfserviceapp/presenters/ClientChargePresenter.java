package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.Charge;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.ClientChargeView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
public class ClientChargePresenter extends BasePresenter<ClientChargeView> {

    private final DataManager dataManager;
    private CompositeSubscription subscription;

    /**
     * Initialises the ClientChargePresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */

    @Inject
    public ClientChargePresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void attachView(ClientChargeView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    public void loadClientCharges(long clientId) {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.getClientCharges(clientId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Page<Charge>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingClientCharges(
                                context.getString(R.string.error_client_charge_loading));
                    }

                    @Override
                    public void onNext(Page<Charge> chargePage) {
                        getMvpView().hideProgress();
                        if (chargePage != null) {
                            getMvpView().showClientCharges(chargePage.getPageItems());
                        }
                    }
                })
        );
    }

    public void loadLoanAccountCharges(long loanId) {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.getLoanCharges(loanId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Charge>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingClientCharges(
                                context.getString(R.string.error_client_charge_loading));
                    }

                    @Override
                    public void onNext(List<Charge> chargeList) {
                        getMvpView().hideProgress();
                        getMvpView().showClientCharges(chargeList);
                    }
                })
        );
    }

    public void loadSavingsAccountCharges(long savingsId) {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.getSavingsCharges(savingsId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Charge>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingClientCharges(
                                context.getString(R.string.error_client_charge_loading));
                    }

                    @Override
                    public void onNext(List<Charge> chargeList) {
                        getMvpView().hideProgress();
                        getMvpView().showClientCharges(chargeList);
                    }
                })
        );
    }

    public void loadClientLocalCharges() {
        checkViewAttached();
        subscription.add(dataManager.getClientLocalCharges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Page<Charge>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorFetchingClientCharges(
                                context.getString(R.string.error_client_charge_loading));
                    }

                    @Override
                    public void onNext(Page<Charge> chargePage) {
                        getMvpView().showClientCharges(chargePage.getPageItems());
                    }
                })
        );
    }
}
