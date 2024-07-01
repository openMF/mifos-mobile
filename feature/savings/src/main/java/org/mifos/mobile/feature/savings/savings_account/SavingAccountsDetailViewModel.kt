package org.mifos.mobile.feature.savings.savings_account

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.ui.theme.Blue
import org.mifos.mobile.core.ui.theme.DepositGreen
import org.mifos.mobile.core.ui.theme.LightYellow
import org.mifos.mobile.core.ui.theme.RedLight
import org.mifos.mobile.core.data.repositories.SavingsAccountRepository
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.Status
import org.mifos.mobile.feature.savings.R
import javax.inject.Inject

@HiltViewModel
class SavingAccountsDetailViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
    ViewModel() {

    private val _savingAccountsDetailUiState =
        mutableStateOf<SavingsAccountDetailUiState>(SavingsAccountDetailUiState.Loading)
    val savingAccountsDetailUiState: State<SavingsAccountDetailUiState>
        get() = _savingAccountsDetailUiState

    private var _savingsId: Long? = 0
    val savingsId: Long? get() = _savingsId

    /**
     * Load details of a particular saving account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     *
     * @param accountId Id of Savings Account
     */
    fun loadSavingsWithAssociations(accountId: Long?) {
        viewModelScope.launch {
            _savingAccountsDetailUiState.value = SavingsAccountDetailUiState.Loading
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                accountId, org.mifos.mobile.core.common.Constants.TRANSACTIONS,
            ).catch {
                _savingAccountsDetailUiState.value = SavingsAccountDetailUiState.Error
            }.collect {
                _savingAccountsDetailUiState.value = SavingsAccountDetailUiState.Success(it)
            }
        }
    }

    fun setSavingsId(savingsId: Long?) {
        _savingsId = savingsId
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


