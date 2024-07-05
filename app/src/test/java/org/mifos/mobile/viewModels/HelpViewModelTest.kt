package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.FAQ
import org.mifos.mobile.utils.HelpUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HelpViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockFAQArrayList: ArrayList<FAQ?>

    private lateinit var viewModel: HelpViewModel

    @Before
    fun setUp() {
        viewModel = HelpViewModel()
    }

    @Test
    fun testLoadFaq() = runTest {
        val qs = arrayOf("Question1", "Question2")
        val ans = arrayOf("Answer1", "Answer2")

        viewModel.loadFaq(qs, ans)

        viewModel.helpUiState.test{
            assertEquals(HelpUiState.ShowFaq(arrayListOf(FAQ("Question1", "Answer1", false), FAQ("Question2", "Answer2", false)))
                , awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testFilterList() = runTest {
        val query = "app"
        val mockFAQ1 =FAQ("How to use the app?", "Answer1", false)
        val mockFAQ2 = FAQ("Is there a user guide available?", "Answer2", false)
        viewModel.loadFaq(arrayOf("How to use the app?", "Is there a user guide available?"), arrayOf("Answer1", "Answer2"))
        advanceUntilIdle()
        val filteredList = viewModel.filterList(query)
        viewModel.helpUiState.test{
            assertEquals(HelpUiState.ShowFaq(arrayListOf(mockFAQ1)), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
