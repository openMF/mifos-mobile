/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.share.application.shareApplication

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.share_application.generated.resources.Res
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_server
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_submit_failed
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_unsaved_changes_message
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.ShareAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.model.entity.templates.shares.ShareProduct
import org.mifos.mobile.core.ui.utils.BaseViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * `ViewModel` for the savings application screen, responsible for handling user input,
 * business logic, and UI state management.
 *
 * It uses a [BaseViewModel] as its foundation to manage [ShareApplicationState],
 * handle [ShareApplicationAction] from the UI, and send [ShareApplicationEvent]
 * to trigger one-time UI side effects.
 *
 * @property userPreferencesRepositoryImpl Repository for accessing user preferences, specifically the client ID.
 * @property shareAccountRepositoryImpl Repository for interacting with savings-related data from a remote source.
 * @property homeRepositoryImpl Repository for accessing home-related data, such as client information.
 * @property networkMonitor Monitors the network connectivity status.
 */
@Suppress("CyclomaticComplexMethod", "TooManyFunctions")
internal class ShareApplyViewModel(
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val shareAccountRepositoryImpl: ShareAccountRepository,
    private val homeRepositoryImpl: HomeRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<ShareApplicationState, ShareApplicationEvent, ShareApplicationAction>(
    initialState = ShareApplicationState(
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        applicantName = "",
        uiState = ShareApplicationUiState.Loading,
    ),
) {

    init {
        observeNetworkStatus()
    }

    /**
     * Observes the network connectivity status and updates the UI state accordingly.
     * If the network is unavailable, it sets the `networkUnavailable` flag in the state
     * and shows a network-related dialog.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(ShareApplicationAction.Internal.ReceiveNetworkResult(isOnline))
                }
        }
    }

    /**
     * Manages UI state changes based on network connectivity.
     *
     * This function updates the application's state to reflect whether the device is online or offline.
     *
     * When the app is **offline**:
     * - It immediately updates the `networkStatus` in the state to `false`.
     * to inform the user that a network connection is required.
     *
     * When the app is **online**:
     * - It immediately updates the `networkStatus` in the state to `true`.
     * - It then triggers essential functions to **refresh data** and ensure the UI is up-to-date,
     * specifically by calling `getClientDataAndTemplate()`.
     *
     * @param isOnline A `Boolean` indicating the current network connectivity status.
     *
     */
    private fun handleNetworkResult(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }

        viewModelScope.launch {
            if (!isOnline) {
                updateState { current ->
                    if (current.uiState is ShareApplicationUiState.Loading ||
                        current.uiState is ShareApplicationUiState.Error ||
                        current.uiState is ShareApplicationUiState.Network
                    ) {
                        current.copy(uiState = ShareApplicationUiState.Network)
                    } else {
                        current
                    }
                }
            } else {
                getClientDataAndTemplate()
            }
        }
    }

    /**
     * Fetches client data and a generic share template from the repositories.
     * The results are combined and handled in a single flow to manage loading and error states.
     */
    private fun getClientDataAndTemplate() {
        showLoading()
        viewModelScope.launch {
            combine(
                homeRepositoryImpl.currentClient(state.clientId),
                shareAccountRepositoryImpl.getShareProducts(state.clientId),
            ) { client, template ->
                client to template
            }
                .catch { throwable ->

                    updateState {
                        it.copy(
                            uiState = if (throwable.cause is IOException) {
                                ShareApplicationUiState.Network
                            } else {
                                ShareApplicationUiState.Error(Res.string.feature_apply_share_error_server)
                            },
                        )
                    }
                }
                .collect { (client, template) ->
                    sendAction(
                        ShareApplicationAction.Internal.ReceiveClientAndTemplateResult(
                            client,
                            template,
                        ),
                    )
                }
        }
    }

    /**
     * Handles the result of the `fetchSavingsTemplate` network call.
     * Updates the state with product options and currency on success,
     * or displays an error and navigates back on failure.
     *
     * @param client The [DataState] containing the client data.
     * @param template The [DataState] containing the savings template data.
     */
    private fun handleClientAndSavingsTemplate(
        client: DataState<Client>,
        template: DataState<Page<ShareProduct>>,
    ) {
        when {
            listOf(client, template).any { it is DataState.Loading } -> {
                showLoading()
            }

            client is DataState.Success && template is DataState.Success -> {
                val products = template.data.pageItems
                updateState {
                    it.copy(
                        applicantName = client.data.displayName ?: "",
                        productOptions = products,
                        uiState = if (products.isEmpty()) {
                            ShareApplicationUiState.Empty
                        } else {
                            ShareApplicationUiState.Success
                        },
                    )
                }
            }

            else -> updateState {
                it.copy(
                    uiState = ShareApplicationUiState.Error(Res.string.feature_apply_share_error_server),
                )
            }
        }
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the `getClientDataAndTemplate` function.
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ShareApplicationUiState.Network) }
            } else {
                getClientDataAndTemplate()
            }
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (ShareApplicationState) -> ShareApplicationState) {
        mutableStateFlow.update(update)
    }

    /**
     * Sets the dialog state to a full-screen loading spinner.
     */
    private fun showLoading() {
        updateState { it.copy(uiState = ShareApplicationUiState.Loading) }
    }

    /**
     * Displays an error dialog with a given message.
     *
     * @param error The [StringResource] for the error message to display.
     */
    private fun showErrorState(error: StringResource) {
        updateState { it.copy(uiState = ShareApplicationUiState.Error(error)) }
    }

    /**
     * Dismisses any currently visible dialog by setting the dialog state to null.
     */
    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [ShareApplicationAction] to be handled.
     */
    override fun handleAction(action: ShareApplicationAction) {
        when (action) {
            is ShareApplicationAction.ShareProductChange -> {
                onShareProductChange(action.id, action.name)
            }

            is ShareApplicationAction.NavigateToConfirmDetails -> validateAndSubmit()

            is ShareApplicationAction.OnNavigateBack -> navigateBack()

            is ShareApplicationAction.ConfirmNavigation -> sendEvent(ShareApplicationEvent.NavigateBack)

            is ShareApplicationAction.DismissDialog -> dismissDialog()

            is ShareApplicationAction.Internal.ReceiveClientAndTemplateResult ->
                handleClientAndSavingsTemplate(action.client, action.template)

            is ShareApplicationAction.Internal.ReceiveNetworkResult -> handleNetworkResult(action.isOnline)

            ShareApplicationAction.Retry -> retry()
        }
    }

    /**
     * Handles changes to the selected savings product.
     * It updates the state with the new product ID and name.
     *
     * @param id The ID of the selected savings product.
     * @param name The name of the selected savings product.
     */
    private fun onShareProductChange(id: Long, name: String) {
        mutableStateFlow.update {
            it.copy(
                selectedShareProduct = name,
                selectedShareProductId = id,
                hasChanges = true,
            )
        }
    }

    /**
     * Validates all form fields before submitting. If validation is successful,
     * it proceeds to `handleSubmit`.
     */
    private fun validateAndSubmit() {
        handleSubmit()
    }

    /**
     * Handles the successful submission of the savings application.
     * It shows a loading overlay and sends an event to navigate to the confirmation screen.
     */
    private fun handleSubmit() {
        viewModelScope.launch {
            try {
                updateState {
                    it.copy(
                        hasChanges = false,
                        uiState = ShareApplicationUiState.Success,
                    )
                }
                sendEvent(ShareApplicationEvent.NavigateToConfirmDetailsScreen)
            } catch (e: Exception) {
                showErrorState(Res.string.feature_apply_share_error_submit_failed)
            }
        }
    }

    /**
     * Handles the back navigation. If there are unsaved changes, it shows a
     * confirmation dialog. Otherwise, it sends an event to navigate back.
     */
    private fun navigateBack() {
        if (state.hasChanges) {
            mutableStateFlow.update {
                it.copy(
                    dialogState = ShareApplicationDialogState.UnsavedChanges(
                        Res.string.feature_apply_share_unsaved_changes_message,
                    ),
                )
            }
        } else {
            sendEvent(ShareApplicationEvent.NavigateBack)
        }
    }
}

