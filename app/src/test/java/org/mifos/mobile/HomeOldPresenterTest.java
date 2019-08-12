package org.mifos.mobile;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.api.local.PreferencesHelper;
import org.mifos.mobile.models.accounts.loan.LoanAccount;
import org.mifos.mobile.models.accounts.savings.SavingAccount;
import org.mifos.mobile.models.client.Client;
import org.mifos.mobile.models.client.ClientAccounts;
import org.mifos.mobile.presenters.HomeOldPresenter;
import org.mifos.mobile.ui.views.HomeOldView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HomeOldPresenterTest {
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    HomeOldView view;

    @Mock
    PreferencesHelper preferencesHelper;

    private HomeOldPresenter presenter;
    private ClientAccounts clientAccounts;
    private Client client;

    @Before
    public void setUp() throws Exception {
        presenter = new HomeOldPresenter(dataManager, context, preferencesHelper);
        presenter.attachView(view);
        clientAccounts = FakeRemoteDataSource.getClientAccounts();
        client = FakeRemoteDataSource.getCurrentClient();
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void testLoadClientAccountDetails() {
        when(dataManager.getClientAccounts()).thenReturn(Observable.just(clientAccounts));
        presenter.loadClientAccountDetails();

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoanAccountDetails(getLoanAccountDetails(clientAccounts
                .getLoanAccounts()));
        verify(view).showSavingAccountDetails(getSavingAccountDetails(clientAccounts
                .getSavingsAccounts()));
        verify(view, never()).showError(context.getString(R.string.error_fetching_accounts));
    }

    @Test
    public void testLoadClientAccountDetailsFails() {
        when(dataManager.getClientAccounts())
                .thenReturn(Observable.<ClientAccounts>error(new Throwable()));
        presenter.loadClientAccountDetails();

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showError(context.getString(R.string.error_fetching_accounts));
    }

    @Test
    public void testGetUserDetails() {
        when(dataManager.getCurrentClient())
                .thenReturn(Observable.just(client));
        presenter.getUserDetails();
        verify(view).showUserDetails(client);
    }

    @Test
    public void testGetUserDetailsFails() {
        when(dataManager.getCurrentClient())
                .thenReturn(Observable.<Client>error(new Throwable()));
        presenter.getUserDetails();

        verify(view).showError(context
                .getString(R.string.error_client_not_found));
    }

//    @Test
//    public void testGetUserImage() throws IOException {
//        ResponseBody responseBody = ResponseBody.create(MediaType.parse("text/plain"),
//                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+" +
//                        "9AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJ\n" +
//                        "bWFnZVJlYWR5ccllPAAAAJ1JREFUeNpi+P//PwMIA4E9EG8E4idQDGLbw+" +
//                        "WhiiqA+D8OXAFVAzbp\n" +
//                        "DxBvB2JLIGaGYkuoGEjOhhFIHAbij0BdPgxYACMj42ogJQpifwBiXSDeC8JIbt" +
//                        "4LxSC5DyxQjTeB\n" +
//                        "+BeaYb+Q5EBOAVutCzMJHUNNPADzzDokiYdAfAmJvwLkGeTgWQfyKZICS6hYBTwc0" +
//                        "QL8ORSjBDhA\n" +
//                        "gAEAOg13B6R/SAgAAAAASUVORK5CYII=");
//        when(dataManager.getClientImage())
//                .thenReturn(Observable.just(responseBody));
//        presenter.getUserImage();
//        final String encodedString = responseBody.string();
//        final String pureBase64Encoded =
//                encodedString.substring(encodedString.indexOf(',') + 1);
//        verify(preferencesHelper).setUserProfileImage(pureBase64Encoded);
//        verify(presenter).setUserProfile(pureBase64Encoded);
//    }

    @Test
    public void testGetUnreadNotificationsCount() {
        int notificationCount = 10;
        when(dataManager.getUnreadNotificationsCount())
                .thenReturn(Observable.just(notificationCount));
        presenter.getUnreadNotificationsCount();
        verify(view).showNotificationCount(notificationCount);
    }

    @Test
    public void testGetUnreadNotificationsCountOnError() {
        when(dataManager.getUnreadNotificationsCount())
                .thenReturn(Observable.<Integer>error(new Throwable()));
        presenter.getUnreadNotificationsCount();
        verify(view).showNotificationCount(0);
    }

    private double getLoanAccountDetails(List<LoanAccount> loanAccountList) {
        double totalAmount = 0;
        for (LoanAccount loanAccount : loanAccountList) {
            totalAmount += loanAccount.getLoanBalance();
        }
        return totalAmount;
    }

    private double getSavingAccountDetails(List<SavingAccount> savingAccountList) {
        double totalAmount = 0;
        for (SavingAccount savingAccount : savingAccountList) {
            totalAmount += savingAccount.getAccountBalance();
        }
        return totalAmount;
    }
}
