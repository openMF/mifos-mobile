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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.home.generated.resources.Res
import mifos_mobile.feature.home.generated.resources.feature_home_common_error
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class HomeViewModel(
    private val homeRepositoryImpl: HomeRepository,
    userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<HomeState, HomeEvent, HomeAction>(
    initialState = HomeState(
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        username = requireNotNull(userPreferencesRepositoryImpl.userInfo.value.userName),
        dialogState = null,
        items = serviceCards,
    ),
) {

    init {
        unreadNotificationsCount()
        loadClientAccountDetails()
    }

    override fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnNavigate -> sendEvent(HomeEvent.Navigate(action.route))

            is HomeAction.OnNotificationClick -> sendEvent(HomeEvent.NavigateToNotification)

            is HomeAction.OnDismissDialog -> updateState { it.copy(dialogState = null) }

            is HomeAction.ToggleAmountVisible -> handleAmountVisible()

            is HomeAction.Internal.ReceiveClientAccounts -> handleClientAccounts(
                action
                    .dataState,
                action.message,
            )
        }
    }

    private fun updateState(update: (HomeState) -> HomeState) {
        mutableStateFlow.update(update)
    }

    private fun handleAmountVisible() {
        updateState {
            it.copy(isAmountVisible = !state.isAmountVisible)
        }
    }

    private fun loadClientAccountDetails() {
        updateState { it.copy(dialogState = HomeState.DialogState.Loading) }
        viewModelScope.launch {
            val message = getString(Res.string.feature_home_common_error)
            homeRepositoryImpl.clientAccounts(clientId = state.clientId ?: 0).catch {
                updateState { it.copy(dialogState = HomeState.DialogState.Error(message)) }
            }.collect { clientAccounts ->
                sendAction(HomeAction.Internal.ReceiveClientAccounts(clientAccounts, message))
            }
        }
    }

    private fun handleClientAccounts(dataState: DataState<ClientAccounts>, message: String) {
        when (dataState) {
            is DataState.Error -> updateState {
                it.copy(
                    dialogState = HomeState
                        .DialogState.Error(message),
                )
            }

            DataState.Loading -> updateState { it.copy(dialogState = HomeState.DialogState.Loading) }

            is DataState.Success -> {
                if (dataState.data.loanAccounts.isNotEmpty()) {
                    getLoanAccountDetails(dataState.data.loanAccounts)
                    getSavingAccountDetails(dataState.data.savingsAccounts)
                    updateState {
                        it.copy(
                            clientAccounts = dataState.data,
                            dialogState = null,
                            currency = dataState.data.loanAccounts.firstOrNull()?.currency?.displaySymbol,
                        )
                    }
                } else {
                    updateState {
                        it.copy(
                            dialogState = null,
                            isLoanApplied = false,
                        )
                    }
                }
            }
        }
    }

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
     * Returns total Loan balance
     *
     * @param loanAccountList [List] of [LoanAccount] associated with the client
     * @return Returns `totalAmount` which is calculated by adding all [LoanAccount]
     * balance.
     */
    private fun getLoanAccountDetails(loanAccountList: List<LoanAccount>) {
        var totalAmount = 0.0
        for (loanAccount in loanAccountList) {
            totalAmount += loanAccount.loanBalance
        }

        updateState { it.copy(loanAmount = totalAmount) }
    }

    private fun getSavingAccountDetails(savingAccountList: List<SavingAccount>?) {
        var totalAmount = 0.0
        for (savingAccount in savingAccountList!!) {
            totalAmount += savingAccount.accountBalance
        }
        updateState { it.copy(savingsAmount = totalAmount) }
    }
}

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
    val dialogState: DialogState?,
    val items: ImmutableList<ServiceItem>,
) {

    sealed interface DialogState {
        data class Error(val message: String) : DialogState

        data object Loading : DialogState
    }
}

sealed interface HomeEvent {
    data class Navigate(val route: String) : HomeEvent
    data object NavigateToNotification : HomeEvent
}

sealed interface HomeAction {
    data class OnNavigate(val route: String) : HomeAction
    data object OnNotificationClick : HomeAction
    data object OnDismissDialog : HomeAction
    data object ToggleAmountVisible : HomeAction

    sealed interface Internal : HomeAction {
        data class ReceiveClientAccounts(
            val dataState: DataState<ClientAccounts>,
            val message: String,
        ) : Internal
    }
}
