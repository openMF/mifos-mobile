package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.accounts.loan.LoanAccount;
import org.mifos.mobilebanking.models.accounts.savings.SavingAccount;
import org.mifos.mobilebanking.models.client.ClientAccounts;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.AccountOverviewMvpView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Rajan Maurya
 *         On 16/10/17.
 */
public class AccountOverviewPresenter extends BasePresenter<AccountOverviewMvpView> {

    private final DataManager dataManager;
    private CompositeSubscription compositeSubscription;

    @Inject
    public AccountOverviewPresenter(@ApplicationContext Context context,
            DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(AccountOverviewMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.clear();
    }

    /**
     * Fetches Client account details as {@link ClientAccounts} from the server and notifies the
     * view to display the {@link List} of {@link LoanAccount} and {@link SavingAccount}. And in
     * case of any error during fetching the required details it notifies the view.
     */
    public void loadClientAccountDetails() {
        checkViewAttached();
        getMvpView().showProgress();
        compositeSubscription.add(dataManager.getClientAccounts()
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
                        getMvpView().showTotalLoanSavings(
                                getLoanAccountDetails(clientAccounts.getLoanAccounts()),
                                getSavingAccountDetails(clientAccounts.getSavingsAccounts()));
                    }
                })
        );
    }

    /**
     * Returns total Loan balance
     *
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
     *
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
