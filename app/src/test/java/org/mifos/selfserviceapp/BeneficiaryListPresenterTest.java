package org.mifos.selfserviceapp;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.presenters.BeneficiaryListPresenter;
import org.mifos.selfserviceapp.ui.views.BeneficiariesView;
import org.mifos.selfserviceapp.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 27/6/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class BeneficiaryListPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    BeneficiariesView view;

    private BeneficiaryListPresenter presenter;
    private List<Beneficiary> beneficiaryList;

    @Before
    public void setUp() throws Exception {
        presenter = new BeneficiaryListPresenter(dataManager, context);
        presenter.attachView(view);

        beneficiaryList = FakeRemoteDataSource.getBeneficiaries();

        when(context.getString(R.string.error_fetching_beneficiaries)).
                thenReturn("Failed to fetch Beneficiaries");

    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testLoadBeneficiaries() throws Exception {
        when(dataManager.getBeneficiaryList()).thenReturn(Observable.just(beneficiaryList));

        presenter.loadBeneficiaries();

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showBeneficiaryList(beneficiaryList);
        verify(view, never()).showError(context.getString(R.string.error_fetching_beneficiaries));
    }

    @Test
    public void testLoadBeneficiariesFails() throws Exception {
        when(dataManager.getBeneficiaryList()).thenReturn(Observable.<List<Beneficiary>>error(new
                RuntimeException()));

        presenter.loadBeneficiaries();

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(context.getString(R.string.error_fetching_beneficiaries));
        verify(view, never()).showBeneficiaryList(beneficiaryList);
    }

}
