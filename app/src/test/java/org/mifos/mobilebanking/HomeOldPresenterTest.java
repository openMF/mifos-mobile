package org.mifos.mobilebanking;

/*
 * Created by saksham on 07/June/2018
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
import org.mifos.mobilebanking.models.client.ClientAccounts;
import org.mifos.mobilebanking.presenters.HomeOldPresenter;
import org.mifos.mobilebanking.ui.views.HomeOldView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class HomeOldPresenterTest {

    public static final int UNREAD_NOTIFICATION = 10;
    public static final String MOCK_RESPONSE_BODY = "mock response body";

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    PreferencesHelper preferencesHelper;

    @Mock
    HomeOldView homeOldView;

    HomeOldPresenter homeOldPresenter;

    @Before
    public void setUp() throws Exception {
        when(dataManager.getPreferencesHelper()).thenReturn(preferencesHelper);

        homeOldPresenter = spy(new HomeOldPresenter(dataManager, context));
        homeOldPresenter.attachView(homeOldView);
    }

    @Test
    public void testLoadClientAccountDetails() throws Exception {
        ClientAccounts clientAccounts = FakeRemoteDataSource.getClientAccounts();
        when(dataManager.getClientAccounts()).thenReturn(Observable.just(clientAccounts));

        homeOldPresenter.loadClientAccountDetails();

        verify(homeOldView, never()).showError(anyString());
        verify(homeOldView, times(1)).showSavingAccountDetails(anyDouble());
        verify(homeOldView, times(1)).showLoanAccountDetails(anyDouble());
    }

    @Test
    public void testGetUserDetails() throws Exception {
        Client client = FakeRemoteDataSource.getClients().getPageItems().get(0);
        when(dataManager.getCurrentClient()).thenReturn(Observable.just(client));

        homeOldPresenter.getUserDetails();

        verify(homeOldView, times(1)).showUserDetails(client);
        verify(homeOldView, never()).showError(anyString());
    }

    @Test
    public void testGetUserImage() throws Exception {
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("text/plain"),
                MOCK_RESPONSE_BODY);
        when(dataManager.getClientImage()).thenReturn(Observable.just(responseBody));
        doNothing().when(homeOldPresenter).setUserProfile(anyString());

        homeOldPresenter.getUserImage();
    }

    @Test
    public void testGetUnreadNotificationsCount() throws Exception {
        when(dataManager.getUnreadNotificationsCount())
                .thenReturn(Observable.just(UNREAD_NOTIFICATION));

        homeOldPresenter.getUnreadNotificationsCount();

        verify(homeOldView).showNotificationCount(Mockito.anyInt());
    }

    @After
    public void tearDown() throws Exception  {
        homeOldPresenter.detachView();
    }
}