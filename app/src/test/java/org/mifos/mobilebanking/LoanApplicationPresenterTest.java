package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.models.payload.LoansPayload;
import org.mifos.mobilebanking.models.templates.loans.LoanTemplate;
import org.mifos.mobilebanking.presenters.LoanApplicationPresenter;
import org.mifos.mobilebanking.ui.enums.LoanState;
import org.mifos.mobilebanking.ui.views.LoanApplicationMvpView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 12/7/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoanApplicationPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    LoanApplicationMvpView view;

    @Mock
    ResponseBody mockedResponseBody;

    private LoanApplicationPresenter presenter;
    private LoanTemplate loanTemplate, loanTemplateWithProduct;
    private LoansPayload loansPayload;

    @Before
    public void setUp() {
        presenter = new LoanApplicationPresenter(dataManager, context);
        presenter.attachView(view);

        loanTemplate = FakeRemoteDataSource.getLoanTemplate();
        loanTemplateWithProduct = FakeRemoteDataSource.getLoanTemplateByTemplate();
        loansPayload = FakeRemoteDataSource.getLoansPayload();
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

    @Test
    public void testLoadLoanApplicationTemplateNew() {
        LoanState loanState = LoanState.CREATE;
        when(dataManager.getLoanTemplate()).thenReturn(Observable.just(loanTemplate));

        presenter.loadLoanApplicationTemplate(loanState);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoanTemplate(loanTemplate);
        verify(view, never()).showError(context.getString(R.string.error_fetching_template));
    }

    @Test
    public void testLoadLoanApplicationTemplateUpdate() {
        LoanState loanState = LoanState.UPDATE;
        when(dataManager.getLoanTemplate()).thenReturn(Observable.just(loanTemplate));

        presenter.loadLoanApplicationTemplate(loanState);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showUpdateLoanTemplate(loanTemplate);
        verify(view, never()).showError(context.getString(R.string.error_fetching_template));
    }

    @Test
    public void testLoadLoanApplicationTemplateFails() {
        LoanState loanState = LoanState.CREATE;
        when(dataManager.getLoanTemplate()).thenReturn(Observable.<LoanTemplate>error(new
                RuntimeException()));

        presenter.loadLoanApplicationTemplate(loanState);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(context.getString(R.string.error_fetching_template));
        verify(view, never()).showLoanTemplate(loanTemplate);
    }

    @Test
    public void testLoadLoanApplicationTemplateByProductNew() {
        LoanState loanState = LoanState.CREATE;
        when(dataManager.getLoanTemplateByProduct(1)).thenReturn(Observable.
                just(loanTemplateWithProduct));

        presenter.loadLoanApplicationTemplateByProduct(1, loanState);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoanTemplateByProduct(loanTemplateWithProduct);
        verify(view, never()).showError(context.getString(R.string.error_fetching_template));
    }

    @Test
    public void testLoadLoanApplicationTemplateByProductUpdate() {
        LoanState loanState = LoanState.UPDATE;
        when(dataManager.getLoanTemplateByProduct(1)).thenReturn(Observable.
                just(loanTemplateWithProduct));

        presenter.loadLoanApplicationTemplateByProduct(1, loanState);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showUpdateLoanTemplateByProduct(loanTemplateWithProduct);
        verify(view, never()).showError(context.getString(R.string.error_fetching_template));
    }

    @Test
    public void testLoadLoanApplicationTemplateByProductFails() {
        LoanState loanState = LoanState.CREATE;
        when(dataManager.getLoanTemplateByProduct(1)).thenReturn(Observable.<LoanTemplate>error(new
                RuntimeException()));

        presenter.loadLoanApplicationTemplateByProduct(1, loanState);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(context.getString(R.string.error_fetching_template));
        verify(view, never()).showLoanTemplate(null);
    }

    @Test
    public void testCreateLoanAccount() {
        when(dataManager.createLoansAccount(loansPayload)).thenReturn(Observable.
                just(mockedResponseBody));

        presenter.createLoansAccount(loansPayload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoanAccountCreatedSuccessfully();

    }

    @Test
    public void testUpdateLoanAccount() {
        when(dataManager.updateLoanAccount(1, loansPayload)).thenReturn(Observable.
                just(mockedResponseBody));

        presenter.updateLoanAccount(1, loansPayload);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoanAccountUpdatedSuccessfully();

    }

}
