package org.mifos.selfserviceapp;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.Transaction;
import org.mifos.selfserviceapp.presenters.RecentTransactionsPresenter;
import org.mifos.selfserviceapp.ui.views.RecentTransactionsView;
import org.mifos.selfserviceapp.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class RecentTransactionsPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    RecentTransactionsView view;

    private RecentTransactionsPresenter presenter;
    private Page<Transaction> recentTransaction;
    private int offset, limit = 50;

    @Before
    public void setUp() {
        presenter = new RecentTransactionsPresenter(dataManager, context);
        presenter.attachView(view);

        recentTransaction = FakeRemoteDataSource.getTransactions();
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

    @Test
    public void testLoadRecentTransactionsZeroOffset() {
        offset = 0;
        when(dataManager.getRecentTransactions(offset, limit)).thenReturn(Observable.just(
                recentTransaction));

        presenter.loadRecentTransactions(false, offset);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showRecentTransactions(recentTransaction.getPageItems());
        verify(view, never()).showErrorFetchingRecentTransactions(context.getString(R.string.
                error_recent_transactions_loading));
    }

    @Test
    public void testLoadRecentTransactionsWithOffset() {
        offset = 10;
        when(dataManager.getRecentTransactions(offset, limit)).thenReturn(Observable.just(
                recentTransaction));

        presenter.loadRecentTransactions(true, offset);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoadMoreRecentTransactions(recentTransaction.getPageItems());
        verify(view, never()).showErrorFetchingRecentTransactions(context.getString(R.string.
                error_recent_transactions_loading));
    }

    @Test
    public void testLoadRecentTransactionsWithZeroTransactions() {
        offset = 0;
        when(dataManager.getRecentTransactions(offset, limit)).thenReturn(Observable.just(
                new Page<Transaction>()));

        presenter.loadRecentTransactions(false, offset);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showEmptyTransaction();
        verify(view, never()).showErrorFetchingRecentTransactions(context.getString(R.string.
                error_recent_transactions_loading));
    }

    @Test
    public void testLoadRecentTransactionsFails() {
        offset = 0;
        when(dataManager.getRecentTransactions(offset, limit)).thenReturn(Observable.
                <Page<Transaction>>error(new RuntimeException()));

        presenter.loadRecentTransactions(false, offset);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showErrorFetchingRecentTransactions(context.getString(R.string.
                error_recent_transactions_loading));
        verify(view, never()).showEmptyTransaction();
    }

}
