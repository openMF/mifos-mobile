package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.AccountOptionAndBeneficiary;
import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.models.payload.TransferPayload;
import org.mifos.selfserviceapp.models.templates.account.AccountOption;
import org.mifos.selfserviceapp.models.templates.account.AccountOptionsTemplate;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.ThirdPartyTransferView;
import org.mifos.selfserviceapp.utils.MFErrorParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 21/6/17.
 */

public class ThirdPartyTransferPresenter extends BasePresenter<ThirdPartyTransferView> {

    private DataManager dataManager;
    private CompositeSubscription subscription;

    @Inject
    public ThirdPartyTransferPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void attachView(ThirdPartyTransferView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    public void loadTransferTemplate() {

        checkViewAttached();
        getMvpView().showProgress();

        subscription.add(Observable.zip(dataManager.getThirdPartyTransferTemplate(),
                dataManager.getBeneficiaryList(),
                new Func2<AccountOptionsTemplate, List<Beneficiary>, AccountOptionAndBeneficiary>()
                {
                    @Override
                    public AccountOptionAndBeneficiary call(AccountOptionsTemplate
                                                                    accountOptionsTemplate,
                                                            List<Beneficiary> beneficiaries) {
                        return new AccountOptionAndBeneficiary(accountOptionsTemplate,
                                beneficiaries);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AccountOptionAndBeneficiary>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context.getString(
                                R.string.error_fetching_third_party_transfer_template));
                    }

                    @Override
                    public void onNext(AccountOptionAndBeneficiary accountOptionAndBeneficiary) {
                        getMvpView().hideProgress();
                        getMvpView().showThirdPartyTransferTemplate(accountOptionAndBeneficiary.
                                getAccountOptionsTemplate());
                        getMvpView().showBeneficiaryList(accountOptionAndBeneficiary.
                                getBeneficiaryList());
                    }
                }));
    }

    public void makeTransfer(TransferPayload transferPayload) {
        checkViewAttached();
        getMvpView().showProgress();
        subscription.add(dataManager.makeThirdPartyTransfer(transferPayload)
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

    public List<String> getAccountNumbersFromAccountOptions(List<AccountOption> accountOptions) {
        final List<String> accountNumbers = new ArrayList<>();
        Observable.from(accountOptions)
                .flatMap(new Func1<AccountOption, Observable<String>>() {
                    @Override
                    public Observable<String> call(AccountOption accountOption) {
                        return Observable.just(accountOption.getAccountNo());
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        accountNumbers.add(s);
                    }
                });
        return accountNumbers;
    }

    public List<String> getAccountNumbersFromBeneficiaries(final List<Beneficiary> beneficiaries) {
        final List<String> accountNumbers = new ArrayList<>();
        Observable.from(beneficiaries)
                .flatMap(new Func1<Beneficiary, Observable<String>>() {
                    @Override
                    public Observable<String> call(Beneficiary beneficiary) {
                        return Observable.just(beneficiary.getAccountNumber());
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        accountNumbers.add(s);
                    }
                });
        return accountNumbers;
    }

    public AccountOption searchAccount(List<AccountOption> accountOptions, final String accountNo) {
        final AccountOption[] account = {new AccountOption()};
        Observable.from(accountOptions)
                .filter(new Func1<AccountOption, Boolean>() {
                    @Override
                    public Boolean call(AccountOption accountOption) {
                        return accountOption.getAccountNo().equals(accountNo);
                    }
                })
                .subscribe(new Action1<AccountOption>() {
                    @Override
                    public void call(AccountOption accountOption) {
                        account[0] = accountOption;
                    }
                });
        return account[0];
    }
}
