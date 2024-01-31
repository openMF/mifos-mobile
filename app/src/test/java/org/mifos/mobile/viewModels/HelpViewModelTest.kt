package org.mifos.mobile.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.models.FAQ
import org.mifos.mobile.ui.help.HelpViewModel
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

    @Mock
    private lateinit var helpUiStateObserver: Observer<HelpUiState>

    private lateinit var viewModel: HelpViewModel

    @Before
    fun setUp() {
        viewModel = HelpViewModel()
        viewModel.helpUiState.observeForever(helpUiStateObserver)
    }

    @Test
    fun testLoadFaq() {
        val qs = arrayOf("Question1", "Question2")
        val ans = arrayOf("Answer1", "Answer2")

        viewModel.loadFaq(qs, ans)

        verify(helpUiStateObserver).onChanged(
            HelpUiState.ShowFaq(
                arrayListOf(
                    FAQ(
                        "Question1",
                        "Answer1",
                        false
                    ),
                    FAQ(
                        "Question2",
                        "Answer2",
                        false
                    )
                )
            )
        )
        verifyNoMoreInteractions(helpUiStateObserver)
    }

    @Test
    fun testFilterList() {
        val query = "app"
        val mockFAQ1 = mock(FAQ::class.java)
        val mockFAQ2 = mock(FAQ::class.java)

        `when`(mockFAQ1.question).thenReturn("How to use the app?")
        `when`(mockFAQ2.question).thenReturn("Is there a user guide available?")

        val filteredList = viewModel.filterList(arrayListOf(mockFAQ1,mockFAQ2), query)

        assertEquals(mockFAQ1, filteredList[0])
    }
}
