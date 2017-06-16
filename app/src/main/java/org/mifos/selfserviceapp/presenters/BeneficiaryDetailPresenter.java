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
