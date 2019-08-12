package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.models.notification.MifosNotification;
import org.mifos.mobile.presenters.NotificationPresenter;
import org.mifos.mobile.ui.views.NotificationView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    NotificationView view;

    private NotificationPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new NotificationPresenter(dataManager, context);
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testLoadNotifications() {
        List<MifosNotification> list = new ArrayList<>();
        when(dataManager.getNotifications())
                .thenReturn(Observable.just(list));
        presenter.loadNotifications();
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showNotifications(list);
    }

    @Test
    public void testLoadNotificationsOnError() {
        when(dataManager.getNotifications())
                .thenReturn(Observable.<List<MifosNotification>>error(new Throwable()));
        presenter.loadNotifications();
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(context
                .getString(R.string.notification));
    }
}