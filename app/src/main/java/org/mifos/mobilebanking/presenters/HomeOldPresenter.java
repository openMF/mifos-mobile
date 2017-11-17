package org.mifos.mobilebanking.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.injection.ActivityContext;
import org.mifos.mobilebanking.models.accounts.loan.LoanAccount;
import org.mifos.mobilebanking.models.accounts.savings.SavingAccount;
import org.mifos.mobilebanking.models.client.Client;
import org.mifos.mobilebanking.models.client.ClientAccounts;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.HomeOldView;
import org.mifos.mobilebanking.utils.ImageUtil;

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

public class HomeOldPresenter extends BasePresenter<HomeOldView> {

    private DataManager dataManager;
    private CompositeSubscription subscription;
    @Inject
    PreferencesHelper preferencesHelper;

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
    public HomeOldPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void attachView(HomeOldView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    /**
     * Fetches Client account details as {@link ClientAccounts} from the server and notifies the
     * view to display the {@link List} of {@link LoanAccount} and {@link SavingAccount}. And in
     * case of any error during fetching the required details it notifies the view.
     */
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

    /**
     * Fetches Details about Client from the server as {@link Client} and notifies the view to
     * display the details. And in case of any error during fetching the required details it
     * notifies the view.
     */
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

    /**
     * Fetches Client image from the server in {@link Base64} format which is then decoded into a
     * {@link Bitmap} after which the view notified to display it.
     */
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

    /**
     * Returns total Loan balance
     * @param loanAccountList {@link List} of {@link LoanAccount} associated with the client
     * @return Returns {@code totalAmount} which is calculated by adding all {@link LoanAccount}
     * balance.
     */
    private double getLoanAccountDetails(List<LoanAccount> loanAccountList) {
        double totalAmount = 0;
        for (LoanAccount loanAccount : loanAccountList) {
            totalAmount += loanAccount.getLoanBalance();
        }
        return totalAmount;
    }

    /**
     * Returns total Savings balance
     * @param savingAccountList {@link List} of {@link SavingAccount} associated with the client
     * @return Returns {@code totalAmount} which is calculated by adding all {@link SavingAccount}
     * balance.
     */
    private double getSavingAccountDetails(List<SavingAccount> savingAccountList) {
        double totalAmount = 0;
        for (SavingAccount savingAccount : savingAccountList) {
            totalAmount += savingAccount.getAccountBalance();
        }
        return totalAmount;
    }

}