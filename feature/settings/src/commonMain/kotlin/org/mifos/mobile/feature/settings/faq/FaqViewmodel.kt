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
import org.mifos.mobile.core.model.entity.FAQ
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * ViewModel for the FAQ screen. It is responsible for loading the list of frequently
 * asked questions, handling user interactions, and managing the UI state.
 */
internal class FaqViewModel : BaseViewModel<FaqState, FaqEvent, FaqAction>(
    initialState = FaqState(emptyList()),
) {
    init {
        // Automatically triggers loading the FAQ list when the ViewModel is created.
        viewModelScope.launch {
            sendAction(FaqAction.Internal.LoadFaqList)
        }
    }

    /**
     * Processes incoming [FaqAction]s to update the state or send events.
     *
     * @param action The action to be handled.
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
     * Loads the questions and answers from the string array resources,
     * pairs them into a list of [FAQ] objects, and updates the state.
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
 *
 * @property faqList The list of frequently asked questions.
 * @property selectedFaqPosition The index of the currently expanded FAQ item.
 */
internal data class FaqState(
    val faqList: List<FAQ> = emptyList(),
    val selectedFaqPosition: Int = 0,
)

/**
 * Defines one-time events that can be sent from the ViewModel to the UI.
 */
internal sealed interface FaqEvent {
    /** Signals that the UI should navigate back. */
    data object OnNavigateBack : FaqEvent

    /** Signals that the UI should navigate to the help screen. */
    data object OnNavigateToHelp : FaqEvent
}

/**
 * Defines all possible actions that can be sent from the UI to the ViewModel.
 */
internal sealed interface FaqAction {
    /** An action to navigate back. */
    data object NavigateBack : FaqAction

    /** An action to navigate to the help screen. */
    data object NavigateToHelp : FaqAction

    /**
     * An action to update the index of the currently selected (expanded) FAQ.
     * @param position The new position index.
     */
    data class UpdateFaqPosition(val position: Int) : FaqAction

    /**
     * Defines internal actions that are not directly triggered by the user.
     */
    sealed interface Internal : FaqAction {
        /** An internal action to trigger loading the FAQ list. */
        data object LoadFaqList : Internal
    }
}
