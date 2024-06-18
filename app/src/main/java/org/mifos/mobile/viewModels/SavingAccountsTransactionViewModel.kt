//package org.mifos.mobile.viewModels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import io.reactivex.Observable
//import io.reactivex.functions.Predicate
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.launch
//import org.mifos.mobile.models.CheckboxStatus
//import org.mifos.mobile.models.accounts.savings.Transactions
//import org.mifos.mobile.repositories.SavingsAccountRepository
//import org.mifos.mobile.utils.CheckBoxStatusUtil
//import org.mifos.mobile.utils.Constants
//import org.mifos.mobile.utils.DateHelper
//import org.mifos.mobile.utils.SavingsAccountUiState
//import javax.inject.Inject
//
//@HiltViewModel
//class SavingAccountsTransactionViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
//    ViewModel() {
//
//    private val _savingAccountsTransactionUiState =
//        MutableStateFlow<SavingsAccountUiState>(SavingsAccountUiState.Initial)
//    val savingAccountsTransactionUiState: StateFlow<SavingsAccountUiState> get() = _savingAccountsTransactionUiState
//
//    /**
//     * Filters [List] of [CheckboxStatus]
//     * @param statusModelList [List] of [CheckboxStatus]
//     * @return Returns [List] of [CheckboxStatus] which have
//     * `checkboxStatus.isChecked()` as true.
//     */
//    fun getCheckedStatus(statusModelList: List<CheckboxStatus?>?): List<CheckboxStatus?>? {
//        return Observable.fromIterable(statusModelList)
//            .filter { (_, _, isChecked) -> isChecked }.toList().blockingGet()
//    }
//
//    /**
//     * Load details of a particular saving account from the server and notify the view
//     * to display it. Notify the view, in case there is any error in fetching
//     * the details from server.
//     *
//     * @param accountId Id of Savings Account
//     */
//    fun loadSavingsWithAssociations(accountId: Long) {
//        viewModelScope.launch {
//            _savingAccountsTransactionUiState.value = SavingsAccountUiState.Loading
//            savingsAccountRepositoryImp.getSavingsWithAssociations(
//                accountId,
//                Constants.TRANSACTIONS,
//            ).catch {
//                _savingAccountsTransactionUiState.value = SavingsAccountUiState.Error
//            }.collect {
//                _savingAccountsTransactionUiState.value =
//                    SavingsAccountUiState.SuccessLoadingSavingsWithAssociations(it)
//            }
//        }
//    }
//
//    /**
//     * Used for filtering [List] of [Transactions] according to `startDate` and
//     * `lastDate`
//     *
//     * @param savingAccountsTransactionList [List] of [Transactions]
//     * @param startDate                     Starting date for filtering
//     * @param lastDate                      Last date for filtering
//     */
//    fun filterTransactionList(
//        savingAccountsTransactionList: List<Transactions?>?,
//        startDate: Long?,
//        lastDate: Long?,
//    ) {
//        val list = when {
//            (startDate != null && lastDate != null) -> {
//                Observable.fromIterable(savingAccountsTransactionList)
//                    .filter { (_, _, _, _, date) ->
//                        (DateHelper.getDateAsLongFromList(date) in startDate..lastDate)
//                    }
//                    .toList().blockingGet()
//            }
//
//            else -> null
//        }
//        _savingAccountsTransactionUiState.value =
//            SavingsAccountUiState.ShowFilteredTransactionsList(list)
//    }
//
//    /**
//     * Filters [List] of [Transactions] according to [CheckboxStatus]
//     * @param savingAccountsTransactionList [List] of filtered [Transactions]
//     * @param status Used for filtering the [List]
//     * @return Returns [List] of filtered [Transactions] according to the
//     * `status` provided.
//     */
//    fun filterTransactionListByType(
//        savingAccountsTransactionList: List<Transactions?>?,
//        status: CheckboxStatus?,
//        checkBoxStatusUtil: CheckBoxStatusUtil
//    ): Collection<Transactions?>? {
//        return Observable.fromIterable(savingAccountsTransactionList)
//            .filter(
//                Predicate { (_, transactionType) ->
//
//                    when {
//                        ((checkBoxStatusUtil.depositString?.let { status?.status?.compareTo(it) } == 0) && (transactionType?.deposit!!)) ->
//                            return@Predicate true
//
//                        ((checkBoxStatusUtil.dividendPayoutString?.let {
//                            status?.status?.compareTo(
//                                it
//                            )
//                        } == 0) && (transactionType?.dividendPayout!!)) ->
//                            return@Predicate true
//
//                        ((checkBoxStatusUtil.withdrawalString?.let { status?.status?.compareTo(it) } == 0) && (transactionType?.withdrawal!!)) ->
//                            return@Predicate true
//
//                        ((checkBoxStatusUtil.interestPostingString?.let {
//                            status?.status?.compareTo(
//                                it
//                            )
//                        } == 0) && (transactionType?.interestPosting!!)) ->
//                            return@Predicate true
//
//                        ((checkBoxStatusUtil.feeDeductionString?.let { status?.status?.compareTo(it) } == 0) && (transactionType?.feeDeduction!!)) ->
//                            return@Predicate true
//
//                        ((checkBoxStatusUtil.withdrawalTransferString?.let {
//                            status?.status?.compareTo(
//                                it
//                            )
//                        } == 0) && (transactionType?.withdrawTransfer!!)) ->
//                            return@Predicate true
//
//                        ((checkBoxStatusUtil.rejectedTransferString?.let {
//                            status?.status?.compareTo(
//                                it
//                            )
//                        } == 0) && (transactionType?.rejectTransfer!!)) ->
//                            return@Predicate true
//
//                        ((checkBoxStatusUtil.overdraftFeeString?.let { status?.status?.compareTo(it) } == 0) && (transactionType?.overdraftFee!!)) ->
//                            return@Predicate true
//
//                        else -> false
//                    }
//                },
//            ).toList().blockingGet()
//    }
//}