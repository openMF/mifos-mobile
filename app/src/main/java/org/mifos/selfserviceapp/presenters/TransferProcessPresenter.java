package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.payload.TransferPayload;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.TransferProcessView;
import org.mifos.selfserviceapp.utils.MFErrorParser;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 1/7/17.
 */

public class TransferProcessPresenter extends BasePresenter<TransferProcessView> {

    public final DataManager dataManager;
    public CompositeSubscription subscriptions;

    @Inject
    public TransferProcessPresenter(DataManager dataManager,
                                    @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(TransferProcessView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.clear();
    }

    public void makeSavingsTransfer(TransferPayload transferPayload) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.makeTransfer(transferPayload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(MFErrorParser.errorMessage(e));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        getMvpView().showTransferredSuccessfully();
                    }
                })
        );
    }


    public void makeTPTTransfer(TransferPayload transferPayload) {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.makeThirdPartyTransfer(transferPayload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(MFErrorParser.errorMessage(e));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgress();
                        getMvpView().showTransferredSuccessfully();
                    }
                })
        );
    }


}
