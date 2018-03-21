package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.payload.AccountDetail;
import org.mifos.mobilebanking.models.templates.account.AccountOption;
import org.mifos.mobilebanking.models.templates.account.AccountOptionsTemplate;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.SavingsMakeTransferMvpView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Rajan Maurya on 10/03/17.
 */
public class SavingsMakeTransferPresenter extends BasePresenter<SavingsMakeTransferMvpView> {

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
    public SavingsMakeTransferPresenter(DataManager dataManager,
            @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        compositeDisposables = new CompositeDisposable();
    }

    @Override
    public void attachView(SavingsMakeTransferMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposables.clear();
    }

    /**
     * Fetches {@link AccountOptionsTemplate} from server and notifies the view to display it. And
     * in case of any error during fetching the required details it notifies the view.
     */
    public void loanAccountTransferTemplate() {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposables.add(dataManager.getAccountTransferTemplate()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<AccountOptionsTemplate>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showError(context.getString(
                                R.string.error_fetching_account_transfer_template));
                    }

                    @Override
                    public void onNext(AccountOptionsTemplate accountOptionsTemplate) {
                        getMvpView().hideProgress();
                        getMvpView().showSavingsAccountTemplate(accountOptionsTemplate);
                    }
                })
        );
    }

    /**
     * Retrieving {@link List} of {@code accountNo} from {@link List} of {@link AccountOption}
     * @param accountOptions {@link List} of {@link AccountOption}
     * @return Returns {@link List} containing {@code accountNo}
     */
    public List<AccountDetail> getAccountNumbers(List<AccountOption> accountOptions,
                                                 final boolean isTypePayFrom) {
        final List<AccountDetail> accountNumber = new ArrayList<>();
        Observable.fromIterable(accountOptions)
                .filter(new Predicate<AccountOption>() {
                    @Override
                    public boolean test(AccountOption accountOption) throws Exception {
                        return !(accountOption.getAccountType().getCode().equals(context.
                                getString(R.string.account_type_loan))
                                && isTypePayFrom);
                    }
                })
                .flatMap(new Function<AccountOption, Observable<AccountDetail>>() {
                    @Override
                    public Observable<AccountDetail> apply(AccountOption accountOption) {
                        return Observable.just(new AccountDetail(accountOption.getAccountNo(),
                                accountOption.getAccountType().getValue()));
                    }
                })
                .subscribe(new Consumer<AccountDetail>() {
                    @Override
                    public void accept(AccountDetail accountDetail) throws Exception {
                        accountNumber.add(accountDetail);

                    }
                });
        return accountNumber;
    }

    /**
     * Searches for a {@link AccountOption} with provided {@code accountId} from {@link List} of
     * {@link AccountOption} and returns it.
     * @param accountOptions {@link List} of {@link AccountOption}
     * @param accountId AccountId which needs to searched in {@link List} of {@link AccountOption}
     * @return Returns {@link AccountOption} which has accountId same as the provided
     * {@code accountId} in function parameter.
     */
    public AccountOption searchAccount(List<AccountOption> accountOptions, final long accountId) {
        final AccountOption[] accountOption = {new AccountOption()};
        Observable.fromIterable(accountOptions)
                .filter(new Predicate<AccountOption>() {
                    @Override
                    public boolean test(AccountOption accountOption) {
                        return (accountId == accountOption.getAccountId());
                    }
                })
                .subscribe(new Consumer<AccountOption>() {
                    @Override
                    public void accept(AccountOption account) throws Exception {
                        accountOption[0] = account;

                    }
                });
        return accountOption[0];
    }
}