/**
 * Represents the UI state for the savings application screen.
 *
 * @property clientId The ID of the current client.
 * @property applicantName The name of the applicant.
 * @property productOptions A list of available savings product options.
 * @property selectedShareProduct The name of the selected share product.
 * @property selectedShareProductId The ID of the selected share product.
 * @property dialogState The state of any dialogs that overlay the main content.
 * @property savingsProductTemplate The full savings template object for the selected product.
 * @property hasChanges A boolean indicating if there are unsaved changes.
 * @property networkStatus A boolean indicating if the network is unavailable.
 * @property uiState The primary UI state of the screen (e.g., Loading, Empty, Success).
 */
@OptIn(ExperimentalMaterial3Api::class)
internal data class ShareApplicationState(
    val clientId: Long,
    val applicantName: String,
    val productOptions: List<ShareProduct> = emptyList(),
    val selectedShareProduct: String = "",
    val selectedShareProductId: Long = 0,
    val dialogState: ShareApplicationDialogState? = null,
    val savingsProductTemplate: SavingsAccountTemplate? = null,
    val hasChanges: Boolean = false,
    val networkStatus: Boolean = false,
    val uiState: ShareApplicationUiState?,
) {
    /**
     * A boolean indicating if the entire form is valid for submission.
     * This is based on the absence of errors and non-empty fields.
     */
    val isFormValid: Boolean
        get() = networkStatus &&
            applicantName.isNotBlank() &&
            selectedShareProduct.isNotBlank()

    /**
     * A map of savings product IDs to their names, derived from `productOptions`.
     */
    val productOptionsMap: Map<Long, String> = productOptions
        .mapNotNull { option ->
            val id = option.id?.toLong()
            val name = option.name
            if (id != null && name != null) id to name else null
        }
        .toMap()

    /**
     * The current date formatted as a string.
     */
    @OptIn(ExperimentalTime::class)
    val submittedOnDate: String
        get() {
            val todayMillis = Clock.System.now().toEpochMilliseconds()
            return DateHelper.getDateMonthYearString(todayMillis)
        }
}

