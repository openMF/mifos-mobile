package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.beneficary.BeneficiaryPayload;
import org.mifos.mobilebanking.models.beneficary.BeneficiaryUpdatePayload;
import org.mifos.mobilebanking.models.templates.beneficiary.BeneficiaryTemplate;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.BeneficiaryApplicationView;

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

    /**
     * Initialises the LoginPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */
    @Inject
    public BeneficiaryApplicationPresenter(DataManager dataManager,
                                           @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    /**
     * Loads BeneficiaryTemplate from the server and notifies the view to display it. And in case of
     * any error during fetching the required details it notifies the view.
     */
    public void loadBeneficiaryTemplate() {
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

    /**
     * Used to create a Beneficiary and notifies the view after successful creation of Beneficiary.
     * And in case of any error during creation, it notifies the view.
     * @param payload {@link BeneficiaryPayload} used for creating a Beneficiary.
     */
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

    /**
     * Update a Beneficiary with provided {@code beneficiaryId} and notifies the view after
     * successful updation of Beneficiary. And in case of any error during updation, it notifies the
     * view.
     * @param beneficiaryId Id of Beneficiary which you want to update
     * @param payload {@link BeneficiaryPayload} used for updation a Beneficiary.
     */
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
