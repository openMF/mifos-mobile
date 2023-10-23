package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mifos.mobile.models.FAQ
import org.mifos.mobile.utils.HelpUiState
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor() : ViewModel() {

    private val _helpUiState = MutableStateFlow<HelpUiState>(HelpUiState.Initial)
    val helpUiState: StateFlow<HelpUiState> get() = _helpUiState

    fun loadFaq(qs: Array<String>?, ans: Array<String>?) {
        val faqArrayList = ArrayList<FAQ?>()
        if (qs != null) {
            for (i in qs.indices) {
                faqArrayList.add(FAQ(qs[i], ans?.get(i)))
            }
        }
        _helpUiState.value = HelpUiState.ShowFaq(faqArrayList)
    }

    fun filterList(faqArrayList: ArrayList<FAQ?>?, query: String): ArrayList<FAQ?> {
        val filteredList = ArrayList<FAQ?>()
        if (faqArrayList != null) {
            for (faq in faqArrayList) {
                if (faq?.question?.lowercase(Locale.ROOT)
                        ?.contains(query.lowercase(Locale.ROOT)) == true
                ) {
                    filteredList.add(faq)
                }
            }
        }
        return filteredList
    }
}