/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.help

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mifos_mobile.feature.help.generated.resources.Res
import mifos_mobile.feature.help.generated.resources.faq_ans
import mifos_mobile.feature.help.generated.resources.faq_qs
import org.jetbrains.compose.resources.getStringArray
import org.mifos.mobile.core.model.entity.FAQ
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class HelpViewModel : BaseViewModel<HelpUiState, HelpEvent, HelpAction>(HelpUiState.Initial) {

    private var allFaqList: List<FAQ>? = null

    init {
        viewModelScope.launch {
            loadFaq()
        }
    }

    override fun handleAction(action: HelpAction) {
        when (action) {
            is HelpAction.LoadFaq -> {
                viewModelScope.launch {
                    loadFaq()
                }
            }
            is HelpAction.SearchFaq -> filterList(action.query)
            is HelpAction.UpdateFaqPosition -> updateSelectedFaqPosition(action.position)
            HelpAction.OnCallHelpLine -> sendEvent(HelpEvent.CallHelpLine)
            HelpAction.OnMailHelpLine -> sendEvent(HelpEvent.MailHelpLine)
            HelpAction.Location -> sendEvent(HelpEvent.Location)
            HelpAction.DismissSearch -> filterList("")
            HelpAction.NavigateBack -> sendEvent(HelpEvent.NavigateBack)
        }
    }

    private suspend fun loadFaq() {
        val questions = getStringArray(Res.array.faq_qs).map { it.replace("\\s+".toRegex(), " ").trim() }
        val answers = getStringArray(Res.array.faq_ans).map { it.replace("\\s+".toRegex(), " ").trim() }

        if (allFaqList.isNullOrEmpty()) {
            allFaqList = questions.mapIndexed { index, question -> FAQ(question, answers.getOrNull(index)) }
        }

        mutableStateFlow.value = state.copy(faqList = ArrayList(allFaqList!!))
    }

    private fun filterList(query: String) {
        val filteredList = allFaqList
            ?.filter { it.question?.contains(query, ignoreCase = true) ?: false }
            ?: emptyList()
        mutableStateFlow.value = state.copy(searchQuery = query, faqList = ArrayList(filteredList))
    }

    private fun updateSelectedFaqPosition(position: Int) {
        val newPosition = if (state.selectedFaqPosition == position) -1 else position
        mutableStateFlow.value = state.copy(selectedFaqPosition = newPosition)
    }
}

internal data class HelpUiState(
    val faqList: List<FAQ> = emptyList(),
    val searchQuery: String = "",
    val selectedFaqPosition: Int = -1,
) {
    companion object {
        val Initial: HelpUiState = HelpUiState()
    }
}

sealed interface HelpAction {
    data object LoadFaq : HelpAction
    data object OnCallHelpLine : HelpAction
    data object OnMailHelpLine : HelpAction
    data object Location : HelpAction
    data class SearchFaq(val query: String) : HelpAction
    data class UpdateFaqPosition(val position: Int) : HelpAction
    data object DismissSearch : HelpAction
    data object NavigateBack : HelpAction
}

sealed interface HelpEvent {
    data object CallHelpLine : HelpEvent
    data object MailHelpLine : HelpEvent
    data object Location : HelpEvent
    data object NavigateBack : HelpEvent
}