/**
 * A sealed interface representing the different types of full-screen UI states
 * for the savings application screen.
 */
sealed interface ShareApplicationUiState {
    /** Represents a full-screen loading state. */
    data object Loading : ShareApplicationUiState

    /** Represents an empty state where no data is available. */
    data object Empty : ShareApplicationUiState

    /**
     * Represents an error state with a message.
     * @property message The [StringResource] for the error message.
     */
    data class Error(val message: StringResource) : ShareApplicationUiState

    /** Represents a successful state where content can be displayed. */
    data object Success : ShareApplicationUiState

    /** Represents a state where there is a network connectivity issue. */
    data object Network : ShareApplicationUiState

    /** Represents a state where an overlay loading spinner should be shown. */
    data object OverlayLoading : ShareApplicationUiState
}

/**
 * A sealed interface representing the different types of dialogs that can be
 * shown on the savings application screen.
 */
internal sealed interface ShareApplicationDialogState {
    /**
     * Represents a dialog to confirm navigation with unsaved changes.
     * @property message The [StringResource] for the confirmation message.
     */
    data class UnsavedChanges(val message: StringResource) : ShareApplicationDialogState
}

/**
 * A sealed interface representing one-time events that trigger UI side effects.
 */
internal sealed interface ShareApplicationEvent {
    /** Navigates back from the current screen. */
    data object NavigateBack : ShareApplicationEvent

    /** Navigates to the confirmation details screen. */
    data object NavigateToConfirmDetailsScreen : ShareApplicationEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle.
 */
internal sealed interface ShareApplicationAction {
    /** User action to navigate back. */
    data object OnNavigateBack : ShareApplicationAction

    /** User action to dismiss a dialog. */
    data object DismissDialog : ShareApplicationAction

    /** User action to retry a failed operation. */
    data object Retry : ShareApplicationAction

    /** User action to confirm navigation (e.g., dismissing an unsaved changes dialog). */
    data object ConfirmNavigation : ShareApplicationAction

    /**
     * User action when a savings product is selected.
     * @property id The ID of the selected savings product.
     * @property name The name of the selected savings product.
     */
    data class ShareProductChange(val id: Long, val name: String) : ShareApplicationAction

    /** User action to navigate to the confirm details screen, triggering form validation. */
    data object NavigateToConfirmDetails : ShareApplicationAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : ShareApplicationAction {

        /**
         * Internal Action triggered by network status observation.
         * @property isOnline A boolean indicating if the device is online.
         */
        data class ReceiveNetworkResult(val isOnline: Boolean) : ShareApplicationAction

        /**
         * An internal action to handle the result of fetching client and template data.
         * @property client The [DataState] containing the client data.
         * @property template The [DataState] containing the share product data.
         */
        data class ReceiveClientAndTemplateResult(
            val client: DataState<Client>,
            val template: DataState<Page<ShareProduct>>,
        ) : Internal
    }
}
