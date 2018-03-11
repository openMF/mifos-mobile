package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.models.register.UserVerify;
import org.mifos.mobilebanking.presenters.RegistrationVerificationPresenter;
import org.mifos.mobilebanking.ui.views.RegistrationVerificationView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Sean Kelly on 9/12/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class RegistrationVerificationPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    RegistrationVerificationView view;

    @Mock
    ResponseBody responseBody;

    private RegistrationVerificationPresenter presenter;
    private UserVerify userVerify;

    @Before
    public void setUp() throws Exception {
        presenter = new RegistrationVerificationPresenter(dataManager, context);
        presenter.attachView(view);

        userVerify = FakeRemoteDataSource.getUserVerify();
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testVerifyUser() throws Exception {
        when(dataManager.verifyUser(userVerify)).thenReturn(Observable.just(responseBody));

        presenter.verifyUser(userVerify);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showVerifiedSuccessfully();
        verify(view, never()).showError("");
    }

    @Test
    public void testVerifyUserFails() throws Exception {
        when(dataManager.verifyUser(userVerify)).thenReturn(Observable.<ResponseBody>error(new
                RuntimeException()));

        presenter.verifyUser(userVerify);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view, never()).showVerifiedSuccessfully();
        verify(view).showError("");
    }

}
