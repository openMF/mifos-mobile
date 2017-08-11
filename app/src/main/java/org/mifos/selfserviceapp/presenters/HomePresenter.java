package org.mifos.selfserviceapp.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.client.ClientAccounts;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.HomeView;
import org.mifos.selfserviceapp.utils.ImageUtil;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 19/6/17.
 */

public class HomePresenter extends BasePresenter<HomeView> {

    private DataManager dataManager;
    private CompositeSubscription subscription;
    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    public HomePresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void attachView(HomeView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    public void loadClientAccountDetails() {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.getClientAccounts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ClientAccounts>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context.getString(R.string.error_fetching_accounts));
                    }

                    @Override
                    public void onNext(ClientAccounts clientAccounts) {
                        getMvpView().hideProgress();
                        getMvpView().showLoanAccountDetails(getLoanAccountDetails(clientAccounts
                                .getLoanAccounts()));
                        getMvpView().showSavingAccountDetails(getSavingAccountDetails(clientAccounts
                                .getSavingsAccounts()));
                    }
                })
        );
    }

    public void getUserDetails() {
        checkViewAttached();
        subscription.add(dataManager.getCurrentClient()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Client>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(context.getString(R.string.error_fetching_client));
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onNext(Client client) {
                        if (client != null) {
                            preferencesHelper.setOfficeName(client.getOfficeName());
                            getMvpView().showUserDetails(client);
                        } else {
                            getMvpView().showError(context
                                    .getString(R.string.error_client_not_found));
                        }
                    }
                })
        );
    }

    public void getUserImage() {
        checkViewAttached();
        subscription.add(dataManager.getClientImage()
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        try {

                            final String encodedString = response.string();

                            final String pureBase64Encoded =
                                    encodedString.substring(encodedString.indexOf(',') + 1);

                            final byte[] decodedBytes =
                                    Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                            Bitmap decodedBitmap = ImageUtil.getInstance().
                                    compressImage(decodedBytes, 256, 256);
                            getMvpView().showUserImage(decodedBitmap);
                        } catch (IOException e) {
                            Log.d("userimage", e.toString());
                        }
                    }
                })
        );
    }

    private double getLoanAccountDetails(List<LoanAccount> loanAccountList) {
        double totalAmount = 0;
        for (LoanAccount loanAccount : loanAccountList) {
            totalAmount += loanAccount.getLoanBalance();
        }
        return totalAmount;
    }

    private double getSavingAccountDetails(List<SavingAccount> savingAccountList) {
        double totalAmount = 0;
        for (SavingAccount savingAccount : savingAccountList) {
            totalAmount += savingAccount.getAccountBalance();
        }
        return totalAmount;
    }

}
