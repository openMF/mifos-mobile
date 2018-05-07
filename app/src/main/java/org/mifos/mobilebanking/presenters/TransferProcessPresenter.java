package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.payload.TransferPayload;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.TransferProcessView;
import org.mifos.mobilebanking.utils.MFErrorParser;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * Created by dilpreet on 1/7/17.
 */

public class TransferProcessPresenter extends BasePresenter<TransferProcessView> {

    public final DataManager dataManager;
    public CompositeDisposable compositeDisposables;

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
    public TransferProcessPresenter(DataManager dataManager,
                                    @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposables = new CompositeDisposable();
    }

    @Override
    public void attachView(TransferProcessView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposables.clear();
    }

    /**
     * Used for making a Transfer with the help of {@code transferPayload} provided in function
     * parameter. It notifies the view after successful making a Transfer. And in case of any error
     * during transfer, it notifies the view.
     * @param transferPayload Contains details about the Transfer
     */
    public void makeSavingsTransfer(TransferPayload transferPayload) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposables.add(dataManager.makeTransfer(transferPayload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onComplete() {

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

    /**
     * Used for making a Third Party Transfer with the help of {@code transferPayload} provided in
     * function parameter. It notifies the view after successful making a Third Party Transfer. And
     * in case of any error during transfer, it notifies the view.
     * @param transferPayload Contains details about the Third Party Transfer
     */
    public void makeTPTTransfer(TransferPayload transferPayload) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposables.add(dataManager.makeThirdPartyTransfer(transferPayload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context.getString(R.string.transfer_error));
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
