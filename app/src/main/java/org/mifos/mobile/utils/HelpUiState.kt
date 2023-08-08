package org.mifos.mobile.utils

import org.mifos.mobile.models.FAQ

sealed class HelpUiState {
    data class ShowFaq(val faqArrayList: ArrayList<FAQ?>) : HelpUiState()
}