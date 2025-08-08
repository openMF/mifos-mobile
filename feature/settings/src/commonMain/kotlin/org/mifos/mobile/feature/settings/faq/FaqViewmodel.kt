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

internal class FaqViewModel : BaseViewModel<FaqState, FaqEvent, FaqAction>(
    initialState = FaqState(emptyList()),
) {
    init {
        viewModelScope.launch {
            sendAction(FaqAction.Internal.LoadFaqList)
        }
    }
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

internal data class FaqState(
    val faqList: List<FAQ> = emptyList(),
    val selectedFaqPosition: Int = 0,
)
internal sealed interface FaqEvent {
    data object OnNavigateBack : FaqEvent
    data object OnNavigateToHelp : FaqEvent
}
internal sealed interface FaqAction {
    data object NavigateBack : FaqAction
    data object NavigateToHelp : FaqAction

    data class UpdateFaqPosition(val position: Int) : FaqAction

    sealed interface Internal : FaqAction {
        data object LoadFaqList : Internal
    }
}
