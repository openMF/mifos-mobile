/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.home.generated.resources.Res
import mifos_mobile.feature.home.generated.resources.feature_server_error
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * `ViewModel` for the Home screen.
 *
 * This ViewModel is responsible for fetching and displaying client-specific data such as
 * account balances, recent transactions, and notifications. It uses a [BaseViewModel]
 * to manage its state ([HomeState]), handle actions ([HomeAction]), and emit events ([HomeEvent]).
 * It also monitors network connectivity to handle offline scenarios.
 *
 * @param homeRepositoryImpl Repository for fetching client and account data.
 * @param networkMonitor Monitors the network connectivity status.
 * @param userPreferencesRepositoryImpl Repository for accessing user preferences, such as client ID and user info.
 */
internal class HomeViewModel(
    private val homeRepositoryImpl: HomeRepository,
    private val networkMonitor: NetworkMonitor,
    userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<HomeState, HomeEvent, HomeAction>(
    initialState = HomeState(
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        username = requireNotNull(userPreferencesRepositoryImpl.userInfo.value.userName),
        items = serviceCards,
        uiState = HomeScreenState.Loading,
    ),
) {

    init {
        observeNetworkStatus()
    }

    /**
     * Observes the network connectivity status and updates the UI state accordingly.
     * If the network is unavailable, it sets the `networkStatus` flag in the state
     * and shows a network-related dialog.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(HomeAction.ObserveNetworkStatus(isOnline))
                }
        }
    }

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [HomeAction] to be handled.
     */
    override fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnNavigate -> sendEvent(HomeEvent.Navigate(action.route))

            is HomeAction.OnNotificationClick -> sendEvent(HomeEvent.NavigateToNotification)

            is HomeAction.OnDismissDialog -> updateState { it.copy(dialogState = null) }

            is HomeAction.ToggleAmountVisible -> handleAmountVisible()

            is HomeAction.ObserveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is HomeAction.Retry -> retry()

            is HomeAction.Internal.ReceiveClientAccounts -> handleClientAccounts(
                action.dataState,
            )
        }
    }

    /**
     * Manages UI state changes based on network connectivity.
     *
     * This function updates the application's state to reflect whether the device is online or offline.
     *
     * When the app is **offline**:
     * - It immediately updates the `networkStatus` in the state to `false`.
     * - If this is the **first time the app is launched**, the `uiState` is set to `HomeScreenState.Network`
     * to inform the user that a network connection is required.
     *
     * When the app is **online**:
     * - It immediately updates the `networkStatus` in the state to `true`.
     * - It then triggers essential functions to **refresh data** and ensure the UI is up-to-date,
     * specifically by calling `unreadNotificationsCount()` and `loadClientAccountDetails()`.
     *
     * @param isOnline A `Boolean` indicating the current network connectivity status.
     *
     * @see HomeScreenState
     */
    private fun handleNetworkStatus(isOnline: Boolean) {
        val isFirstLaunch = state.uiState == HomeScreenState.Loading

        updateState { it.copy(networkStatus = isOnline) }

        viewModelScope.launch {
            if (!isOnline) {
                updateState { current ->
                    current.copy(
                        uiState = if (isFirstLaunch) HomeScreenState.Network else current.uiState,
                    )
                }
            } else {
                unreadNotificationsCount()
                loadClientAccountDetails()
            }
        }
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the fetching of notifications
     * and client account details.
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = HomeScreenState.Network) }
            } else {
                unreadNotificationsCount()
                loadClientAccountDetails()
            }
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (HomeState) -> HomeState) {
        mutableStateFlow.update(update)
    }

    /**
     * Toggles the visibility of the amount on the screen.
     */
    private fun handleAmountVisible() {
        updateState {
            it.copy(isAmountVisible = !state.isAmountVisible)
        }
    }

    /**
     * Fetches the client's account details from the repository.
     */
    private fun loadClientAccountDetails() {
        updateState { it.copy(uiState = HomeScreenState.Loading) }

        viewModelScope.launch {
            homeRepositoryImpl.clientAccounts(clientId = state.clientId ?: 0)
                .catch {
                    updateState { it.copy(uiState = HomeScreenState.Error(Res.string.feature_server_error)) }
                }
                .collect { clientAccounts ->
                    sendAction(HomeAction.Internal.ReceiveClientAccounts(clientAccounts))
                }
        }
    }

    /**
     * Handles the result of the `clientAccounts` network call.
     *
     * On success, it updates the state with the client's loan and savings account
     * details, and calculates the total balances. If the client has no accounts,
     * it updates the state accordingly. On error, it displays an error dialog.
     *
     * @param dataState The [DataState] containing the client's account information.
     */
    private fun handleClientAccounts(dataState: DataState<ClientAccounts>) {
        when (dataState) {
            is DataState.Error -> updateState {
                it.copy(
                    uiState = HomeScreenState.Error(Res.string.feature_server_error),
                )
            }

            DataState.Loading -> updateState { it.copy(uiState = HomeScreenState.Loading) }

            is DataState.Success -> {
                val hasLoans = dataState.data.loanAccounts.isNotEmpty()
                val hasSavings = dataState.data.savingsAccounts?.isNotEmpty() ?: false

                if (hasLoans) {
                    getLoanAccountDetails(dataState.data.loanAccounts)
                }

                if (hasSavings) {
                    getSavingAccountDetails(dataState.data.savingsAccounts)
                }

                if (hasLoans || hasSavings) {
                    updateState {
                        it.copy(
                            clientAccounts = dataState.data,
                            uiState = HomeScreenState.Success,
                            currency = dataState.data.loanAccounts.firstOrNull()?.currency?.displaySymbol
                                ?: dataState.data.savingsAccounts?.firstOrNull()?.currency?.displaySymbol,
                        )
                    }
                } else {
                    updateState {
                        it.copy(
                            dialogState = null,
                            isLoanApplied = false,
                            uiState = HomeScreenState.Success,
                        )
                    }
                }
            }
        }
    }

    /**
     * Fetches the number of unread notifications from the repository and
     * updates the state.
     */
    private fun unreadNotificationsCount() {
        viewModelScope.launch {
            homeRepositoryImpl.unreadNotificationsCount().catch {
                updateState {
                    it.copy(notificationCount = 0)
                }
            }.collect { count ->
                when (count) {
                    is DataState.Error -> updateState {
                        it.copy(notificationCount = 0)
                    }

                    DataState.Loading -> Unit
                    is DataState.Success -> updateState {
                        it.copy(notificationCount = count.data)
                    }
                }
            }
        }
    }

    /**
     * Calculates the total balance from a list of loan accounts and updates the state.
     *
     * @param loanAccountList [List] of [LoanAccount] associated with the client.
     */
    private fun getLoanAccountDetails(loanAccountList: List<LoanAccount>) {
        var totalAmount = 0.0
        for (loanAccount in loanAccountList) {
            totalAmount += loanAccount.loanBalance
        }

        updateState { it.copy(loanAmount = totalAmount) }
    }

    /**
     * Calculates the total balance from a list of savings accounts and updates the state.
     *
     * @param savingAccountList [List] of [SavingAccount] associated with the client.
     */
    private fun getSavingAccountDetails(savingAccountList: List<SavingAccount>?) {
        var totalAmount = 0.0
        for (savingAccount in savingAccountList!!) {
            totalAmount += savingAccount.accountBalance
        }
        updateState { it.copy(savingsAmount = totalAmount) }
    }
}

