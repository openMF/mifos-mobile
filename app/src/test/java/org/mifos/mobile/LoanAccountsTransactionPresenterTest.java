package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations;
import org.mifos.mobile.presenters.LoanAccountsTransactionPresenter;
import org.mifos.mobile.ui.views.LoanAccountsTransactionView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mifos.mobile.utils.Constants;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 27/6/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoanAccountsTransactionPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    LoanAccountsTransactionView view;

    private LoanAccountsTransactionPresenter presenter;
    private LoanWithAssociations loanWithTransaction, loanWithEmptyTransactions;

    @Before
    public void setUp() throws Exception {
        presenter = new LoanAccountsTransactionPresenter(dataManager, context);
        presenter.attachView(view);

        loanWithTransaction = FakeRemoteDataSource.getLoanAccountWithTransaction();
        loanWithEmptyTransactions = FakeRemoteDataSource.getLoanAccountWithEmptyTransaction();
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testLoadLoanAccountDetails() throws Exception {
        when(dataManager.getLoanWithAssociations(Constants.TRANSACTIONS, 29)).
                thenReturn(Observable.just(loanWithTransaction));

        presenter.loadLoanAccountDetails(29);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoanTransactions(loanWithTransaction);
        verify(view, never()).showEmptyTransactions(null);
        verify(view, never()).showErrorFetchingLoanAccountsDetail(context.
                getString(R.string.error_loan_account_details_loading));

    }

    @Test
    public void testLoadLoanAccountDetailsEmpty() throws Exception {
        when(dataManager.getLoanWithAssociations(Constants.TRANSACTIONS, 29)).
                thenReturn(Observable.just(loanWithEmptyTransactions));

        presenter.loadLoanAccountDetails(29);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showEmptyTransactions(loanWithEmptyTransactions);
        verify(view, never()).showLoanTransactions(null);
        verify(view, never()).showErrorFetchingLoanAccountsDetail(context.
                getString(R.string.error_loan_account_details_loading));

    }


    @Test
    public void testLoadLoanAccountDetailsFails() throws Exception {
        when(dataManager.getLoanWithAssociations(Constants.TRANSACTIONS, 29)).
                thenReturn(Observable.<LoanWithAssociations>error(new RuntimeException()));

        presenter.loadLoanAccountDetails(29);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showErrorFetchingLoanAccountsDetail(context.
                getString(R.string.error_loan_account_details_loading));
        verify(view, never()).showEmptyTransactions(null);
        verify(view, never()).showLoanTransactions(null);

    }
}
