package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.AccountOptionAndBeneficiary;
import org.mifos.mobilebanking.models.beneficary.Beneficiary;
import org.mifos.mobilebanking.models.templates.account.AccountOption;
import org.mifos.mobilebanking.models.templates.account.AccountOptionsTemplate;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.ThirdPartyTransferView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by dilpreet on 21/6/17.
 */

public class ThirdPartyTransferPresenter extends BasePresenter<ThirdPartyTransferView> {

    private DataManager dataManager;
    private CompositeDisposable compositeDisposable;

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
    public ThirdPartyTransferPresenter(DataManager dataManager,
            @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(ThirdPartyTransferView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    /**
     * Fetches {@link AccountOptionsTemplate} and {@link List} of {@link Beneficiary} from server
     * and notifies the view to display them. And in case of any error during fetching the required
     * details it notifies the view.
     */
    public void loadTransferTemplate() {

        checkViewAttached();
        getMvpView().showProgress();

        compositeDisposable.add(Observable.zip(dataManager.getThirdPartyTransferTemplate(),
                dataManager.getBeneficiaryList(),
                new BiFunction<AccountOptionsTemplate, List<Beneficiary>, AccountOptionAndBeneficiary>()
                {
                    @Override
                    public AccountOptionAndBeneficiary apply(AccountOptionsTemplate
                                                                    accountOptionsTemplate,
                                                            List<Beneficiary> beneficiaries) {
                        return new AccountOptionAndBeneficiary(accountOptionsTemplate,
                                beneficiaries);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<AccountOptionAndBeneficiary>() {
                    @Override
                    public void onComplete() {

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

    /**
     * Retrieving {@link List} of {@code accountNumbers} from {@link List} of {@link AccountOption}
     * @param accountOptions {@link List} of {@link AccountOption}
     * @return Returns {@link List} containing {@code accountNumbers}
     */
    public List<String> getAccountNumbersFromAccountOptions(List<AccountOption> accountOptions) {
        final List<String> accountNumbers = new ArrayList<>();
        Observable.from(accountOptions)
                .flatMap(new Function<AccountOption, Observable<String>>() {
                    @Override
                    public Observable<String> apply(AccountOption accountOption) {
                        return Observable.just(accountOption.getAccountNo());
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        accountNumbers.add(s);

                    }
                });
        return accountNumbers;
    }

    /**
     * Retrieving {@link List} of {@code accountNumbers} from {@link List} of {@link Beneficiary}
     * @param beneficiaries {@link List} of {@link Beneficiary}
     * @return Returns {@link List} containing {@code accountNumbers}
     */
    public List<String> getAccountNumbersFromBeneficiaries(final List<Beneficiary> beneficiaries) {
        final List<String> accountNumbers = new ArrayList<>();
        Observable.from(beneficiaries)
                .flatMap(new Function<Beneficiary, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Beneficiary beneficiary) {
                        return Observable.just(beneficiary.getAccountNumber());
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        accountNumbers.add(s);

                    }
                });
        return accountNumbers;
    }

    /**
     * Searches for a {@link AccountOption} with provided {@code accountNo} from {@link List} of
     * {@link AccountOption} and returns it.
     * @param accountOptions {@link List} of {@link AccountOption}
     * @param accountNo Account Number which needs to searched in {@link List} of
     * {@link AccountOption}
     * @return Returns {@link AccountOption} which has Account Number same as the provided
     * {@code accountNo} in function parameter.
     */
    public AccountOption searchAccount(List<AccountOption> accountOptions, final String accountNo) {
        final AccountOption[] account = {new AccountOption()};
        Observable.from(accountOptions)
                .filter(new Function<AccountOption, Boolean>() {
                    @Override
                    public Boolean apply(AccountOption accountOption) {
                        return accountOption.getAccountNo().equals(accountNo);
                    }
                })
                .subscribe(new Consumer<AccountOption>() {
                    @Override
                    public void accept(AccountOption accountOption) throws Exception {
                        account[0] = accountOption;
                    }
                });
        return account[0];
    }
}
