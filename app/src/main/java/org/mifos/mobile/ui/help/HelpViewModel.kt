package org.mifos.mobile.ui.help

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mifos.mobile.core.model.entity.FAQ
import org.mifos.mobile.utils.HelpUiState
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor() : ViewModel() {

    private val _helpUiState = MutableStateFlow<HelpUiState>(HelpUiState.Initial)
    val helpUiState: StateFlow<HelpUiState> get() = _helpUiState
    private var allFaqList: ArrayList<FAQ?>? = null

    fun loadFaq(qs: Array<String>?, ans: Array<String>?) {
        if (allFaqList.isNullOrEmpty()) {
            val faqArrayList = ArrayList<FAQ?>()
            if (qs != null) {
                for (i in qs.indices) {
                    faqArrayList.add(FAQ(qs[i], ans?.get(i)))
                }
            }
            allFaqList = faqArrayList
            _helpUiState.value = HelpUiState.ShowFaq(faqArrayList)
        } else {
            _helpUiState.value = HelpUiState.ShowFaq(allFaqList!!)
        }
    }

    fun filterList(query: String) {
        val filteredList = ArrayList<FAQ?>()
        allFaqList?.let { faqList ->
            for (faq in faqList) {
                if (faq?.question?.lowercase(Locale.ROOT)
                        ?.contains(query.lowercase(Locale.ROOT)) == true
                ) {
                    filteredList.add(faq)
                }
            }
        }
        _helpUiState.value = HelpUiState.ShowFaq(filteredList)
    }

    fun updateSelectedFaqPosition(position: Int) {
        val currentState = _helpUiState.value
        if (currentState is HelpUiState.ShowFaq) {
            val selectFaqPosition =
                if (currentState.selectedFaqPosition == position) -1 else position
            _helpUiState.value = currentState.copy(selectedFaqPosition = selectFaqPosition)
        }
    }
}