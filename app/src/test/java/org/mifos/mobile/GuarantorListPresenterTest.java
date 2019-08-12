package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.models.guarantor.GuarantorPayload;
import org.mifos.mobile.presenters.GuarantorListPresenter;
import org.mifos.mobile.ui.views.GuarantorListView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuarantorListPresenterTest {
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    GuarantorListView view;

    private GuarantorListPresenter presenter;
    List<GuarantorPayload> guarantorPayloadList;


    @Before
    public void setUp() throws Exception {
        presenter = new GuarantorListPresenter(context, dataManager);
        presenter.attachView(view);
        guarantorPayloadList = FakeRemoteDataSource.getGuarantorsList();
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testGetGuarantorList() throws IOException {
        long loanId = 1;
        when(dataManager.getGuarantorList(loanId))
                .thenReturn(Observable.just(guarantorPayloadList));
        presenter.getGuarantorList(loanId);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showGuarantorListSuccessfully(guarantorPayloadList);
    }

    @Test
    public void testGetGuarantorListOnError() throws IOException {
        long loanId = 1;
        Exception exception = new Exception("ExceptionMessage");
        when(dataManager.getGuarantorList(loanId))
                .thenReturn(Observable.<List<GuarantorPayload>>error(exception));
        presenter.getGuarantorList(loanId);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(exception.getMessage());
    }
}
