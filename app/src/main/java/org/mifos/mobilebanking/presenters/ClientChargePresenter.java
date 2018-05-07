package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ActivityContext;
import org.mifos.mobilebanking.models.Charge;
import org.mifos.mobilebanking.models.Page;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.ClientChargeView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
public class ClientChargePresenter extends BasePresenter<ClientChargeView> {

    private final DataManager dataManager;
    private CompositeDisposable compositeDisposable;

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
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(ClientChargeView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public void loadClientCharges(long clientId) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(dataManager.getClientCharges(clientId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Page<Charge>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingClientCharges(
                                context.getString(R.string.client_charges));
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
        compositeDisposable.add(dataManager.getLoanCharges(loanId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<Charge>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingClientCharges(
                                context.getString(R.string.client_charges));
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
        compositeDisposable.add(dataManager.getSavingsCharges(savingsId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<Charge>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showErrorFetchingClientCharges(
                                context.getString(R.string.client_charges));
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
        compositeDisposable.add(dataManager.getClientLocalCharges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Page<Charge>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorFetchingClientCharges(
                                context.getString(R.string.client_charges));
                    }

                    @Override
                    public void onNext(Page<Charge> chargePage) {
                        getMvpView().showClientCharges(chargePage.getPageItems());
                    }
                })
        );
    }
}
