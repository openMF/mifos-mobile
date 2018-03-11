package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.models.register.RegisterPayload;
import org.mifos.mobilebanking.presenters.RegistrationPresenter;
import org.mifos.mobilebanking.ui.views.RegistrationView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Chirag Gupta on 11/29/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistrationPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    RegistrationView view;

    @Mock
    ResponseBody responseBody;

    private RegistrationPresenter presenter;
    private RegisterPayload registerPayload;

    @Before
    public void setUp() throws Exception {
        presenter = new RegistrationPresenter(dataManager, context);
        presenter.attachView(view);

        registerPayload = FakeRemoteDataSource.getRegisterPayload();
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testRegisterUser() throws Exception {
        when(dataManager.registerUser(registerPayload)).thenReturn(Observable.just(responseBody));

        presenter.registerUser(registerPayload);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showRegisteredSuccessfully();
        verify(view, never()).showError("");
    }

    @Test
    public void testRegisterUserFails() throws Exception {
        when(dataManager.registerUser(registerPayload)).thenReturn(Observable.
                <ResponseBody>error(new RuntimeException()));

        presenter.registerUser(registerPayload);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view, never()).showRegisteredSuccessfully();
        verify(view).showError("");
    }
}



