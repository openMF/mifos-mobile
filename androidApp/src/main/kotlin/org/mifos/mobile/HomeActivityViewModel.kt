/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.library.passcode.data.PasscodeManager
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.model.UserData
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val passcodeManager: PasscodeManager,
) : ViewModel() {

    val uiState: StateFlow<HomeActivityUiState> = userDataRepository.userData.map {
        HomeActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = HomeActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    fun logOut() {
        viewModelScope.launch {
            userDataRepository.logOut()
            passcodeManager.clearPasscode()
        }
    }
}

sealed interface HomeActivityUiState {
    data object Loading : HomeActivityUiState
    data class Success(val userData: UserData) : HomeActivityUiState
}
