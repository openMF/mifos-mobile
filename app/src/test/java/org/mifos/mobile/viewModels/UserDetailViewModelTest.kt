package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.repositories.HomeRepositoryImp
import org.mifos.mobile.repositories.UserDetailRepositoryImp
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.feature.user_profile.utils.UserDetailUiState
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class UserDetailViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var userDetailRepositoryImp: UserDetailRepositoryImp

    @Mock
    lateinit var homeRepositoryImp: HomeRepositoryImp

    @Mock
    private lateinit var preferencesHelper: PreferencesHelper


    private lateinit var viewModel: org.mifos.mobile.feature.user_profile.viewmodel.UserDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = org.mifos.mobile.feature.user_profile.viewmodel.UserDetailViewModel(
            userDetailRepositoryImp,
            homeRepositoryImp
        )
        viewModel.preferencesHelper = preferencesHelper
    }

    @Test
    fun testLoadingUserDetails_Success(): Unit = runTest{
        val mockClient = Mockito.mock(Client::class.java)

        Mockito.`when`(homeRepositoryImp.currentClient()).thenReturn(flowOf(mockClient))

        viewModel.userDetailUiState.test {
            viewModel.userDetails
            assertEquals(UserDetailUiState.Loading ,awaitItem())
            assertEquals(UserDetailUiState.ShowUserDetails(mockClient), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected =  Exception::class)
    fun testLoadingUserDetails_Error(): Unit = runTest{
        val errorMessageResId = R.string.error_fetching_client

        Mockito.`when`(homeRepositoryImp.currentClient())
            .thenThrow( Exception("Error fetching client details"))


        viewModel.userDetailUiState.test {
            viewModel.userDetails
            assertEquals(UserDetailUiState.Loading ,awaitItem())
            assertEquals(UserDetailUiState.ShowError(errorMessageResId), awaitItem())
            cancelAndIgnoreRemainingEvents()
           }
    }

}