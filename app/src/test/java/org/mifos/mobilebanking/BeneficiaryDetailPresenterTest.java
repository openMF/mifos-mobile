package org.mifos.mobilebanking;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.presenters.BeneficiaryDetailPresenter;
import org.mifos.mobilebanking.ui.views.BeneficiaryDetailView;
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
public class BeneficiaryDetailPresenterTest {
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    BeneficiaryDetailView view;

    @Mock
    ResponseBody mockedResponseBody;

    private BeneficiaryDetailPresenter presenter;
    private long beneficiaryId = 1;

    @Before
    public void setUp() {
        presenter = new BeneficiaryDetailPresenter(dataManager, context);
        presenter.attachView(view);
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

    @Test
    public void testDeleteBeneficiary() {
        when(dataManager.deleteBeneficiary(beneficiaryId)).thenReturn(Observable.
                just(mockedResponseBody));

        presenter.deleteBeneficiary(beneficiaryId);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showBeneficiaryDeletedSuccessfully();
        verify(view, never()).showError(context.getString(R.string.error_deleting_beneficiary));
    }

    @Test
    public void testDeleteBeneficiaryFails() {
        when(dataManager.deleteBeneficiary(beneficiaryId)).thenReturn(Observable.<ResponseBody>
                error(new RuntimeException()));

        presenter.deleteBeneficiary(beneficiaryId);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(context.getString(R.string.error_deleting_beneficiary));
        verify(view, never()).showBeneficiaryDeletedSuccessfully();
    }
}
