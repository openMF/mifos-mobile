/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.shareaccount.shareAccountDetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.share_account.generated.resources.Res
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_activation_date
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_application_date
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_approved_shares
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_currency
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_generic_error_server
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_market_price
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_number
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_pending_shares
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_product_name
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_status
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.ShareAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccountWithAssociations
import org.mifos.mobile.core.model.entity.templates.account.AccountType
import org.mifos.mobile.core.qr.getAccountDetailsInString
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.shareaccount.component.ShareActionItems
import org.mifos.mobile.feature.shareaccount.component.shareAccountActions

internal class ShareAccountDetailsViewModel(
    private val shareAccountRepository: ShareAccountRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ShareAccountDetailsState, ShareAccountDetailsEvent, ShareAccountDetailsAction>(
    initialState = run {
        val accountId = savedStateHandle.toRoute<ShareAccountDetailsRoute>().accountId
        ShareAccountDetailsState(
            accountId = accountId,
            items = shareAccountActions,
        )
    },
) {

    init {
        observeNetwork()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(ShareAccountDetailsAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    private fun fetchAccountDetails() {
        viewModelScope.launch {
            shareAccountRepository.getShareAccountDetails(state.accountId)
                .collect { result ->
                    sendAction(ShareAccountDetailsAction.Internal.ShareResultReceived(result))
                }
        }
    }

    override fun handleAction(action: ShareAccountDetailsAction) {
        when (action) {
            ShareAccountDetailsAction.OnNavigateBack -> sendEvent(ShareAccountDetailsEvent.NavigateBack)

            ShareAccountDetailsAction.OnRetry -> retry()

            is ShareAccountDetailsAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is ShareAccountDetailsAction.OnNavigateToAction ->
                sendEvent(ShareAccountDetailsEvent.NavigateToAction(action.route))

            is ShareAccountDetailsAction.Internal.ShareResultReceived ->
                handleShareAccountResult(action.dataState)

            ShareAccountDetailsAction.DismissDialog -> handleDismissDialog()
        }
    }

    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                fetchAccountDetails()
            }
        }
    }

    private fun handleNetworkStatus(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }
        viewModelScope.launch {
            if (isOnline) {
                fetchAccountDetails()
            } else if (state.uiState !is ScreenUiState.Success) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            }
        }
    }

    private fun handleShareAccountResult(dataState: DataState<ShareAccountWithAssociations>) {
        when (dataState) {
            is DataState.Error -> {
                updateState {
                    it.copy(
                        uiState = if (dataState.exception is IOException ||
                            dataState.exception.cause is IOException
                        ) {
                            ScreenUiState.Network
                        } else {
                            ScreenUiState.Error(Res.string.feature_share_account_generic_error_server)
                        },
                    )
                }
            }
            DataState.Loading -> updateState { it.copy(uiState = ScreenUiState.Loading) }
            is DataState.Success -> extractDetails(dataState.data)
        }
    }

    private fun extractDetails(account: ShareAccountWithAssociations) {
        val isActive = account.status?.active == true
        val currencyCode = account.currency?.code
        val decimals = account.currency?.decimalPlaces

        val appDate = account.timeline?.submittedOnDate?.let { DateHelper.getDateAsString(it) } ?: "-"
        val actDate = account.timeline?.activatedDate?.let { DateHelper.getDateAsString(it) } ?: "-"

        val displayItems = listOf(
            LabelValueItem(Res.string.feature_share_account_number, account.accountNo ?: "-"),
            LabelValueItem(Res.string.feature_share_account_product_name, account.productName ?: "-"),

            LabelValueItem(Res.string.feature_share_account_status, account.status?.value ?: "-"),
            LabelValueItem(
                Res.string.feature_share_account_currency,
                account.currency?.displayLabel ?: currencyCode ?: "",
            ),

            LabelValueItem(
                Res.string.feature_share_account_approved_shares,
                account.summary?.totalApprovedShares?.toString() ?: "0",
            ),
            LabelValueItem(
                Res.string.feature_share_account_pending_shares,
                account.summary?.totalPendingForApprovalShares?.toString() ?: "0",
            ),

            LabelValueItem(
                Res.string.feature_share_account_market_price,
                CurrencyFormatter.format(
                    account.currentMarketPrice ?: 0.0,
                    currencyCode,
                    decimals,
                ),
            ),

            LabelValueItem(Res.string.feature_share_account_application_date, appDate),
            LabelValueItem(Res.string.feature_share_account_activation_date, actDate),

        )

        val visibleActions: Set<ShareActionItems> = setOf(
            ShareActionItems.Charges,
            ShareActionItems.Transactions,
            ShareActionItems.QrCode,
        )

        updateState {
            it.copy(
                accountId = account.id ?: 0L,
                accountNumber = account.accountNo,
                clientName = account.clientName,
                isActive = isActive,
                displayItems = displayItems,
                allowedActions = visibleActions,
                uiState = ScreenUiState.Success,
            )
        }
    }

    private fun updateState(update: (ShareAccountDetailsState) -> ShareAccountDetailsState) {
        mutableStateFlow.update(update)
    }

    private fun handleDismissDialog() {
        mutableStateFlow.update { it.copy(dialogState = null) }
    }

    fun getQrString(): String {
        val officeName = userPreferencesRepository.userInfo.value.officeName
        return if (officeName.isNotEmpty()) {
            getAccountDetailsInString(
                clientName = state.clientName ?: "",
                accountNumber = state.accountNumber ?: "",
                accountType = AccountType(3, "accountType.share", "Share Account"),
                officeName = officeName,
            )
        } else {
            ""
        }
    }
}

@Immutable
internal data class ShareAccountDetailsState(
    val accountId: Long = -1L,
    val accountNumber: String? = "",
    val clientName: String? = "",
    val isActive: Boolean = false,
    val displayItems: List<LabelValueItem> = emptyList(),
    val items: ImmutableList<ShareActionItems> = shareAccountActions,
    val allowedActions: Set<ShareActionItems> = emptySet(),

    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
}

sealed interface ShareAccountDetailsEvent {
    data object NavigateBack : ShareAccountDetailsEvent
    data class NavigateToAction(val route: String) : ShareAccountDetailsEvent
}

sealed interface ShareAccountDetailsAction {
    data object OnNavigateBack : ShareAccountDetailsAction
    data object OnRetry : ShareAccountDetailsAction
    data class OnNavigateToAction(val route: String) : ShareAccountDetailsAction
    data object DismissDialog : ShareAccountDetailsAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : ShareAccountDetailsAction

    sealed interface Internal : ShareAccountDetailsAction {
        data class ShareResultReceived(val dataState: DataState<ShareAccountWithAssociations>) : Internal
    }
}
