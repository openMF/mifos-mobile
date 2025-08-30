/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.settings

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_error_fetching_client
import mifos_mobile.feature.settings.generated.resources.feature_settings_logout_description
import mifos_mobile.feature.settings.generated.resources.feature_settings_logout_title
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ImageUtil
import org.mifos.mobile.feature.settings.componenets.SettingsItems
import org.mifos.mobile.feature.settings.componenets.settingsItems
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * `ViewModel` for the Settings screen.
 *
 * This ViewModel handles fetching and displaying client-related information,
 * including client details and a profile image. It follows a Unidirectional
 * Data Flow (UDF) pattern, using a [BaseViewModel] to manage its state ([SettingsState]),
 * handle actions ([SettingsAction]), and emit events ([SettingsEvents]).
 *
 * @param homeRepositoryImpl Repository for fetching home-related data, such as client info and image.
 * @param userPreferencesRepositoryImpl Repository for accessing user preferences, including the client ID.
 * @param userDataRepositoryImpl Repository for logout user.
 */
internal class SettingsViewModel(
    private val homeRepositoryImpl: HomeRepository,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val userDataRepositoryImpl: UserDataRepository,
) : BaseViewModel<SettingsState, SettingsEvents, SettingsAction>(
    initialState = run {
        SettingsState(
            settingsItems = settingsItems,
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        )
    },
) {
    init {
        loadUserData()
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (SettingsState) -> SettingsState) {
        mutableStateFlow.update(update)
    }

    /**
     * Sets the state of the dialog to be displayed on the screen.
     *
     * @param dialogState The new [SettingsState.DialogState] to set, or `null` to dismiss the dialog.
     */
    private fun setDialogState(dialogState: SettingsState.DialogState?) {
        updateState { it.copy(dialogState = dialogState) }
    }

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [SettingsAction] to be handled.
     */
    override fun handleAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnNavigateBack -> sendEvent(SettingsEvents.NavigateBack)
            SettingsAction.DismissDialog -> setDialogState(null)
            SettingsAction.LogoutDialog -> handleLogout()
            SettingsAction.Logout -> handleInternalLogOut()
            is SettingsAction.Internal.ReceiveClientInfo -> handleClientResponse(action.dataState)
            is SettingsAction.Internal.ReceiveClientImage -> handleClientImageResponse(action.dataState)
            is SettingsAction.NavigateTo -> sendEvent(SettingsEvents.NavigateTo(action.item))
        }
    }

    /**
     * Handles the user logout process.
     *
     * This function initiates the logout flow by updating the state to show a confirmation dialog.
     * The actual logout logic (clearing data, navigating) is handled by a separate function
     * after the user confirms the action.
     *
     * This function sets the dialog state to a [SettingsState.DialogState.Logout] with
     * a title and a descriptive message to prompt user confirmation.
     */
    private fun handleLogout() {
        mutableStateFlow.update {
            it.copy(
                dialogState = SettingsState.DialogState.Logout(
                    title = Res.string.feature_settings_logout_title,
                    message = Res.string.feature_settings_logout_description,
                ),
            )
        }
    }

    /**
     * Executes the internal logout logic.
     *
     * This function first dismisses any active dialog by setting the dialog state to null.
     * It then triggers the actual logout operation on the `userDataRepository`. This includes
     * clearing user-specific data and tokens from the repository, which is a critical
     * step in securely logging out the user.
     *
     * The commented-out code `userDataRepository.logout(...)` represents the intended
     * functionality to be implemented. The `reason` parameter provides context for the logout event.
     */
    private fun handleInternalLogOut() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }

        viewModelScope.launch {
            userDataRepositoryImpl.logOut()
        }
    }

    /**
     * Fetches the client's details and profile image from the repositories.
     */
    private fun loadUserData() {
        viewModelScope.launch {
            homeRepositoryImpl.currentClient(state.clientId ?: -1L)
                .catch {
                    setDialogState(
                        SettingsState.DialogState.Error(
                            Res.string.feature_settings_error_fetching_client,
                        ),
                    )
                }
                .collect { sendAction(SettingsAction.Internal.ReceiveClientInfo(it)) }
        }

        viewModelScope.launch {
            homeRepositoryImpl.clientImage(state.clientId ?: -1L)
                .catch {
                    // Do nothing on image fetch error, as it's not critical.
                }
                .collect { sendAction(SettingsAction.Internal.ReceiveClientImage(it)) }
        }
    }

    /**
     * Handles the response from the `currentClient` network call.
     *
     * On success, it updates the state with the client data.
     * On loading, it shows a loading dialog.
     * On error, it shows an error dialog.
     *
     * @param state The [DataState] containing the client data.
     */
    private fun handleClientResponse(state: DataState<Client>) {
        when (state) {
            is DataState.Error -> {
                setDialogState(
                    SettingsState.DialogState.Error(
                        Res.string.feature_settings_error_fetching_client,
                    ),
                )
            }
            DataState.Loading -> setDialogState(SettingsState.DialogState.Loading)
            is DataState.Success -> {
                setDialogState(null)
                updateState { it.copy(client = state.data) }
            }
        }
    }

    /**
     * Handles the response from the `clientImage` network call.
     *
     * On success, it calls [setUserProfile] to process the image data.
     * On loading, it shows a loading dialog.
     *
     * @param state The [DataState] containing the base64 encoded image string.
     */
    private fun handleClientImageResponse(state: DataState<String>) {
        when (state) {
            is DataState.Error -> {
                // No need to show user that client image getting failed
                setDialogState(null)
            }
            DataState.Loading -> setDialogState(SettingsState.DialogState.Loading)
            is DataState.Success -> {
                setDialogState(null)
                setUserProfile(state.data)
            }
        }
    }

    /**
     * Decodes a base64 encoded image string, compresses it, and updates the state
     * with the resulting bitmap as a byte array.
     *
     * @param image The base64 encoded image string.
     */
    @OptIn(ExperimentalEncodingApi::class)
    private fun setUserProfile(image: String?) {
        if (image.isNullOrBlank()) return

        // Extract the Base64 payload after the standard data URL prefix (more robust than first comma)
        val afterPrefix = image.substringAfter("base64,", image)
        // Remove any whitespace/newlines that can appear in API responses
        val cleaned = afterPrefix.replace(Regex("\\s+"), "")
        try {
            val decodedBytes = Base64.decode(cleaned)
            val compressedImageBytes = ImageUtil.compressImage(decodedBytes)
            if (compressedImageBytes.isNotEmpty()) {
                updateState { it.copy(profileImage = compressedImageBytes) }
            }
        } catch (e: Exception) {
            // Log the error but fail silently in the UI
            println(e.message)
        }
    }
}

