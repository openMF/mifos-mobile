package org.mifos.mobile.feature.savingsaccount.savingsAccountUpdate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_account_number_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_client_name_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_product_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_submission_date_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.templates.savings.ProductOptions
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.savingsaccount.components.savingsAccountActions
import org.mifos.mobile.feature.savingsaccount.savingsAccount.SavingsAccountRoute
import org.mifos.mobile.feature.savingsaccount.savingsAccountDetails.SavingsAccountDetailsRoute
import org.mifos.mobile.feature.savingsaccount.savingsAccountDetails.SavingsAccountDetailsState

internal class AccountUpdateViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle
): BaseViewModel<AccountUpdateState, AccountUpdateEvent, AccountUpdateAction>(
    initialState = run {
        val clientDetails = savedStateHandle.toRoute<SavingsAccountUpdateRoute>()
        val detailsMap = mapOf(
            Res.string.feature_savings_update_client_name_label to clientDetails.clientName,
            Res.string.feature_savings_update_submission_date_label to clientDetails.submissionData,
            Res.string.feature_savings_update_account_number_label to clientDetails.accountNumber,
            Res.string.feature_savings_update_product_label to clientDetails.product
        )
        val productOptions = mapOf(
            1L to "Wallet",
            2L to "FDP",
            4L to "1 year Fixed Deposit"

        )
        AccountUpdateState(
            clientId = requireNotNull(userPreferencesRepository.clientId.value),
            accountId = requireNotNull(clientDetails.accountId),
            details = detailsMap,
            productOptions = productOptions,
            dialogState = null,
        )
    }
)  {


//    TODO get savings product from server
//    init {
//        observeSavingsProducts()
//    }

//    private fun observeSavingsProducts() {
//        viewModelScope.launch {
//            savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(state.clientId)
//                .collect {
//                    sendAction(AccountUpdateAction.Internal.ReceiveProducts(it))
//                }
//        }
//    }


    private fun updateState(update: (AccountUpdateState) -> AccountUpdateState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: AccountUpdateAction) {
        when(action) {
            AccountUpdateAction.OnNavigateBack -> sendEvent(AccountUpdateEvent.NavigateBack)

            AccountUpdateAction.RequestUpdate -> handleRequestUpdate()

            is AccountUpdateAction.OnProductSelected -> handleProductChange(action.id, action.product)

//            TODO handle received products
            is AccountUpdateAction.Internal.ReceiveProducts -> { }

            is AccountUpdateAction.Internal.ReceiveUpdateRequestResult -> {
                viewModelScope.launch {
                    handleUpdateRequestResult(action.dataState)
                }
            }
        }
    }

    private fun handleProductChange(id: Long, product: String) {
        updateState { it.copy(selectedProductId = id, selectedProduct = product) }
    }

    private fun handleRequestUpdate() {
        updateState { it.copy(dialogState = AccountUpdateState.DialogState.Loading) }

        viewModelScope.launch {
            val response = savingsAccountRepositoryImp.updateSavingsAccount(
                accountId = state.accountId,
                payload = SavingsAccountUpdatePayload(
                    clientId = state.clientId,
                    productId = state.selectedProductId
                )
            )

            sendEvent(AccountUpdateEvent.NavigateToAuthenticate())
//            sendAction(AccountUpdateAction.Internal.ReceiveUpdateRequestResult(response))
        }
    }

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private suspend fun handleUpdateRequestResult(dataState: DataState<String>) {
        when(dataState) {

            is DataState.Loading -> updateState { it.copy(
                dialogState = AccountUpdateState.DialogState.Loading)
            }

            is DataState.Error -> updateState { it.copy(
                dialogState = AccountUpdateState.DialogState.Error(dataState.message))
            }

            is DataState.Success -> {
//                sendEvent(
//                    AccountUpdateEvent.NavigateToAuthenticate(
//                        eventType = EventType.SUCCESS.name,
//                        eventDestination = SavingsAccountRoute::class.serializer().descriptor.serialName,
//                        title = getString(Res.string.feature_signup_user_registered_successfully),
//                        subtitle = getString(Res.string.feature_signup_user_registered_successfully_tip),
//                        buttonText = getString(Res.string.feature_common_next),
//                    ),
//                )
            }
        }

    }

}

internal data class AccountUpdateState(
    val clientId: Long?,
    val accountId: Long?,
    val details: Map<StringResource, String?> = emptyMap(),
    val productOptions: Map<Long, String> = emptyMap(),
    val selectedProductId: Long? = null,
    val selectedProduct: String = "",

    val dialogState: DialogState?,
) {
    sealed interface DialogState {
        data class Error(val error: String): DialogState

        data object Loading: DialogState

    }
}

internal sealed interface AccountUpdateEvent {
    data object NavigateBack: AccountUpdateEvent

    data class NavigateToAuthenticate(
        val status: String = EventType.SUCCESS.name
    ) : AccountUpdateEvent

    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : AccountUpdateEvent
}

internal sealed interface AccountUpdateAction {
    data object OnNavigateBack: AccountUpdateAction

    data object RequestUpdate: AccountUpdateAction

    data class OnProductSelected(
        val id: Long,
        val product: String,
    ) : AccountUpdateAction

    sealed interface Internal: AccountUpdateAction {
        data class ReceiveProducts(val dataState: DataState<SavingsAccountTemplate>): Internal

        data class ReceiveUpdateRequestResult(val dataState: DataState<String>) : Internal
    }
}
