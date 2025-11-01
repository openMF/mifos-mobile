/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.faq

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.faq_ans
import mifos_mobile.feature.settings.generated.resources.faq_qs
import org.jetbrains.compose.resources.getStringArray
import org.mifos.mobile.core.common.DataState.Loading.data
import org.mifos.mobile.core.model.entity.FAQ
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * ViewModel for the FAQ screen. It handles the business logic, state management,
 * and events for the FAQ feature.
 *
 * @constructor Creates an instance of [FaqViewModel] and initiates the loading of the FAQ list.
 */
internal class FaqViewModel : BaseViewModel<FaqState, FaqEvent, FaqAction>(
    initialState = FaqState(emptyList()),
) {
    init {
        viewModelScope.launch {
            sendAction(FaqAction.Internal.LoadFaqList)
        }
    }

    /**
     * Handles actions dispatched from the UI.
     * @param action The [FaqAction] to be processed.
     */
    override fun handleAction(action: FaqAction) {
        when (action) {
            FaqAction.NavigateBack -> {
                sendEvent(FaqEvent.OnNavigateBack)
            }
            is FaqAction.NavigateToHelp -> {
                sendEvent(FaqEvent.OnNavigateToHelp)
            }
            is FaqAction.Internal.LoadFaqList -> loadFaqList()

            is FaqAction.UpdateFaqPosition -> {
                mutableStateFlow.update {
                    it.copy(selectedFaqPosition = action.position)
                }
            }
        }
    }

    /**
     * Loads the list of FAQs from the string resources and updates the state.
     * Questions and answers are retrieved from XML arrays, cleaned of extra whitespace,
     * and then mapped into a list of [FAQ] objects.
     */
    private fun loadFaqList() {
        viewModelScope.launch {
            val questions = getStringArray(Res.array.faq_qs).map {
                it.replace("\\s+".toRegex(), " ").trim()
            }
            val answers = getStringArray(Res.array.faq_ans).map {
                it.replace("\\s+".toRegex(), " ").trim()
            }
            val data = questions.mapIndexed { index, question ->
                FAQ(
                    question = question,
                    answer = answers[index],
                )
            }
            mutableStateFlow.update {
                it.copy(faqList = data)
            }
        }
    }
}

/**
 * Represents the state of the FAQ screen.
 * @property faqList The list of frequently asked questions.
 * @property selectedFaqPosition The index of the currently selected/expanded FAQ item.
 */
internal data class FaqState(
    val faqList: List<FAQ> = emptyList(),
    val selectedFaqPosition: Int = 0,
)

/**
 * Represents the events that can be sent from the ViewModel to the UI.
 */
internal sealed interface FaqEvent {
    data object OnNavigateBack : FaqEvent
    data object OnNavigateToHelp : FaqEvent
}

/**
 * Represents the actions that can be dispatched from the UI to the ViewModel.
 */
internal sealed interface FaqAction {
    /** Action to navigate back. */
    data object NavigateBack : FaqAction

    /** Action to navigate to the help screen. */
    data object NavigateToHelp : FaqAction

    /** Action to update the selected FAQ item. */
    data class UpdateFaqPosition(val position: Int) : FaqAction

    /** Internal ViewModel actions not directly triggered by the user. */
    sealed interface Internal : FaqAction {
        /** Action to load the FAQ list. */
        data object LoadFaqList : Internal
    }
}
