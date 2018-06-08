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
import org.mifos.mobilebanking.models.notification.MifosNotification;
import org.mifos.mobilebanking.presenters.NotificationPresenter;
import org.mifos.mobilebanking.ui.views.NotificationView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;

@RunWith(MockitoJUnitRunner.class)
public class NotificationPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    NotificationView notificationView;

    NotificationPresenter notificationPresenter;

    @Before
    public void setUp() throws Exception {
        notificationPresenter = new NotificationPresenter(dataManager, context);
        notificationPresenter.attachView(notificationView);
    }

    @Test
    public void testLoadNotification() throws Exception {
        List<MifosNotification> notifications = FakeRemoteDataSource.getNotifications();

        when(dataManager.getNotifications()).thenReturn(Observable.just(notifications));

        notificationPresenter.loadNotifications();

        verify(notificationView, never()).showError(anyString());
        verify(notificationView).showNotifications(ArgumentMatchers.<MifosNotification>anyList());
    }

    @After
    public void tearDown() throws Exception {
        notificationPresenter.detachView();
    }

}
