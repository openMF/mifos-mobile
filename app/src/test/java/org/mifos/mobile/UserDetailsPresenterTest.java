package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.api.local.PreferencesHelper;
import org.mifos.mobile.models.client.Client;
import org.mifos.mobile.models.notification.NotificationRegisterPayload;
import org.mifos.mobile.presenters.UserDetailsPresenter;
import org.mifos.mobile.ui.views.UserDetailsView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    UserDetailsView view;

    @Mock
    PreferencesHelper preferencesHelper;

    @Mock
    ResponseBody responseBody;

    private UserDetailsPresenter presenter;
    private Client client;

    @Before
    public void setUp() throws Exception {
        presenter = new UserDetailsPresenter(context, dataManager, preferencesHelper);
        client = FakeRemoteDataSource.getCurrentClient();
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testGetUserDetails() {
        when(dataManager.getCurrentClient())
                .thenReturn(Observable.just(client));
        presenter.getUserDetails();
        verify(view).showUserDetails(client);
    }

    @Test
    public void testGetUserDetailsFails() {
        when(dataManager.getCurrentClient())
                .thenReturn(Observable.<Client>error(new Throwable()));
        presenter.getUserDetails();

        verify(view).showError(context
                .getString(R.string.error_client_not_found));
    }

    @Test
    public void testRegisterNotification() {
        String token = "1";
        when(dataManager.registerNotification(any(NotificationRegisterPayload.class)))
                .thenReturn(Observable.just(responseBody));
        presenter.registerNotification(token);

        verify(preferencesHelper).setSentTokenToServer(true);
        verify(preferencesHelper).saveGcmToken(token);
    }
}