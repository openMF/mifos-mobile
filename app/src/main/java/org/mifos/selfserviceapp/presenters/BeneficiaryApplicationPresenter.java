package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.beneficary.BeneficiaryPayload;
import org.mifos.selfserviceapp.models.beneficary.BeneficiaryUpdatePayload;
import org.mifos.selfserviceapp.models.templates.beneficiary.BeneficiaryTemplate;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.BeneficiaryApplicationView;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 16/6/17.
 */

public class BeneficiaryApplicationPresenter extends BasePresenter<BeneficiaryApplicationView> {

    private DataManager dataManager;
    private CompositeSubscription subscription;

    @Inject
    public BeneficiaryApplicationPresenter(DataManager dataManager,
                                           @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    public void showBeneficiaryTemplate() {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.getBeneficiaryTemplate()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BeneficiaryTemplate>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context
                                .getString(R.string.error_fetching_beneficiary_template));
                    }

                    @Override
                    public void onNext(BeneficiaryTemplate beneficiaryTemplate) {
                        getMvpView().hideProgress();
                        getMvpView().showBeneficiaryTemplate(beneficiaryTemplate);
                    }
                }));
    }

    public void createBeneficiary(BeneficiaryPayload payload) {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.createBeneficiary(payload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context
                                .getString(R.string.error_creating_beneficiary));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        getMvpView().showBeneficiaryCreatedSuccessfully();
                    }
                }));
    }

    public void updateBeneficiary(long beneficiaryId, BeneficiaryUpdatePayload payload) {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.updateBeneficiary(beneficiaryId, payload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context
                                .getString(R.string.error_updating_beneficiary));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        getMvpView().showBeneficiaryUpdatedSuccessfully();
                    }
                }));
    }
}
