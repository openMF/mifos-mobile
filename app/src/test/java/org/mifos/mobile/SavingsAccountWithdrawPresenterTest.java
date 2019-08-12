package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload;
import org.mifos.mobile.presenters.SavingsAccountWithdrawPresenter;
import org.mifos.mobile.ui.views.SavingsAccountWithdrawView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SavingsAccountWithdrawPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    SavingsAccountWithdrawView view;

    private SavingsAccountWithdrawPresenter presenter;

    @Mock
    SavingsAccountWithdrawPayload payload;

    @Mock
    ResponseBody responseBody;

    @Before
    public void setUp() throws Exception {
        presenter = new SavingsAccountWithdrawPresenter(dataManager, context);
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testSubmitWithdrawSavingsAccount() {
        String accountId = "1";
        when(dataManager.submitWithdrawSavingsAccount(accountId, payload))
                .thenReturn(Observable.just(responseBody));
        presenter.submitWithdrawSavingsAccount(accountId, payload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showSavingsAccountWithdrawSuccessfully();
    }

    @Test
    public void testSubmitWithdrawSavingsAccountOnError() {
        String accountId = "1";
        Exception exception = new Exception("message");
        when(dataManager.submitWithdrawSavingsAccount(accountId, payload))
                .thenReturn(Observable.<ResponseBody>error(exception));
        presenter.submitWithdrawSavingsAccount(accountId, payload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(exception.getMessage());
    }

}