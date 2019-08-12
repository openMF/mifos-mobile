package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.presenters.GuarantorDetailPresenter;
import org.mifos.mobile.ui.views.GuarantorDetailView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuarantorDetailPresenterTest {
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    GuarantorDetailView view;

    @Mock
    ResponseBody responseBody;

    private GuarantorDetailPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new GuarantorDetailPresenter(context, dataManager);
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testDeleteGuarantor() throws IOException {
        long loanId = 1, guarantorId = 1;
        when(dataManager.deleteGuarantor(loanId, guarantorId))
                .thenReturn(Observable.just(responseBody));
        presenter.deleteGuarantor(loanId, guarantorId);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).guarantorDeletedSuccessfully(responseBody.string());
    }

    @Test
    public void testDeleteGuarantorOnError() throws IOException {
        long loanId = 1, guarantorId = 1;
        Exception exception = new Exception("ExceptionMessage");
        when(dataManager.deleteGuarantor(loanId, guarantorId))
                .thenReturn(Observable.<ResponseBody>error(exception));
        presenter.deleteGuarantor(loanId, guarantorId);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(exception.getMessage());
    }
}
