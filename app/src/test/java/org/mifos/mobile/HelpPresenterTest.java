package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.api.local.PreferencesHelper;
import org.mifos.mobile.models.accounts.loan.LoanAccount;
import org.mifos.mobile.models.accounts.savings.SavingAccount;
import org.mifos.mobile.models.client.Client;
import org.mifos.mobile.models.client.ClientAccounts;
import org.mifos.mobile.presenters.HelpPresenter;
import org.mifos.mobile.presenters.base.BasePresenter;
import org.mifos.mobile.ui.views.HelpView;
import org.mifos.mobile.ui.views.HomeView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class HelpPresenterTest {
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    HelpPresenter view;

    public BasePresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new HelpPresenter(dataManager, context);
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void loadFaq() {
        when(dataManager.getThirdPartyTransferTemplate()).thenReturn(Observable.
                <HelpView>error(new RuntimeException()));
        when(dataManager.getThirdPartyTransferTemplate()).thenReturn(Observable.
                <HelpPresenter>error(new RuntimeException()));
    }
}