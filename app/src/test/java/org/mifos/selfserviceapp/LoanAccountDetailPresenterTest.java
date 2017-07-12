package org.mifos.selfserviceapp;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.presenters.LoanAccountsDetailPresenter;
import org.mifos.selfserviceapp.ui.views.LoanAccountsDetailView;
import org.mifos.selfserviceapp.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 27/6/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoanAccountDetailPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    LoanAccountsDetailView view;

    private LoanAccountsDetailPresenter presenter;
    private LoanAccount loanAccount;

    @Before
    public void setUp() throws Exception {
        presenter = new LoanAccountsDetailPresenter(dataManager, context);
        presenter.attachView(view);

        loanAccount = FakeRemoteDataSource.getLoanAccount();
    }

    @Test
    public void testLoadLoanAccountDetails() throws Exception {
        when(dataManager.getLoanAccountDetails(29)).thenReturn(Observable.just(loanAccount));

        presenter.loadLoanAccountDetails(29);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoanAccountsDetail(loanAccount);
        verify(view, never()).showErrorFetchingLoanAccountsDetail(context.
                getString(R.string.error_loan_account_details_loading));
    }

    @Test
    public void testLoadLoanAccountDetailsFails() throws Exception {
        when(dataManager.getLoanAccountDetails(29)).thenReturn(Observable.<LoanAccount>error(new
                RuntimeException()));

        presenter.loadLoanAccountDetails(29);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showErrorFetchingLoanAccountsDetail(context.
                getString(R.string.error_loan_account_details_loading));
        verify(view, never()).showLoanAccountsDetail(null);
    }
}
