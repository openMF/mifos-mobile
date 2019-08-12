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
import org.mifos.mobile.models.UpdatePasswordPayload;
import org.mifos.mobile.presenters.UpdatePasswordPresenter;
import org.mifos.mobile.ui.views.UpdatePasswordView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mifos.mobile.utils.MFErrorParser;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import okhttp3.Credentials;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UpdatePasswordPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    UpdatePasswordView view;

    @Mock
    PreferencesHelper preferencesHelper;

    @Mock
    ResponseBody responseBody;

    private UpdatePasswordPayload passwordPayload;
    private UpdatePasswordPresenter presenter;

    @Before
    public void setUp() throws Exception {
        when(preferencesHelper.getBaseUrl())
                .thenReturn(BaseURL.PROTOCOL_HTTPS + BaseURL.API_ENDPOINT);
        presenter = new UpdatePasswordPresenter(context, dataManager, preferencesHelper);
        passwordPayload = FakeRemoteDataSource.getUpdatePasswordPayload();
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void updateAccountPassword() {
        when(dataManager.updateAccountPassword(passwordPayload))
                .thenReturn(Observable.just(responseBody));
        presenter.updateAccountPassword(passwordPayload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showPasswordUpdatedSuccessfully();
    }

    @Test
    public void updateAccountPasswordOnError() {
        Exception exception = new Exception("message");
        when(dataManager.updateAccountPassword(passwordPayload))
                .thenReturn(Observable.<ResponseBody>error(exception));
        presenter.updateAccountPassword(passwordPayload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(MFErrorParser.errorMessage(exception));
    }

    @Test
    public void updateAuthenticationToken() {
        String password = "password";
        presenter.updateAuthenticationToken(password);
        String authenticationToken = Credentials.basic(preferencesHelper.getUserName(), password);

        verify(preferencesHelper).saveToken(authenticationToken);
    }
}