/**
 * Represents the UI state for the Home screen.
 *
 * @property clientId The ID of the current client.
 * @property currency The currency symbol for the client's accounts, or `null`.
 * @property isLoanApplied A boolean indicating if the client has any active loans.
 * @property username The username of the currently logged-in user.
 * @property clientAccounts The full account details of the client, or `null`.
 * @property notificationCount The number of unread notifications.
 * @property loanAmount The total outstanding balance of all loans.
 * @property savingsAmount The total balance of all savings accounts.
 * @property isAmountVisible A boolean indicating if the balance amounts should be visible.
 * @property dialogState The state of any dialog to be shown on the screen.
 * @property items An immutable list of service card items to display on the home screen.
 * @property networkStatus A boolean indicating the current network connectivity status.
 * @property uiState The current state of the Home screen, which can be loading, success, error, or network-related.
 * @property networkBanner The state of the network banner, which can indicate online, offline, or back online.
 */
@Immutable
internal data class HomeState(
    val clientId: Long? = 0,
    val currency: String? = "",
    val isLoanApplied: Boolean = true,
    val username: String = "",
    val clientAccounts: ClientAccounts? = null,
    val notificationCount: Int = 0,
    val loanAmount: Double = 0.0,
    val savingsAmount: Double = 0.0,
    val isAmountVisible: Boolean = false,
    val dialogState: DialogState? = null,
    val items: ImmutableList<ServiceItem>,
    val networkStatus: Boolean = true,
    val uiState: HomeScreenState?,

) {
    /**
     * A sealed interface representing the different types of dialogs that can be
     * shown on the Home screen.
     */
    sealed interface DialogState {
        /**
         * Represents a generic error dialog with a message.
         * @property message The [StringResource] for the error message.
         */
        data class Error(val message: StringResource) : DialogState
    }
}

sealed interface HomeScreenState {
    /**
     * Represents the initial loading state of the Home screen.
     */
    data object Loading : HomeScreenState

    /**
     * Represents the state when the Home screen has successfully loaded.
     */
    data object Success : HomeScreenState

    /**
     * Represents an error state on the Home screen.
     * @property message The [StringResource] for the error message.
     */
    data class Error(val message: StringResource) : HomeScreenState

    data object Network : HomeScreenState
}

/**
 * A sealed interface representing one-time events that trigger UI side effects,
 * such as navigation.
 */
sealed interface HomeEvent {
    /**
     * Event to navigate to a specific screen.
     * @property route The navigation route string for the destination.
     */
    data class Navigate(val route: String) : HomeEvent

    /** Event to navigate to the notification screen. */
    data object NavigateToNotification : HomeEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle for the Home screen.
 */
sealed interface HomeAction {
    /**
     * User action to navigate to a specific screen.
     * @property route The navigation route string.
     */
    data class OnNavigate(val route: String) : HomeAction

    /** User action when the notification icon is clicked. */
    data object OnNotificationClick : HomeAction

    /** User action to dismiss a dialog. */
    data object OnDismissDialog : HomeAction

    /** User action to toggle the visibility of account balances. */
    data object ToggleAmountVisible : HomeAction

    /**
     * Action triggered by network status observation.
     * @property isOnline A boolean indicating if the device is online.
     */
    data class ObserveNetworkStatus(val isOnline: Boolean) : HomeAction

    /** Action to retry fetching data after an error or network issue. */
    data object Retry : HomeAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : HomeAction {
        /**
         * An internal action to handle the result of fetching client accounts.
         * @property dataState The [DataState] containing the client account data.
         */
        data class ReceiveClientAccounts(
            val dataState: DataState<ClientAccounts>,
        ) : Internal
    }
}
