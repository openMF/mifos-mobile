package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.models.client.ClientAccounts;
import org.mifos.mobilebanking.presenters.AccountsPresenter;
import org.mifos.mobilebanking.ui.views.AccountsView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mifos.mobilebanking.utils.Constants;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 26/6/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountsPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    AccountsView accountsView;

    private AccountsPresenter presenter;
    private ClientAccounts accounts, savingsAccount, loanAccounts, shareAccounts;

    @Before
    public void setUp() throws Exception {
        presenter = new AccountsPresenter(context, dataManager);
        presenter.attachView(accountsView);

        accounts = FakeRemoteDataSource.getClientAccounts();
        savingsAccount = FakeRemoteDataSource.getClientSavingsAccount();
        loanAccounts = FakeRemoteDataSource.getClientLoanAccount();
        shareAccounts = FakeRemoteDataSource.getClientShareAccount();

    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testLoadClientAccounts() {
        when(dataManager.getClientAccounts()).thenReturn(Observable.just(accounts));

        presenter.loadClientAccounts();

        verify(accountsView).showProgress();
        verify(accountsView).hideProgress();
        verify(accountsView).showSavingsAccounts(accounts.getSavingsAccounts());
        verify(accountsView).showLoanAccounts(accounts.getLoanAccounts());
        verify(accountsView).showShareAccounts(accounts.getShareAccounts());
        verify(accountsView, never()).showError(context.getString(R.string.
                error_fetching_accounts));

    }

    @Test
    public void testLoadClientAccountsFail() {
        when(dataManager.getClientAccounts()).thenReturn(Observable.<ClientAccounts>error(new
                RuntimeException()));
        presenter.loadClientAccounts();

        verify(accountsView).showProgress();
        verify(accountsView).hideProgress();
        verify(accountsView).showError(context.getString(R.string.
                error_fetching_accounts));

    }

    @Test
    public void testLoadClientSavingsAccount() {
        when(dataManager.getAccounts(Constants.SAVINGS_ACCOUNTS)).
                thenReturn(Observable.just(savingsAccount));

        presenter.loadAccounts(Constants.SAVINGS_ACCOUNTS);

        verify(accountsView).showProgress();
        verify(accountsView).hideProgress();
        verify(accountsView).showSavingsAccounts(savingsAccount.getSavingsAccounts());
        verify(accountsView, never()).showLoanAccounts(null);
        verify(accountsView, never()).showShareAccounts(null);
        verify(accountsView, never()).showError(context.getString(R.string.
                error_fetching_accounts));

    }

    @Test
    public void testLoadClientLoanAccount() {
        when(dataManager.getAccounts(Constants.LOAN_ACCOUNTS)).
                thenReturn(Observable.just(loanAccounts));

        presenter.loadAccounts(Constants.LOAN_ACCOUNTS);

        verify(accountsView).showProgress();
        verify(accountsView).hideProgress();
        verify(accountsView).showLoanAccounts(loanAccounts.getLoanAccounts());
        verify(accountsView, never()).showSavingsAccounts(null);
        verify(accountsView, never()).showShareAccounts(null);
        verify(accountsView, never()).showError(context.getString(R.string.
                error_fetching_accounts));

    }

    @Test
    public void testLoadClientShareAccount() {
        when(dataManager.getAccounts(Constants.SHARE_ACCOUNTS)).
                thenReturn(Observable.just(shareAccounts));

        presenter.loadAccounts(Constants.SHARE_ACCOUNTS);

        verify(accountsView).showProgress();
        verify(accountsView).hideProgress();
        verify(accountsView).showShareAccounts(shareAccounts.getShareAccounts());
        verify(accountsView, never()).showLoanAccounts(null);
        verify(accountsView, never()).showSavingsAccounts(null);
        verify(accountsView, never()).showError(context.getString(R.string.
                error_fetching_accounts));

    }

}
