package org.mifos.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.presenters.HomeOldPresenter
import org.mifos.mobile.ui.views.HomeOldView
import org.mifos.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeOldPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: HomeOldView? = null

    @Mock
    var preferencesHelper: PreferencesHelper? = null
    private var presenter: HomeOldPresenter? = null
    private var clientAccounts: ClientAccounts? = null
    private var client: Client? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = HomeOldPresenter(dataManager!!, context!!, preferencesHelper!!)
        presenter?.attachView(view)
        clientAccounts = FakeRemoteDataSource.clientAccounts
        client = FakeRemoteDataSource.currentClient
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testLoadClientAccountDetails() {
        Mockito.`when`(dataManager?.clientAccounts).thenReturn(Observable.just(clientAccounts))
        presenter?.loadClientAccountDetails()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        clientAccounts
                ?.loanAccounts?.let { getLoanAccountDetails(it) }?.let { Mockito.verify(view)?.showLoanAccountDetails(it) }
        Mockito.verify(view)?.showSavingAccountDetails(getSavingAccountDetails(clientAccounts
                ?.savingsAccounts))
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_accounts))
    }

    @Test
    fun testLoadClientAccountDetailsFails() {
        Mockito.`when`(dataManager?.clientAccounts)
                .thenReturn(Observable.error(Throwable()))
        presenter?.loadClientAccountDetails()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context?.getString(R.string.error_fetching_accounts))
    }

    @Test
    fun testGetUserDetails() {
        Mockito.`when`(dataManager?.currentClient)
                .thenReturn(Observable.just(client))
        presenter?.userDetails
        Mockito.verify(view)?.showUserDetails(client)
    }

    @Test
    fun testGetUserDetailsFails() {
        Mockito.`when`(dataManager?.currentClient)
                .thenReturn(Observable.error(Throwable()))
        presenter?.userDetails
        Mockito.verify(view)?.showError(context
                ?.getString(R.string.error_client_not_found))
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
    fun testGetUnreadNotificationsCount() {
        val notificationCount = 10
        Mockito.`when`(dataManager?.unreadNotificationsCount)
                .thenReturn(Observable.just(notificationCount))
        presenter?.unreadNotificationsCount
        Mockito.verify(view)?.showNotificationCount(notificationCount)
    }

    @Test
    fun testGetUnreadNotificationsCountOnError() {
        Mockito.`when`(dataManager?.unreadNotificationsCount)
                .thenReturn(Observable.error(Throwable()))
        presenter?.unreadNotificationsCount
        Mockito.verify(view)?.showNotificationCount(0)
    }

    private fun getLoanAccountDetails(loanAccountList: List<LoanAccount>): Double {
        var totalAmount = 0.0
        for ((_, _, _, _, _, _, _, _, _, _, _, _, _, _, loanBalance) in loanAccountList) {
            totalAmount += loanBalance
        }
        return totalAmount
    }

    private fun getSavingAccountDetails(savingAccountList: List<SavingAccount>?): Double {
        var totalAmount = 0.0
        for ((_, _, _, _, _, accountBalance) in savingAccountList!!) {
            totalAmount += accountBalance
        }
        return totalAmount
    }
}