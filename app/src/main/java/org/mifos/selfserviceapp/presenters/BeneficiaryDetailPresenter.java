package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.BeneficiaryDetailView;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 16/6/17.
 */

public class BeneficiaryDetailPresenter extends BasePresenter<BeneficiaryDetailView> {

    private DataManager manager;
    private CompositeSubscription subscription;

    /**
     * Initialises the LoginPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param manager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */
    @Inject
    public BeneficiaryDetailPresenter(DataManager manager, @ActivityContext Context context) {
        super(context);
        this.manager = manager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    /**
     * Used to delete a Beneficiary with given {@code beneficiaryId} and notifies the view after
     * successful deletion of a beneficiary. And in case of any error during deletion, it notifies
     * the view.
     * @param beneficiaryId Id of Beneficiary which you want to delete.
     */
    public void deleteBeneficiary(long beneficiaryId) {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(manager.deleteBeneficiary(beneficiaryId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context.
                                getString(R.string.error_deleting_beneficiary));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        getMvpView().showBeneficiaryDeletedSuccessfully();
                    }
                }));
    }
}
