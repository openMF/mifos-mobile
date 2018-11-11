package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.BaseURL;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.models.Page;
import org.mifos.mobilebanking.models.User;
import org.mifos.mobilebanking.models.client.Client;
import org.mifos.mobilebanking.presenters.LoginPresenter;
import org.mifos.mobilebanking.ui.views.LoginView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
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
public class LoginPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    PreferencesHelper mockHelper;

    @Mock
    LoginView view;

    private LoginPresenter presenter;
    private User user;
    private Page<Client> clientPage, noClientPage;

    @Before
    public void setUp() throws Exception {

        when(mockHelper.getBaseUrl()).thenReturn(BaseURL.PROTOCOL_HTTPS + BaseURL.API_ENDPOINT);
        when(dataManager.getPreferencesHelper()).thenReturn(mockHelper);

        presenter = new LoginPresenter(dataManager, context);
        presenter.attachView(view);

        user = FakeRemoteDataSource.getUser();
        clientPage = FakeRemoteDataSource.getClients();
        noClientPage = FakeRemoteDataSource.getNoClients();
    }

    @Test
    public void testLogin() throws Exception {
        when(dataManager.login("selfservice", "password")).thenReturn(Observable.just(user));

        presenter.login("selfservice", "password");

        verify(view).showProgress();
        verify(view).onLoginSuccess(user.getUsername());
    }

    @Test
    public void testLoadClients() throws Exception {
        long clientId = clientPage.getPageItems().get(0).getId();
        when(dataManager.getClients()).thenReturn(Observable.just(clientPage));

        presenter.loadClient();

        verify(view).hideProgress();
        verify(view).showPassCodeActivity();
        verify(view, never()).showMessage(context.getString(R.string.error_fetching_client));
    }

    @Test
    public void testLoadNoClients() throws Exception {
        long clientId = clientPage.getPageItems().get(0).getId();
        when(dataManager.getClients()).thenReturn(Observable.just(noClientPage));

        presenter.loadClient();

        verify(view).hideProgress();
        verify(view).showMessage(context.getString(R.string.error_client_not_found));
        verify(view, never()).showPassCodeActivity();
    }

    @Test
    public void testLoadClientFails() throws Exception {
        when(dataManager.getClients()).thenReturn(Observable.<Page<Client>>error(RetrofitUtils.
                get404Exception()));

        presenter.loadClient();

        verify(view).hideProgress();
        verify(view).showMessage(context.getString(R.string.error_fetching_client));
        verify(view, never()).showPassCodeActivity();
    }

    @Test
    public void testLoadClientUnauthorized() throws Exception {
        when(dataManager.getClients()).thenReturn(Observable.<Page<Client>>error(RetrofitUtils.
                get401Exception()));

        presenter.loadClient();

        verify(view).hideProgress();
        verify(view).showMessage(context.getString(R.string.unauthorized_client));
        verify(view, never()).showPassCodeActivity();
    }
    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }
}
