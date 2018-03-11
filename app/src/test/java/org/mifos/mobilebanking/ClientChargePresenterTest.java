
package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.models.Charge;
import org.mifos.mobilebanking.models.Page;
import org.mifos.mobilebanking.presenters.ClientChargePresenter;
import org.mifos.mobilebanking.ui.views.ClientChargeView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;


import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Chirag Gupta on 12/06/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientChargePresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    ClientChargeView view;

    private ClientChargePresenter presenter;
    private Page<Charge> charge;
    private List<Charge> savingsCharge, loanCharge;

    @Before
    public void setUp() throws Exception {
        presenter = new ClientChargePresenter(dataManager, context);
        presenter.attachView(view);

        charge = FakeRemoteDataSource.getCharge();
        savingsCharge = FakeRemoteDataSource.getSavingsCharge();
        loanCharge = FakeRemoteDataSource.getLoanCharge();
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testLoadClientCharges() {
        when(dataManager.getClientCharges(1)).thenReturn(Observable.just(charge));

        presenter.loadClientCharges(1);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showClientCharges(charge.getPageItems());
        verify(view, never()).
                showErrorFetchingClientCharges(context.
                        getString(R.string.error_client_charge_loading));
    }

    @Test
    public void testLoadClientChargesFails() {
        when(dataManager.getClientCharges(1)).
                thenReturn(Observable.<Page<Charge>>error(new RuntimeException()));

        presenter.loadClientCharges(1);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view, never()).showClientCharges(charge.getPageItems());
        verify(view).
                showErrorFetchingClientCharges(
                        context.getString(R.string.error_client_charge_loading));
    }

    @Test
    public void testLoadLoanAccountCharges() {
        when(dataManager.getLoanCharges(1)).thenReturn(Observable.just(loanCharge));

        presenter.loadLoanAccountCharges(1);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showClientCharges(loanCharge);
        verify(view, never()).
                showErrorFetchingClientCharges(
                        context.getString(R.string.error_client_charge_loading));
    }

    @Test
    public void testLoadLoanAccountChargesFails() {
        when(dataManager.getLoanCharges(1)).
                thenReturn(Observable.<List<Charge>>error(new RuntimeException()));

        presenter.loadLoanAccountCharges(1);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view, never()).showClientCharges(loanCharge);
        verify(view).
                showErrorFetchingClientCharges(
                        context.getString(R.string.error_client_charge_loading));
    }

    @Test
    public void testLoadSavingsAccountCharges() {
        when(dataManager.getSavingsCharges(1)).thenReturn(Observable.just(savingsCharge));

        presenter.loadSavingsAccountCharges(1);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showClientCharges(savingsCharge);
        verify(view, never()).
                showErrorFetchingClientCharges(
                        context.getString(R.string.error_client_charge_loading));
    }

    @Test
    public void testLoadSavingsAccountChargesFails() {
        when(dataManager.getSavingsCharges(1)).
                thenReturn(Observable.<List<Charge>>error(new RuntimeException()));

        presenter.loadSavingsAccountCharges(1);
        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view, never()).showClientCharges(savingsCharge);
        verify(view).
                showErrorFetchingClientCharges(
                        context.getString(R.string.error_client_charge_loading));
    }

    @Test
    public void testLoadClientLocalCharges() {
        when(dataManager.getClientLocalCharges()).thenReturn(Observable.just(charge));

        presenter.loadClientLocalCharges();
        verify(view).showClientCharges(charge.getPageItems());
        verify(view, never()).
                showErrorFetchingClientCharges(
                        context.getString(R.string.error_client_charge_loading));
    }

    @Test
    public void testLoadClientLocalChargesFails() {
        when(dataManager.getClientLocalCharges()).
                thenReturn(Observable.<Page<Charge>>error(new RuntimeException()));

        presenter.loadClientLocalCharges();
        verify(view, never()).showClientCharges(charge.getPageItems());
        verify(view).
                showErrorFetchingClientCharges(
                        context.getString(R.string.error_client_charge_loading));
    }
}
