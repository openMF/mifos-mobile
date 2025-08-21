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

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.DarkThemeConfig
import org.mifos.mobile.core.model.LanguageConfig
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.NetworkBannerState

class ComposeAppViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<AppState, AppEvent, AppAction>(
    initialState = AppState(
        darkTheme = false,
        isAndroidTheme = false,
        isDynamicColorsEnabled = false,
    ),
) {
    val networkStatus = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true,
        )

    init {
        networkStatus
            .onEach { handleNetworkStatus(it) }
            .launchIn(viewModelScope)

        userPreferencesRepository
            .observeDarkThemeConfig
            .onEach { trySendAction(AppAction.Internal.ThemeUpdate(it)) }
            .launchIn(viewModelScope)

        userPreferencesRepository
            .observeDynamicColorPreference
            .onEach { trySendAction(AppAction.Internal.DynamicColorsUpdate(it)) }
            .launchIn(viewModelScope)

        userPreferencesRepository
            .observeLanguage
            .map { AppEvent.UpdateAppLocale(it.localName) }
            .onEach(::sendEvent)
            .launchIn(viewModelScope)
    }

    private var lastOnlineStatus: Boolean? = null

    /**
     * Manages the display of a network status banner based on changes in connectivity.
     *
     * This function is a robust way to handle the network status UI, ensuring the
     * banner is only shown when there's a meaningful change in connectivity. It relies on
     * a state variable, `lastOnlineStatus`, to track the previous network state.
     *
     * The logic is divided into three key scenarios:
     *
     * 1.  **First Launch (`lastOnlineStatus == null`):**
     * - This case handles the initial state of the app.
     * - If the app launches offline, the `NetworkBannerState` is immediately set to `Offline`
     * to inform the user.
     * - If the app launches online, no banner is shown, and the state is set to `None`.
     *
     * 2.  **Going Offline (`!isOnline && lastOnlineStatus == true`):**
     * - This is the transition from an online to an offline state.
     * - The `NetworkBannerState` is updated to `Offline`, which triggers the display
     * of the "No internet connection" banner.
     *
     * 3.  **Coming Back Online (`isOnline && lastOnlineStatus == false`):**
     * - This is the transition from an offline to an online state.
     * - The `NetworkBannerState` is set to `BackOnline`, which displays a brief
     * "Back online" banner.
     * - A `2-second delay` is introduced to allow the user to see the message.
     * - After the delay, the `NetworkBannerState` is reset to `None`,
     * which hides the banner.
     *
     * Finally, after all state updates, the `lastOnlineStatus` is updated to the
     * current `isOnline` value, ensuring the state is maintained for the next network change.
     *
     * @param isOnline A `Boolean` value representing the current network status. It defaults to the
     * current value of `networkStatus.value` if not provided.
     * @see NetworkBannerState
     */
    private fun handleNetworkStatus(isOnline: Boolean = networkStatus.value) {
        viewModelScope.launch {
            when {
                lastOnlineStatus == null -> {
                    if (!isOnline) {
                        mutableStateFlow.update { it.copy(networkBanner = NetworkBannerState.Offline) }
                    } else {
                        mutableStateFlow.update { it.copy(networkBanner = NetworkBannerState.None) }
                    }
                }

                !isOnline && lastOnlineStatus == true -> {
                    mutableStateFlow.update { it.copy(networkBanner = NetworkBannerState.Offline) }
                }

                isOnline && lastOnlineStatus == false -> {
                    mutableStateFlow.update { it.copy(networkBanner = NetworkBannerState.BackOnline) }
                    delay(2000)
                    mutableStateFlow.update { it.copy(networkBanner = NetworkBannerState.None) }
                }
            }

            lastOnlineStatus = isOnline
        }
    }

    override fun handleAction(action: AppAction) {
        when (action) {
            is AppAction.AppSpecificLanguageUpdate -> handleAppSpecificLanguageUpdate(action)

            is AppAction.Internal.ThemeUpdate -> handleAppThemeUpdated(action)

            is AppAction.Internal.DynamicColorsUpdate -> handleDynamicColorsUpdate(action)
        }
    }

    private fun handleAppSpecificLanguageUpdate(action: AppAction.AppSpecificLanguageUpdate) {
        viewModelScope.launch {
            userPreferencesRepository.setLanguage(action.appLanguage)
        }
    }

    private fun handleAppThemeUpdated(action: AppAction.Internal.ThemeUpdate) {
        mutableStateFlow.update {
            it.copy(darkTheme = action.theme == DarkThemeConfig.DARK)
        }
        sendEvent(AppEvent.UpdateAppTheme(osValue = action.theme.osValue))
    }

    private fun handleDynamicColorsUpdate(action: AppAction.Internal.DynamicColorsUpdate) {
        mutableStateFlow.update { it.copy(isDynamicColorsEnabled = action.isDynamicColorsEnabled) }
    }
}

data class AppState(
    val darkTheme: Boolean,
    val isAndroidTheme: Boolean,
    val isDynamicColorsEnabled: Boolean,
    val networkBanner: NetworkBannerState = NetworkBannerState.None,
)

sealed interface AppEvent {
    data class ShowToast(val message: String) : AppEvent

    data class UpdateAppLocale(
        val localeName: String?,
    ) : AppEvent

    data class UpdateAppTheme(
        val osValue: Int,
    ) : AppEvent
}

sealed interface AppAction {
    data class AppSpecificLanguageUpdate(val appLanguage: LanguageConfig) : AppAction

    sealed class Internal : AppAction {

        data class ThemeUpdate(
            val theme: DarkThemeConfig,
        ) : Internal()

        data class DynamicColorsUpdate(
            val isDynamicColorsEnabled: Boolean,
        ) : Internal()
    }
}
