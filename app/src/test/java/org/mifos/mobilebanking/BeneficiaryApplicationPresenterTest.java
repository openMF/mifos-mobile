package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.models.beneficiary.BeneficiaryPayload;
import org.mifos.mobilebanking.models.beneficiary.BeneficiaryUpdatePayload;
import org.mifos.mobilebanking.models.templates.beneficiary.BeneficiaryTemplate;
import org.mifos.mobilebanking.presenters.BeneficiaryApplicationPresenter;
import org.mifos.mobilebanking.ui.views.BeneficiaryApplicationView;
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
public class BeneficiaryApplicationPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    BeneficiaryApplicationView view;

    @Mock
    ResponseBody mockedResponseBody;

    private BeneficiaryUpdatePayload updatePayload;
    private BeneficiaryPayload payload;
    private BeneficiaryApplicationPresenter presenter;
    private BeneficiaryTemplate beneficiaryTemplate;

    @Before
    public void setUp() {
        presenter = new BeneficiaryApplicationPresenter(dataManager, context);
        presenter.attachView(view);

        beneficiaryTemplate = FakeRemoteDataSource.getBeneficiaryTemplate();
        payload = FakeRemoteDataSource.beneficiaryPayload();
        updatePayload = FakeRemoteDataSource.beneficiaryUpdatePayload();
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

    @Test
    public void testShowBeneficiaryTemplate() {
        when(dataManager.getBeneficiaryTemplate()).thenReturn(Observable.just(beneficiaryTemplate));

        presenter.loadBeneficiaryTemplate();

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showBeneficiaryTemplate(beneficiaryTemplate);
        verify(view, never()).showError(context
                .getString(R.string.error_fetching_beneficiary_template));
    }

    @Test
    public void testShowBeneficiaryTemplateFails() {
        when(dataManager.getBeneficiaryTemplate()).thenReturn(Observable.<BeneficiaryTemplate>error
                (new RuntimeException()));

        presenter.loadBeneficiaryTemplate();

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(context
                .getString(R.string.error_fetching_beneficiary_template));
        verify(view, never()).showBeneficiaryTemplate(null);
    }

    @Test
    public void testCreateBeneficiary() {
        when(dataManager.createBeneficiary(payload)).thenReturn(Observable.
                just(mockedResponseBody));

        presenter.createBeneficiary(payload);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showBeneficiaryCreatedSuccessfully();
        verify(view, never()).showError(context
                .getString(R.string.error_creating_beneficiary));
    }

    @Test
    public void testCreateBeneficiaryFails() {
        when(dataManager.createBeneficiary(payload)).thenReturn(Observable.<ResponseBody>error(new
                RuntimeException()));

        presenter.createBeneficiary(payload);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view, never()).showBeneficiaryCreatedSuccessfully();
        verify(view).showError(context
                .getString(R.string.error_creating_beneficiary));
    }

    @Test
    public void testUpdateBeneficiary() {
        when(dataManager.updateBeneficiary(1, updatePayload)).thenReturn(Observable.
                just(mockedResponseBody));

        presenter.updateBeneficiary(1, updatePayload);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showBeneficiaryUpdatedSuccessfully();
        verify(view, never()).showError(context
                .getString(R.string.error_updating_beneficiary));
    }

    @Test
    public void testUpdateBeneficiaryFails() {
        when(dataManager.updateBeneficiary(1, updatePayload)).thenReturn(Observable.
                <ResponseBody>error(new RuntimeException()));

        presenter.updateBeneficiary(1, updatePayload);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(context
                .getString(R.string.error_updating_beneficiary));
        verify(view, never()).showBeneficiaryUpdatedSuccessfully();
    }
}