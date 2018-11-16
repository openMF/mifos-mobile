package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.models.beneficiary.Beneficiary;
import org.mifos.mobilebanking.models.templates.account.AccountOptionsTemplate;
import org.mifos.mobilebanking.presenters.ThirdPartyTransferPresenter;
import org.mifos.mobilebanking.ui.views.ThirdPartyTransferView;
import org.mifos.mobilebanking.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;


import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ThirdPartyTransferPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    ThirdPartyTransferView view;

    private AccountOptionsTemplate accountOptionsTemplate;
    private ThirdPartyTransferPresenter presenter;
    private List<Beneficiary> beneficiaryList;

    @Before
    public void setUp() {
        presenter = new ThirdPartyTransferPresenter(dataManager, context);
        presenter.attachView(view);

        accountOptionsTemplate = FakeRemoteDataSource.getAccountOptionsTemplate();
        beneficiaryList = FakeRemoteDataSource.getBeneficiaries();
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

    @Test
    public void testTransferTemplate() {
        when(dataManager.getThirdPartyTransferTemplate()).thenReturn(Observable.
                just(accountOptionsTemplate));
        when(dataManager.getBeneficiaryList()).thenReturn(Observable.
                just(beneficiaryList));

        presenter.loadTransferTemplate();

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showThirdPartyTransferTemplate(accountOptionsTemplate);
        verify(view).showBeneficiaryList(beneficiaryList);
        verify(view, never()).showError(context.getString(
                R.string.error_fetching_account_transfer_template));
    }

    @Test
    public void testTransferTemplateFails() {
        when(dataManager.getThirdPartyTransferTemplate()).thenReturn(Observable.
                <AccountOptionsTemplate>error(new RuntimeException()));
        when(dataManager.getBeneficiaryList()).thenReturn(Observable.
                <List<Beneficiary>>error(new RuntimeException()));

        presenter.loadTransferTemplate();

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(context.getString(
                R.string.error_fetching_account_transfer_template));
        verify(view, never()).showThirdPartyTransferTemplate(accountOptionsTemplate);
        verify(view, never()).showBeneficiaryList(beneficiaryList);
    }
}
