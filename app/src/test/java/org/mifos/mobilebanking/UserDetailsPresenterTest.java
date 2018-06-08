package org.mifos.mobilebanking;

/*
 * Created by saksham on 08/June/2018
 */

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.models.client.Client;
import org.mifos.mobilebanking.models.notification.NotificationRegisterPayload;
import org.mifos.mobilebanking.presenters.UserDetailsPresenter;
import org.mifos.mobilebanking.ui.views.UserDetailsView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsPresenterTest {

    private final String MOCK_RESPONSE_BODY = "mock_response_body";

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    UserDetailsView userDetailsView;

    @Mock
    PreferencesHelper preferencesHelper;

    UserDetailsPresenter userDetailsPresenter;

    @Before
    public void setUp() throws Exception {
        when(dataManager.getPreferencesHelper()).thenReturn(preferencesHelper);

        userDetailsPresenter = spy(new UserDetailsPresenter(context, dataManager));
        userDetailsPresenter.attachView(userDetailsView);
    }

    @Test
    public void testGetUserDetails() throws Exception {
        Client client = FakeRemoteDataSource.getClients().getPageItems().get(0);

        when(dataManager.getCurrentClient()).thenReturn(Observable.just(client));

        userDetailsPresenter.getUserDetails();

        verify(userDetailsView, times(1)).showUserDetails(client);
    }

    @Test
    public void testGetUserImage() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("text/plain"), MOCK_RESPONSE_BODY);

        when(dataManager.getClientImage()).thenReturn(Observable.just(responseBody));
        doNothing().when(userDetailsPresenter).setUserProfile(anyString());

        userDetailsPresenter.getUserImage();
    }

    @Test
    public void testRegisterNotification() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("text/plain"), MOCK_RESPONSE_BODY);
        when(dataManager.registerNotification((NotificationRegisterPayload) any()))
                .thenReturn(Observable.just(responseBody));

        userDetailsPresenter.registerNotification("token");

        verify(preferencesHelper, times(1)).setSentTokenToServer(anyBoolean());
        verify(preferencesHelper, times(1)).saveGcmToken(anyString());
    }

    @After
    public void tearDown() throws Exception {
        userDetailsPresenter.detachView();
    }
}
