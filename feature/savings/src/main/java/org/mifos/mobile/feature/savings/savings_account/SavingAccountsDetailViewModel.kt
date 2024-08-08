package org.mifos.mobile.feature.savings.savings_account

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.ui.theme.Blue
import org.mifos.mobile.core.ui.theme.DepositGreen
import org.mifos.mobile.core.ui.theme.LightYellow
import org.mifos.mobile.core.ui.theme.RedLight
import org.mifos.mobile.core.data.repositories.SavingsAccountRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.Status
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.network.Result
import org.mifos.mobile.core.network.asResult
import org.mifos.mobile.feature.qr.utils.QrCodeGenerator
import org.mifos.mobile.feature.savings.R
import javax.inject.Inject

@HiltViewModel
class SavingAccountsDetailViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    private var preferencesHelper: PreferencesHelper
) : ViewModel() {

    val savingsId = savedStateHandle.getStateFlow<Long?>(key = Constants.SAVINGS_ID, initialValue = null)

    val savingAccountsDetailUiState = savingsId
        .flatMapLatest {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                savingsId.value, Constants.TRANSACTIONS,
            )
        }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> SavingsAccountDetailUiState.Success(result.data)
                is Result.Loading -> SavingsAccountDetailUiState.Loading
                is Result.Error -> SavingsAccountDetailUiState.Error
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SavingsAccountDetailUiState.Loading
        )

    fun getQrString(savingsWithAssociations: SavingsWithAssociations?): String {
        return QrCodeGenerator.getAccountDetailsInString(
            savingsWithAssociations?.accountNo,
            preferencesHelper.officeName,
            AccountType.SAVINGS,
        )
    }
}

sealed class SavingsAccountDetailUiState {
    data object Loading : SavingsAccountDetailUiState()
    data object Error : SavingsAccountDetailUiState()
    data class Success(val savingAccount: SavingsWithAssociations) : SavingsAccountDetailUiState()
}

fun Status.getStatusColorAndText(): Pair<Color, Int> {
    return when {
        this.active == true -> Pair(DepositGreen, R.string.active)
        this.approved == true -> Pair(Blue, R.string.need_approval)
        this.submittedAndPendingApproval == true -> Pair(LightYellow, R.string.pending)
        this.matured == true -> Pair(RedLight, R.string.matured)
        else -> Pair(Color.Black, R.string.closed)
    }
}


