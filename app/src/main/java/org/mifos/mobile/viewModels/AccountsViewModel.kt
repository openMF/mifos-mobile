package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.functions.Predicate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.models.CheckboxStatus
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.accounts.share.ShareAccount
import org.mifos.mobile.repositories.AccountsRepository
import org.mifos.mobile.repositories.HomeRepository
import org.mifos.mobile.utils.AccountsFilterUtil
import org.mifos.mobile.utils.AccountsUiState
import org.mifos.mobile.utils.Constants
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountsRepositoryImp: AccountsRepository,
    private val homeRepositoryImp: HomeRepository
) : ViewModel() {

    private val _accountsUiState = MutableStateFlow<AccountsUiState>(AccountsUiState.Loading)
    val accountsUiState: StateFlow<AccountsUiState> = _accountsUiState

    /**
     * Loads savings, loan and share accounts associated with the Client from the server
     * and notifies the view to display it. And in case of any error during fetching the required
     * details it notifies the view.
     */
    fun loadClientAccounts() {
        viewModelScope.launch {
            _accountsUiState.value = AccountsUiState.Loading
            homeRepositoryImp.clientAccounts().catch {
                _accountsUiState.value = AccountsUiState.Error
            }.collect { clientAccounts ->
                _accountsUiState.value =
                    AccountsUiState.ShowSavingsAccounts(clientAccounts.savingsAccounts)
                _accountsUiState.value =
                    AccountsUiState.ShowLoanAccounts(clientAccounts.loanAccounts)
                _accountsUiState.value =
                    AccountsUiState.ShowShareAccounts(clientAccounts.shareAccounts)
            }
        }
    }

    /**
     * Loads savings, loan or share account depending upon `accountType` provided from the
     * server and notifies the view to display it.And in case of any error during fetching the
     * required details it notifies the view.
     * @param accountType Type of account for which we need to fetch details
     */
    fun loadAccounts(accountType: String?) {
        viewModelScope.launch {
            _accountsUiState.value = AccountsUiState.Loading
            accountsRepositoryImp.loadAccounts(accountType).catch {
                _accountsUiState.value = AccountsUiState.Error
            }.collect { clientAccounts ->
                when (accountType) {
                    Constants.SAVINGS_ACCOUNTS -> _accountsUiState.value =
                        AccountsUiState.ShowSavingsAccounts(clientAccounts.savingsAccounts)
                    Constants.LOAN_ACCOUNTS -> _accountsUiState.value =
                        AccountsUiState.ShowLoanAccounts(clientAccounts.loanAccounts)
                    Constants.SHARE_ACCOUNTS -> _accountsUiState.value =
                        AccountsUiState.ShowShareAccounts(clientAccounts.shareAccounts)
                }
            }

        }
    }

    /**
     * Filters [List] of [SavingAccount]
     * @param accounts [List] of [SavingAccount]
     * @param input [String] which is used for filtering
     * @return Returns [List] of filtered [SavingAccount] according to the `input`
     * provided.
     */
    fun searchInSavingsList(
        accounts: List<SavingAccount?>?,
        input: String?,
    ): List<SavingAccount?> {
        return Observable.fromIterable(accounts)
            .filter { (accountNo, productName) ->
                input?.lowercase(Locale.ROOT)
                    ?.let { productName?.lowercase(Locale.ROOT)?.contains(it) } == true ||
                        input?.lowercase(Locale.ROOT)?.let {
                            accountNo?.lowercase(Locale.ROOT)
                                ?.contains(it)
                        } == true
            }.toList().blockingGet()
    }

    /**
     * Filters [List] of [LoanAccount]
     * @param accounts [List] of [LoanAccount]
     * @param input [String] which is used for filtering
     * @return Returns [List] of filtered [LoanAccount] according to the `input`
     * provided.
     */
    fun searchInLoanList(
        accounts: List<LoanAccount?>?,
        input: String?,
    ): List<LoanAccount?>? {
        return Observable.fromIterable(accounts)
            .filter { (_, _, _, accountNo, productName) ->
                input?.lowercase(Locale.ROOT)
                    ?.let { productName?.lowercase(Locale.ROOT)?.contains(it) } == true ||
                        input?.lowercase(Locale.ROOT)?.let {
                            accountNo?.lowercase(Locale.ROOT)
                                ?.contains(it)
                        } == true
            }.toList().blockingGet()
    }

    /**
     * Filters [List] of [ShareAccount]
     * @param accounts [List] of [ShareAccount]
     * @param input [String] which is used for filtering
     * @return Returns [List] of filtered [ShareAccount] according to the `input`
     * provided.
     */
    fun searchInSharesList(
        accounts: Collection<ShareAccount?>?,
        input: String?,
    ): List<ShareAccount?>? {
        return Observable.fromIterable(accounts)
            .filter { (accountNo, _, _, _, productName) ->
                input?.lowercase(Locale.ROOT)
                    ?.let { productName?.lowercase(Locale.ROOT)?.contains(it) } == true ||
                        input?.lowercase(Locale.ROOT)?.let {
                            accountNo?.lowercase(Locale.ROOT)
                                ?.contains(it)
                        } == true
            }.toList().blockingGet()
    }

    /**
     * Filters [List] of [CheckboxStatus]
     * @param statusModelList [List] of [CheckboxStatus]
     * @return Returns [List] of [CheckboxStatus] which have
     * `checkboxStatus.isChecked()` as true.
     */
    fun getCheckedStatus(statusModelList: List<CheckboxStatus?>?): List<CheckboxStatus?>? {
        return Observable.fromIterable(statusModelList)
            .filter { (_, _, isChecked) -> isChecked }.toList().blockingGet()
    }

    /**
     * Filters [List] of [SavingAccount] according to [CheckboxStatus]
     * @param accounts [List] of filtered [SavingAccount]
     * @param status Used for filtering the [List]
     * @return Returns [List] of filtered [SavingAccount] according to the
     * `status` provided.
     */
    fun getFilteredSavingsAccount(
        accounts: List<SavingAccount?>?,
        status: CheckboxStatus?,
        accountsFilterUtil: AccountsFilterUtil
    ): Collection<SavingAccount?>? {
        return Observable.fromIterable(accounts)
            .filter(
                Predicate { (_, _, _, _, _, _, _, _, _, _, _, status1) ->
                    if (accountsFilterUtil.activeString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.active == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.approvedString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.approved == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.approvalPendingString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.submittedAndPendingApproval == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.maturedString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.matured == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.closedString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.closed == true
                    ) {
                        return@Predicate true
                    }
                    false
                },
            ).toList().blockingGet()
    }

    /**
     * Filters [List] of [LoanAccount] according to [CheckboxStatus]
     * @param accounts [List] of filtered [LoanAccount]
     * @param status Used for filtering the [List]
     * @return Returns [List] of filtered [LoanAccount] according to the
     * `status` provided.
     */
    fun getFilteredLoanAccount(
        accounts: List<LoanAccount?>?,
        status: CheckboxStatus?,
        accountsFilterUtil: AccountsFilterUtil
    ): Collection<LoanAccount?>? {
        return Observable.fromIterable(accounts)
            .filter(
                Predicate { (_, _, _, _, _, _, _, _, _, _, _, status1, _, _, _, _, _, inArrears) ->
                    if (accountsFilterUtil.inArrearsString?.let { status?.status?.compareTo(it) }
                        == 0 && inArrears == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.activeString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.active == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.waitingForDisburseString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.waitingForDisbursal == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.approvalPendingString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.pendingApproval == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.overpaidString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.overpaid == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.closedString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.closed == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.withdrawnString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.isLoanTypeWithdrawn() == true
                    ) {
                        return@Predicate true
                    }
                    false
                },
            ).toList().blockingGet()
    }

    /**
     * Filters [List] of [ShareAccount] according to [CheckboxStatus]
     * @param accounts [List] of filtered [ShareAccount]
     * @param status Used for filtering the [List]
     * @return Returns [List] of filtered [ShareAccount] according to the
     * `status` provided.
     */
    fun getFilteredShareAccount(
        accounts: List<ShareAccount?>?,
        status: CheckboxStatus?,
        accountsFilterUtil: AccountsFilterUtil
    ): Collection<ShareAccount?>? {
        return Observable.fromIterable(accounts)
            .filter(
                Predicate { (_, _, _, _, _, _, status1) ->
                    if (accountsFilterUtil.activeString
                            ?.let { status?.status?.compareTo(it) } == 0 &&
                        status1?.active == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.approvedString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.approved == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.approvalPendingString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.submittedAndPendingApproval == true
                    ) {
                        return@Predicate true
                    } else if (accountsFilterUtil.closedString
                            ?.let { status?.status?.compareTo(it) } == 0 && status1?.closed == true
                    ) {
                        return@Predicate true
                    }
                    false
                },
            ).toList().blockingGet()
    }
}