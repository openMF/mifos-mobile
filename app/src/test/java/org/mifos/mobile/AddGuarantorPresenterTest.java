package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.presenters.AddGuarantorPresenter;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddGuarantorPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    protected AddGuarantorPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new AddGuarantorPresenter(context, dataManager);
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void getGuarantorTemplate() {
            when(dataManager.getGuarantorTemplate(long))
                    .thenReturn(Observable.just(payload));
            presenter.loadClientAccountDetails();
        verify(guarantorView).showProgress();
        verify(guarantorView).hideProgress();
        verify(guarantorView).showGuarantorTemplate(loanId);
        verify(guarantorView).showGuarantorUpdation(payload);
        verify(guarantorView).showGuarantorApplication(payload);
        verify(guarantorView, never()).showError(context.getString(R.string.error_fetching_GuarantorDetails));
        }

    @Test
    public void createGuarantor() {
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

    @Test
    public void updateGuarantor() {
        public void testDeleteGuarantorOnError() throws IOException {
            long loanId = 1, guarantorId = 1;
            Exception exception = new Exception("ExceptionMessage");
            when(dataManager.deleteGuarantor(loanId, guarantorId))
                    .thenReturn(Observable.<ResponseBody>error(exception));
            presenter.updateGuarantor(payload, loanId, guarantorId);Guarantor(loanId, guarantorId);
            verify(view).showProgress();
            verify(view).hideProgress();
            verify(view).showError(exception.getMessage());
        }
    }
}