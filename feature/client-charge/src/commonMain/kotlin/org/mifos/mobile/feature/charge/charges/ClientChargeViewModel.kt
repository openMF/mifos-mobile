/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.charges

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.charges
import mifos_mobile.feature.client_charge.generated.resources.client_charges
import mifos_mobile.feature.client_charge.generated.resources.feature_generic_error_server
import mifos_mobile.feature.client_charge.generated.resources.loan_charges
import mifos_mobile.feature.client_charge.generated.resources.savings_charges
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.charge.components.ChargeFilterUtil

internal class ClientChargeViewModel(
    private val accountsRepositoryImpl: AccountsRepository,
    private val clientChargeRepositoryImp: ClientChargeRepository,
    userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ClientChargeState, ClientChargeEvent, ClientChargeAction>(
    initialState = run {
        val chargeRoute = savedStateHandle.toRoute<ClientChargesRoute>()
        val initialType = ChargeType.valueOf(chargeRoute.chargeType.uppercase())

        val topBarId = when (initialType) {
            ChargeType.CLIENT -> Res.string.client_charges
            ChargeType.SAVINGS -> Res.string.savings_charges
            ChargeType.LOAN -> Res.string.loan_charges
            else -> Res.string.charges
        }

        val canSwitch = initialType == ChargeType.CLIENT

        ClientChargeState(
            chargeType = initialType,
            chargeTypeId = chargeRoute.chargeTypeId,
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
            topBarTitleResId = topBarId,
            canSwitchAccounts = canSwitch,
            isOnline = false,
        )
    },
) {

    init {
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(ClientChargeAction.ReceiveNetworkResult(isOnline = isOnline))
                }
        }
    }

    private fun updateState(update: (ClientChargeState) -> ClientChargeState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: ClientChargeAction) {
        when (action) {
            is ClientChargeAction.OnNavigate -> sendEvent(ClientChargeEvent.Navigate)
            is ClientChargeAction.OnDismissDialog -> mutableStateFlow.update {
                it.copy(dialogState = null)
            }
            is ClientChargeAction.OnChargeClick -> sendEvent(
                ClientChargeEvent.OnChargeClick(action.charge),
            )

            is ClientChargeAction.RefreshCharges -> loadCharges()
            is ClientChargeAction.Retry -> {
                viewModelScope.launch {
                    if (!state.networkStatus) {
                        updateState { it.copy(uiState = ScreenUiState.Network) }
                    } else {
                        loadCharges()
                    }
                }
            }
            is ClientChargeAction.ReceiveNetworkResult -> handleNetworkResult(action.isOnline)

            is ClientChargeAction.ToggleFilter,
            is ClientChargeAction.ClearFilter,
            is ClientChargeAction.ApplyFilter,
            -> handleFilterAction(action)

            is ClientChargeAction.Internal -> handleInternalAction(action)
        }
    }

    private fun handleFilterAction(action: ClientChargeAction) {
        when (action) {
            is ClientChargeAction.ToggleFilter -> {
                updateState { it.copy(showFilter = !it.showFilter) }
            }
            is ClientChargeAction.ClearFilter -> performClearFilter()
            is ClientChargeAction.ApplyFilter -> performApplyFilter(action)
            else -> Unit
        }
    }

    private fun handleInternalAction(action: ClientChargeAction.Internal) {
        when (action) {
            is ClientChargeAction.Internal.ReceiveClientChargesResult ->
                handleClientChargesResult(action.result)

            is ClientChargeAction.Internal.ReceiveLoanOrSavingsChargesResult ->
                handleLoanOrSavingsChargesResult(action.result)

            is ClientChargeAction.Internal.SavingsAccountsLoaded ->
                updateSavingsAccounts(action.accounts)
            is ClientChargeAction.Internal.LoanAccountsLoaded ->
                updateLoanAccounts(action.accounts)
            is ClientChargeAction.Internal.ShareAccountsLoaded ->
                updateShareAccounts(action.accounts)
        }
    }

    private fun performClearFilter() {
        if (state.canSwitchAccounts) {
            updateState {
                it.copy(
                    selectedSavingsAccount = null,
                    selectedLoanAccount = null,
                    selectedShareAccount = null,
                    activeFilter = ChargeFilterUtil.ALL,
                    chargeType = ChargeType.CLIENT,
                    chargeTypeId = null,
                    topBarTitleResId = Res.string.client_charges,
                    showFilter = false,
                )
            }
            loadCharges()
        } else {
            updateState {
                it.copy(
                    activeFilter = ChargeFilterUtil.ALL,
                    showFilter = false,
                )
            }
            applyLocalFilter()
        }
    }

    private fun performApplyFilter(action: ClientChargeAction.ApplyFilter) {
        val previousId = state.chargeTypeId
        val previousType = state.chargeType

        val newFilter = action.filter

        val newChargeType = when (action.target) {
            is ChargeAccountTarget.Savings -> ChargeType.SAVINGS
            is ChargeAccountTarget.Loan -> ChargeType.LOAN
            is ChargeAccountTarget.Share -> ChargeType.SHARE
            ChargeAccountTarget.AllAccounts -> ChargeType.CLIENT
        }

        val newId = when (val target = action.target) {
            is ChargeAccountTarget.Savings -> target.account.id
            is ChargeAccountTarget.Loan -> target.account.id
            is ChargeAccountTarget.Share -> target.account.id
            ChargeAccountTarget.AllAccounts -> null
        }

        val newTitle = when (newChargeType) {
            ChargeType.SAVINGS -> Res.string.savings_charges
            ChargeType.LOAN -> Res.string.loan_charges
            ChargeType.SHARE -> Res.string.charges
            else -> Res.string.client_charges
        }

        updateState {
            it.copy(
                selectedSavingsAccount = (action.target as? ChargeAccountTarget.Savings)?.account,
                selectedLoanAccount = (action.target as? ChargeAccountTarget.Loan)?.account,
                selectedShareAccount = (action.target as? ChargeAccountTarget.Share)?.account,
                activeFilter = newFilter,
                showFilter = false,
                chargeType = newChargeType,
                topBarTitleResId = newTitle,
                chargeTypeId = newId,
            )
        }

        if (previousId != newId || previousType != newChargeType) {
            loadCharges()
        } else {
            applyLocalFilter()
        }
    }

    private fun updateSavingsAccounts(accounts: List<SavingAccount>) {
        if (accounts.isEmpty()) return
        updateState { state ->
            val default = accounts.firstOrNull { it.id == state.chargeTypeId }
            val shouldSelectDefault = !state.canSwitchAccounts && state.chargeType == ChargeType.SAVINGS

            state.copy(
                savingsAccounts = accounts,
                selectedSavingsAccount = if (shouldSelectDefault) default else state.selectedSavingsAccount,
            )
        }
    }

    private fun updateLoanAccounts(accounts: List<LoanAccount>) {
        if (accounts.isEmpty()) return
        updateState { state ->
            val default = accounts.firstOrNull { it.id == state.chargeTypeId }
            val shouldSelectDefault = !state.canSwitchAccounts && state.chargeType == ChargeType.LOAN

            state.copy(
                loanAccounts = accounts,
                selectedLoanAccount = if (shouldSelectDefault) default else state.selectedLoanAccount,
            )
        }
    }

    private fun updateShareAccounts(accounts: List<ShareAccount>) {
        if (accounts.isEmpty()) return
        updateState { state ->
            val default = accounts.firstOrNull { it.id == state.chargeTypeId }
            val shouldSelectDefault = !state.canSwitchAccounts && state.chargeType == ChargeType.SHARE

            state.copy(
                shareAccounts = accounts,
                selectedShareAccount = if (shouldSelectDefault) default else state.selectedShareAccount,
            )
        }
    }

    private fun fetchAccounts(accountType: String) {
        viewModelScope.launch {
            accountsRepositoryImpl.loadAccounts(
                clientId = state.clientId,
                accountType = accountType,
            ).collect { dataState ->
                if (dataState is DataState.Success) {
                    val accounts = when (accountType) {
                        Constants.SAVINGS_ACCOUNTS -> dataState.data.savingsAccounts.orEmpty()
                            .filter { it.status?.active == true }

                        Constants.LOAN_ACCOUNTS ->
                            dataState.data.loanAccounts
                                .filter { it.status?.active == true }

                        Constants.SHARE_ACCOUNTS ->
                            dataState.data.shareAccounts
                                .filter { it.status?.active == true }

                        else -> emptyList()
                    }

                    when (accountType) {
                        Constants.SAVINGS_ACCOUNTS -> sendAction(
                            ClientChargeAction.Internal.SavingsAccountsLoaded(
                                accounts.filterIsInstance<SavingAccount>(),
                            ),
                        )
                        Constants.LOAN_ACCOUNTS -> sendAction(
                            ClientChargeAction.Internal.LoanAccountsLoaded(
                                accounts.filterIsInstance<LoanAccount>(),
                            ),
                        )
                        Constants.SHARE_ACCOUNTS -> sendAction(
                            ClientChargeAction.Internal.ShareAccountsLoaded(
                                accounts.filterIsInstance<ShareAccount>(),
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun handleNetworkResult(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }
        if (!isOnline) {
            updateState { current ->
                if (current.uiState is ScreenUiState.Loading ||
                    current.uiState is ScreenUiState.Error ||
                    current.uiState is ScreenUiState.Empty ||
                    current.uiState is ScreenUiState.Network
                ) {
                    current.copy(uiState = ScreenUiState.Network)
                } else {
                    current
                }
            }
        } else {
            loadCharges()
            if (state.canSwitchAccounts) {
                fetchAccounts(Constants.SAVINGS_ACCOUNTS)
                fetchAccounts(Constants.LOAN_ACCOUNTS)
                fetchAccounts(Constants.SHARE_ACCOUNTS)
            } else {
                if (state.chargeType == ChargeType.SAVINGS) fetchAccounts(Constants.SAVINGS_ACCOUNTS)
                if (state.chargeType == ChargeType.LOAN) fetchAccounts(Constants.LOAN_ACCOUNTS)
                if (state.chargeType == ChargeType.SHARE) fetchAccounts(Constants.SHARE_ACCOUNTS)
            }
        }
    }

    private fun handleLoanOrSavingsChargesResult(result: DataState<List<Charge>>) {
        when (result) {
            is DataState.Loading -> updateState { it.copy(uiState = ScreenUiState.Loading) }
            is DataState.Error -> updateState {
                it.copy(
                    uiState = if (result.exception.cause is IOException) {
                        ScreenUiState.Network
                    } else {
                        ScreenUiState.Error(Res.string.feature_generic_error_server)
                    },
                )
            }
            is DataState.Success -> {
                updateState { it.copy(originalCharges = result.data) }
                applyLocalFilter()
            }
        }
    }

    private fun handleClientChargesResult(result: DataState<Page<Charge>>) {
        when (result) {
            is DataState.Loading -> updateState { it.copy(uiState = ScreenUiState.Loading) }
            is DataState.Error -> updateState {
                it.copy(
                    uiState = if (result.exception.cause is IOException) {
                        ScreenUiState.Network
                    } else {
                        ScreenUiState.Error(Res.string.feature_generic_error_server)
                    },
                )
            }
            is DataState.Success -> {
                updateState { it.copy(originalCharges = result.data.pageItems) }
                applyLocalFilter()
            }
        }
    }

    private fun applyLocalFilter() {
        val filter = state.activeFilter
        val originalList = state.originalCharges
        val filteredList = if (filter == ChargeFilterUtil.ALL) {
            originalList
        } else {
            originalList.filter { filter.matchCondition(it) }
        }

        updateState {
            it.copy(
                charges = filteredList,
                uiState = if (filteredList.isEmpty()) ScreenUiState.Empty else ScreenUiState.Success,
            )
        }
    }

    private fun loadCharges() {
        updateState { it.copy(uiState = ScreenUiState.Loading) }

        viewModelScope.launch {
            when (state.chargeType) {
                ChargeType.CLIENT -> processClientCharges()
                ChargeType.LOAN, ChargeType.SAVINGS, ChargeType.SHARE -> processLoanOrSavingsCharges()
            }
        }
    }

    private fun processClientCharges() {
        viewModelScope.launch {
            clientChargeRepositoryImp.getCharges(state.clientId)
                .collect { result ->
                    sendAction(ClientChargeAction.Internal.ReceiveClientChargesResult(result))
                }
        }
    }

    private fun processLoanOrSavingsCharges() {
        viewModelScope.launch {
            val idToFetch = state.chargeTypeId
            if (idToFetch == null) {
                updateState { it.copy(uiState = ScreenUiState.Empty) }
                return@launch
            }

            val flow = if (state.chargeType == ChargeType.SHARE) {
                clientChargeRepositoryImp.getShareAccountCharges(idToFetch)
            } else {
                clientChargeRepositoryImp.getLoanOrSavingsCharges(state.chargeType, idToFetch)
            }

            flow.collect { result ->
                sendAction(ClientChargeAction.Internal.ReceiveLoanOrSavingsChargesResult(result))
            }
        }
    }
}

data class ClientChargeState(
    val networkStatus: Boolean = false,
    val clientId: Long,
    val chargeType: ChargeType,
    val chargeTypeId: Long?,
    val isOnline: Boolean,
    val isEmpty: Boolean = false,
    val topBarTitleResId: StringResource = Res.string.charges,
    val charges: List<Charge> = emptyList(),
    val originalCharges: List<Charge> = emptyList(),

    val savingsAccounts: List<SavingAccount> = emptyList(),
    val loanAccounts: List<LoanAccount> = emptyList(),
    val shareAccounts: List<ShareAccount> = emptyList(),

    val selectedSavingsAccount: SavingAccount? = null,
    val selectedLoanAccount: LoanAccount? = null,
    val selectedShareAccount: ShareAccount? = null,

    val activeFilter: ChargeFilterUtil = ChargeFilterUtil.ALL,
    val showFilter: Boolean = false,
    val canSwitchAccounts: Boolean = true,

    val dialogState: DialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {

    val selectedAccountNo: String?
        get() = selectedSavingsAccount?.accountNo
            ?: selectedLoanAccount?.accountNo
            ?: selectedShareAccount?.accountNo

    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

sealed interface ClientChargeAction {

    data object RefreshCharges : ClientChargeAction
    data object OnNavigate : ClientChargeAction
    data object OnDismissDialog : ClientChargeAction
    data class OnChargeClick(val charge: Charge) : ClientChargeAction
    data class ReceiveNetworkResult(val isOnline: Boolean) : ClientChargeAction
    data object Retry : ClientChargeAction

    data object ToggleFilter : ClientChargeAction
    data object ClearFilter : ClientChargeAction

    data class ApplyFilter(
        val target: ChargeAccountTarget,
        val filter: ChargeFilterUtil,
    ) : ClientChargeAction

    sealed class Internal : ClientChargeAction {
        data class ReceiveLoanOrSavingsChargesResult(
            val result: DataState<List<Charge>>,
        ) : Internal()

        data class ReceiveClientChargesResult(
            val result: DataState<Page<Charge>>,
        ) : Internal()

        data class SavingsAccountsLoaded(val accounts: List<SavingAccount>) : Internal()
        data class LoanAccountsLoaded(val accounts: List<LoanAccount>) : Internal()
        data class ShareAccountsLoaded(val accounts: List<ShareAccount>) : Internal()
    }
}

sealed interface ClientChargeEvent {
    data class ShowToast(val message: String) : ClientChargeEvent
    data object Navigate : ClientChargeEvent
    data class OnChargeClick(val charge: Charge) : ClientChargeEvent
}

sealed class ChargeAccountTarget {

    data object AllAccounts : ChargeAccountTarget()

    data class Savings(val account: SavingAccount) : ChargeAccountTarget()
    data class Loan(val account: LoanAccount) : ChargeAccountTarget()
    data class Share(val account: ShareAccount) : ChargeAccountTarget()
}