/**
 * Represents the UI state for the Settings screen.
 *
 * @property settingsItems An immutable list of settings menu items.
 * @property clientId The ID of the current client.
 * @property client The client's detailed information, or `null` if not yet loaded.
 * @property profileImage The client's profile image as a byte array, or `null`.
 * @property dialogState The state of any dialog to be shown on the screen.
 */
@Immutable
internal data class SettingsState(
    val settingsItems: ImmutableList<SettingsItems>,
    val clientId: Long? = null,
    val client: Client? = null,
    val profileImage: ByteArray? = null,
    val dialogState: DialogState? = null,
) {
    /**
     * A sealed interface representing the different types of dialogs that can be
     * shown on the Settings screen.
     */
    sealed interface DialogState {
        /**
         * Represents a generic error dialog with a message.
         * @property message The [StringResource] for the error message.
         */
        data class Error(val message: StringResource) : DialogState

        /** Represents a loading dialog. */
        data object Loading : DialogState

        /** Represents a logout dialog. */
        data class Logout(
            val title: StringResource,
            val message: StringResource,
        ) : DialogState
    }
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle for the Settings screen.
 */
internal sealed interface SettingsAction {
    /** User action to navigate back from the screen. */
    data object OnNavigateBack : SettingsAction

    /** User action to dismiss a dialog. */
    data object DismissDialog : SettingsAction

    /**
     * Action to navigate to a specific settings screen.
     * @property item The [SettingsItems] to navigate to.
     */
    data class NavigateTo(val item: SettingsItems) : SettingsAction

    /**
     * Action to display a logout confirmation dialog.
     */
    data object LogoutDialog : SettingsAction

    /**
     * Action to perform user logout.
     */
    data object Logout : SettingsAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : SettingsAction {
        /**
         * An internal action to handle the result of fetching client information.
         * @property dataState The [DataState] containing the client data.
         */
        data class ReceiveClientInfo(val dataState: DataState<Client>) : Internal

        /**
         * An internal action to handle the result of fetching the client's profile image.
         * @property dataState The [DataState] containing the image data as a string.
         */
        data class ReceiveClientImage(val dataState: DataState<String>) : Internal
    }
}

/**
 * A sealed interface representing one-time events that trigger UI side effects,
 * such as navigation.
 */
internal sealed interface SettingsEvents {
    /** Event to navigate back from the screen. */
    data object NavigateBack : SettingsEvents

    data class NavigateTo(val item: SettingsItems) : SettingsEvents
}
