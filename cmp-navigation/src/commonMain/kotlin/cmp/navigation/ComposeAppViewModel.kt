/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.model.UserData

class ComposeAppViewModel(
    private val userDataRepository: UserDataRepository,
//    private val passcodeManager: PasscodeManager,
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = userDataRepository.userData.map { dataState ->
        when (dataState) {
            is DataState.Success -> MainUiState.Success(dataState.data)
            is DataState.Error -> MainUiState.Error(dataState.exception.message ?: "Unknown error")
            DataState.Loading -> MainUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    fun logOut() {
        viewModelScope.launch {
            userDataRepository.logOut()
//            passcodeManager.clearPasscode()
        }
    }
}

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Error(val error: String) : MainUiState
    data class Success(val userData: UserData) : MainUiState
}
