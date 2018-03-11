package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.models.client.ClientAccounts;
import org.mifos.mobilebanking.presenters.AccountOverviewPresenter;
import org.mifos.mobilebanking.ui.views.AccountOverviewMvpView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Chirag Gupta on 12/06/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class AccountOverviewPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    AccountOverviewMvpView mvpView;

    private AccountOverviewPresenter presenter;
    private ClientAccounts accounts;

    @Before
    public void setUp() throws Exception {
        presenter = new AccountOverviewPresenter(context, dataManager);
        presenter.attachView(mvpView);

        accounts = FakeRemoteDataSource.getClientAccounts();
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testLoadClientAccountDetails() {
        when(dataManager.getClientAccounts()).thenReturn(Observable.just(accounts));

        presenter.loadClientAccountDetails();

        verify(mvpView).showProgress();
        verify(mvpView).hideProgress();
        verify(mvpView).showTotalLoanSavings(
                presenter.getLoanAccountDetails(accounts.getLoanAccounts()),
                presenter.getSavingAccountDetails(accounts.getSavingsAccounts()));
        verify(mvpView, never()).showError(context.getString(R.string.error_fetching_accounts));
    }

    @Test
    public void testLoadClientAccountDetailsFail() {
        when(dataManager.getClientAccounts()).thenReturn(Observable.<ClientAccounts>error(new
                RuntimeException()));

        presenter.loadClientAccountDetails();

        verify(mvpView).showProgress();
        verify(mvpView).hideProgress();
        verify(mvpView, never()).showTotalLoanSavings(
                presenter.getLoanAccountDetails(accounts.getLoanAccounts()),
                presenter.getSavingAccountDetails(accounts.getSavingsAccounts()));
        verify(mvpView).showError(context.getString(R.string.error_fetching_accounts));
    }
}
