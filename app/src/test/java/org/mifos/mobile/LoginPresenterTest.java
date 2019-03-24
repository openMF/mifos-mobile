package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.BaseURL;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.api.local.PreferencesHelper;
import org.mifos.mobile.models.Page;
import org.mifos.mobile.models.User;
import org.mifos.mobile.models.client.Client;
import org.mifos.mobile.presenters.LoginPresenter;
import org.mifos.mobile.ui.views.LoginView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
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
