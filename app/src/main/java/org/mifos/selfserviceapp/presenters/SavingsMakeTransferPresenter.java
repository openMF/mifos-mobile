package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.models.payload.SavingsTransferPayload;
import org.mifos.selfserviceapp.models.templates.account.AccountOption;
import org.mifos.selfserviceapp.models.templates.account.AccountOptionsTemplate;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.SavingsMakeTransferMvpView;
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
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 10/03/17.
 */
public class SavingsMakeTransferPresenter extends BasePresenter<SavingsMakeTransferMvpView> {

    public final DataManager dataManager;
    public CompositeSubscription subscriptions;

    @Inject
    public SavingsMakeTransferPresenter(DataManager dataManager,
            @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(SavingsMakeTransferMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.unsubscribe();
    }

    public void loanAccountTransferTemplate() {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getAccountTransferTemplate()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AccountOptionsTemplate>() {
                    @Override
                    public void onCompleted() {
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

    public void makeTransfer(SavingsTransferPayload savingsTransferPayload) {
        checkViewAttached();
        getMvpView().showProgressDialog();
        subscriptions.add(dataManager.makeTransfer(savingsTransferPayload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressDialog();
                        getMvpView().showError(MFErrorParser.errorMessage(e));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getMvpView().hideProgressDialog();
                        getMvpView().showTransferredSuccessfully();
                    }
                })
        );
    }

    public List<String> getAccountNumbers(List<AccountOption> accountOptions) {
        final List<String> accountNumber = new ArrayList<>();
        Observable.from(accountOptions)
                .flatMap(new Func1<AccountOption, Observable<String>>() {
                    @Override
                    public Observable<String> call(AccountOption accountOption) {
                        return Observable.just(accountOption.getAccountNo());
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String accountNo) {
                        accountNumber.add(accountNo);
                    }
                });
        return accountNumber;
    }

    public AccountOption searchAccount(List<AccountOption> accountOptions, final long accountId) {
        final AccountOption[] accountOption = {new AccountOption()};
        Observable.from(accountOptions)
                .filter(new Func1<AccountOption, Boolean>() {
                    @Override
                    public Boolean call(AccountOption accountOption) {
                        return (accountId == accountOption.getAccountId());
                    }
                })
                .subscribe(new Action1<AccountOption>() {
                    @Override
                    public void call(AccountOption account) {
                        accountOption[0] = account;
                    }
                });
        return accountOption[0];
    }
}
