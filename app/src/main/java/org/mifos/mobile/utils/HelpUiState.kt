package org.mifos.mobile.utils

import org.mifos.mobile.core.model.entity.FAQ

sealed class HelpUiState {
    object Initial : HelpUiState()
    data class ShowFaq(
        val faqArrayList: ArrayList<FAQ?>,
        val selectedFaqPosition: Int = -1
    ) : HelpUiState()
}