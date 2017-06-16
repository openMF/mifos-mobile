package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.BeneficiariesView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 14/6/17.
 */

public class BeneficiaryListPresenter extends BasePresenter<BeneficiariesView> {

    private DataManager dataManager;
    private CompositeSubscription subscription;

    @Inject
    public BeneficiaryListPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    public void loadBeneficiaries() {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.getBeneficiaryList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Beneficiary>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context
                                .getString(R.string.error_fetching_beneficiaries));
                    }

                    @Override
                    public void onNext(List<Beneficiary> beneficiaries) {
                        getMvpView().hideProgress();
                        getMvpView().showBeneficiaryList(beneficiaries);
                    }
                }));
    }
}
