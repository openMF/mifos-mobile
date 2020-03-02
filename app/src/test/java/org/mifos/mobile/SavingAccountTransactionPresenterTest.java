package org.mifos.mobile;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobile.presenters.SavingAccountsTransactionPresenter;
import org.mifos.mobile.ui.views.SavingAccountsTransactionView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mifos.mobile.utils.Constants;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class SavingAccountTransactionPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    SavingAccountsTransactionView view;

    private SavingsWithAssociations savingsWithAssociations;
    private SavingAccountsTransactionPresenter presenter;

    @Before
    public void setUp() {

        presenter = new SavingAccountsTransactionPresenter(dataManager, context);
        presenter.attachView(view);

        savingsWithAssociations = FakeRemoteDataSource.getSavingsWithAssociations();
    }

    @Test
    public void testLoadSavingsWithAssociations() {
        when(dataManager.getSavingsWithAssociations(1, Constants.TRANSACTIONS)).thenReturn(
                Observable.just(savingsWithAssociations));

        presenter.loadSavingsWithAssociations(1);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showSavingAccountsDetail(savingsWithAssociations);
        verify(view, never()).showErrorFetchingSavingAccountsDetail(context.getString(R.string.
                error_saving_account_details_loading));
    }

    @Test
    public void testLoadSavingsWithAssociationsFails() {
        when(dataManager.getSavingsWithAssociations(1, Constants.TRANSACTIONS)).thenReturn(
                Observable.<SavingsWithAssociations>error(new RuntimeException()));

        presenter.loadSavingsWithAssociations(1);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showErrorFetchingSavingAccountsDetail(context.getString(R.string.
                error_saving_account_details_loading));
        verify(view, never()).showSavingAccountsDetail(savingsWithAssociations);
    }

}
