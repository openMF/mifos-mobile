package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.models.payload.TransferPayload;
import org.mifos.mobilebanking.presenters.TransferProcessPresenter;
import org.mifos.mobilebanking.ui.views.TransferProcessView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransferProcessPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    TransferProcessView view;

    @Mock
    ResponseBody mockedResponseBody;

    private TransferProcessPresenter presenter;
    private TransferPayload transferPayload;

    @Before
    public void setUp() {
        presenter = new TransferProcessPresenter(dataManager, context);
        presenter.attachView(view);

        transferPayload = FakeRemoteDataSource.getTransferPayload();
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

    @Test
    public void testMakeSavingsTransfer() {
        when(dataManager.makeTransfer(transferPayload)).thenReturn(Observable.
                just(mockedResponseBody));

        presenter.makeSavingsTransfer(transferPayload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showTransferredSuccessfully();
    }

    @Test
    public void testMakeSavingsTransferFails() {
        when(dataManager.makeTransfer(transferPayload)).thenReturn(Observable.
                <ResponseBody>error(new RuntimeException()));

        presenter.makeSavingsTransfer(transferPayload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view, never()).showTransferredSuccessfully();
    }

    @Test
    public void testMakeTPTTransfer() {
        when(dataManager.makeThirdPartyTransfer(transferPayload)).thenReturn(Observable.
                just(mockedResponseBody));

        presenter.makeTPTTransfer(transferPayload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showTransferredSuccessfully();
    }

    @Test
    public void testMakeTPTTransferFails() {
        when(dataManager.makeThirdPartyTransfer(transferPayload)).thenReturn(Observable.
                <ResponseBody>error(new RuntimeException()));

        presenter.makeTPTTransfer(transferPayload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view, never()).showTransferredSuccessfully();
    }
}
