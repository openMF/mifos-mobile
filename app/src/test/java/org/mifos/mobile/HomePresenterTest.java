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
import org.mifos.mobile.presenters.HomePresenter;
import org.mifos.mobile.ui.views.HomeView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HomePresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    HomeView view;

    private HomePresenter presenter;
    private ClientAccounts clientAccounts;
    private Client client;

    @Before
    public void setUp() throws Exception {
            presenter = new HomePresenter(dataManager, context, preferencesHelper);
            presenter.attachView(view);
            clientAccounts = FakeRemoteDataSource.getClientAccounts();
            client = FakeRemoteDataSource.getCurrentClient();
    }

    @After
    public void tearDown() throws Exception {
            presenter.detachView();
    }

    @Test
    public void getUserDetails() {
        when(dataManager.getCurrentClient())
                .thenReturn(Observable.just(client));
        presenter.getUserDetails();
        verify(view).showUserDetails(String.valueOf(client));
    }

    @Test
    public void getUserImage() {
           when(dataManager.getClientImage())
                .thenReturn(Observable.just(client);
        presenter.getUserImage();
        final String encodedString = responseBody.string();
        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(',') + 1);
            verify(preferencesHelper).setUserProfileImage(pureBase64Encoded);
            verify(presenter).setUserProfile(pureBase64Encoded);
        }
    }

    @Test
    public void getUnreadNotificationsCount() {
        when (dataManager.getUnreadNotificationsCount())
                .thenReturn(Observable.just(client));
        presenter.getUnreadNotificationsCount();
        verify(presenter).getUnreadNotificationsCount();
    }

}
