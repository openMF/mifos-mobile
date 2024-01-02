package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.repositories.HomeRepositoryImp
import org.mifos.mobile.repositories.UserDetailRepositoryImp
import org.mifos.mobile.ui.user_profile.UserDetailViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.UserDetailUiState
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


    private lateinit var viewModel: UserDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = UserDetailViewModel(userDetailRepositoryImp, homeRepositoryImp)
        viewModel.preferencesHelper = preferencesHelper
    }

    @Test
    fun testLoadingUserDetails_Success(): Unit = runBlocking {
        val mockClient = Mockito.mock(Client::class.java)

        Mockito.`when`(homeRepositoryImp.currentClient()).thenReturn(flowOf(mockClient))

        viewModel.userDetails

        viewModel.userDetailUiState.collect { value ->
            Assert.assertTrue(value is UserDetailUiState.ShowUserDetails)
            Assert.assertEquals(mockClient, (value as UserDetailUiState.ShowUserDetails).client)
        }
    }

    @Test
    fun testLoadingUserDetails_Error(): Unit = runBlocking {
        val errorMessageResId = R.string.error_fetching_client

        Mockito.`when`(homeRepositoryImp.currentClient()).thenThrow(RuntimeException())

        viewModel.userDetails

        viewModel.userDetailUiState.collect { value ->
            assertTrue(value is UserDetailUiState.ShowError)
            assertEquals(errorMessageResId, (value as UserDetailUiState.ShowError))
        }
    }

}