package org.mifos.mobile.api

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.api.local.DatabaseHelper
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.*
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.models.payload.LoginPayload
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.models.register.RegisterPayload
import org.mifos.mobile.models.register.UserVerify
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vishwajeet
 * @since 13/6/16.
 */
@Singleton
class DataManager @Inject constructor(
    val preferencesHelper: PreferencesHelper,
    private val baseApiManager: BaseApiManager,
    private val databaseHelper: DatabaseHelper,
) {
    var clientId: Long? = preferencesHelper.clientId
    suspend fun login(loginPayload: LoginPayload?): Response<User?>? {
        return baseApiManager.authenticationApi?.authenticate(loginPayload)
    }

    suspend fun clients(): Response<Page<Client?>?>? = baseApiManager.clientsApi.clients()

    suspend fun currentClient(): Client {
        return baseApiManager.clientsApi.getClientForId(clientId)
    }

    suspend fun clientImage(): ResponseBody {
        return baseApiManager.clientsApi.getClientImage(clientId)
    }

    suspend fun clientAccounts(): ClientAccounts {
        return baseApiManager.clientsApi.getClientAccounts(clientId)
    }

    suspend fun getAccounts(accountType: String?): ClientAccounts {
        return baseApiManager.clientsApi.getAccounts(clientId, accountType)
    }

    suspend fun getRecentTransactions(offset: Int, limit: Int): Response<Page<Transaction?>?>? {
        return baseApiManager.recentTransactionsApi
            ?.getRecentTransactionsList(clientId, offset, limit)
    }

    suspend fun getClientCharges(clientId: Long): Response<Page<Charge?>?>? {
        return baseApiManager.clientChargeApi?.getClientChargeList(clientId).apply {
            databaseHelper.syncCharges(this?.body())
        }
    }

    suspend fun getLoanCharges(loanId: Long): Response<List<Charge?>?>? {
        return baseApiManager.clientChargeApi?.getLoanAccountChargeList(loanId)
    }

    suspend fun getSavingsCharges(savingsId: Long): Response<List<Charge?>?>? {
        return baseApiManager.clientChargeApi?.getSavingsAccountChargeList(savingsId)
    }

    suspend fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?,
    ): Response<SavingsWithAssociations?>? {
        return baseApiManager
            .savingAccountsListApi?.getSavingsWithAssociations(accountId, associationType)
    }

    suspend fun accountTransferTemplate(): Response<AccountOptionsTemplate?>? =
        baseApiManager.savingAccountsListApi?.accountTransferTemplate()

    fun makeTransfer(transferPayload: TransferPayload?): Observable<ResponseBody?>? {
        return baseApiManager.savingAccountsListApi?.makeTransfer(transferPayload)
    }

    suspend fun getSavingAccountApplicationTemplate(client: Long?): Response<SavingsAccountTemplate?>? {
        return baseApiManager.savingAccountsListApi
            ?.getSavingsAccountApplicationTemplate(client)
    }

    suspend fun submitSavingAccountApplication(
        payload: SavingsAccountApplicationPayload?,
    ): Response<ResponseBody?>? {
        return baseApiManager.savingAccountsListApi?.submitSavingAccountApplication(payload)
    }

    suspend fun updateSavingsAccount(
        accountId: Long?,
        payload: SavingsAccountUpdatePayload?,
    ): Response<ResponseBody?>? {
        return baseApiManager.savingAccountsListApi
            ?.updateSavingsAccountUpdate(accountId, payload)
    }

    suspend fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?,
    ): Response<ResponseBody?>? {
        return baseApiManager.savingAccountsListApi
            ?.submitWithdrawSavingsAccount(accountId, payload)
    }

    fun getLoanAccountDetails(loanId: Long): Observable<LoanAccount?>? {
        return baseApiManager.loanAccountsListApi?.getLoanAccountsDetail(loanId)
    }

    suspend fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?,
    ): Response<LoanWithAssociations?>? {
        return baseApiManager.loanAccountsListApi
            ?.getLoanWithAssociations(loanId, associationType)
    }

    suspend fun loanTemplate(): Response<LoanTemplate?>? =
        baseApiManager.loanAccountsListApi?.getLoanTemplate(clientId)

    suspend fun getLoanTemplateByProduct(productId: Int?): Response<LoanTemplate?>? {
        return baseApiManager.loanAccountsListApi
            ?.getLoanTemplateByProduct(clientId, productId)
    }

    fun createLoansAccount(loansPayload: LoansPayload?): Observable<ResponseBody?>? {
        return baseApiManager.loanAccountsListApi?.createLoansAccount(loansPayload)
    }

    fun updateLoanAccount(loanId: Long, loansPayload: LoansPayload?): Observable<ResponseBody?>? {
        return baseApiManager.loanAccountsListApi?.updateLoanAccount(loanId, loansPayload)
    }

    suspend fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?,
    ): Response<ResponseBody?>? {
        return baseApiManager.loanAccountsListApi?.withdrawLoanAccount(loanId, loanWithdraw)
    }

    val beneficiaryList: Observable<List<Beneficiary?>?>?
        get() = baseApiManager.beneficiaryApi?.beneficiaryList
    val beneficiaryTemplate: Observable<BeneficiaryTemplate?>?
        get() = baseApiManager.beneficiaryApi?.beneficiaryTemplate

    fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Observable<ResponseBody?>? {
        return baseApiManager.beneficiaryApi?.createBeneficiary(beneficiaryPayload)
    }

    fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): Observable<ResponseBody?>? {
        return baseApiManager.beneficiaryApi?.updateBeneficiary(beneficiaryId, payload)
    }

    fun deleteBeneficiary(beneficiaryId: Long?): Observable<ResponseBody?>? {
        return baseApiManager.beneficiaryApi?.deleteBeneficiary(beneficiaryId)
    }

    val thirdPartyTransferTemplate: Observable<AccountOptionsTemplate?>?
        get() = baseApiManager.thirdPartyTransferApi?.accountTransferTemplate

    fun makeThirdPartyTransfer(transferPayload: TransferPayload?): Observable<ResponseBody?>? {
        return baseApiManager.thirdPartyTransferApi?.makeTransfer(transferPayload)
    }

    suspend fun registerUser(registerPayload: RegisterPayload?): Response<ResponseBody?>? {
        return baseApiManager.registrationApi?.registerUser(registerPayload)
    }

    suspend fun verifyUser(userVerify: UserVerify?): Response<ResponseBody?>? {
        return baseApiManager.registrationApi?.verifyUser(userVerify)
    }

    suspend fun clientLocalCharges(): Response<Page<Charge?>?> = databaseHelper.clientCharges()

    val notifications: Observable<List<MifosNotification?>?>
        get() = databaseHelper.notifications

    fun unreadNotificationsCount(): Int {
        return databaseHelper.unreadNotificationsCount()
    }

    suspend fun registerNotification(payload: NotificationRegisterPayload?): ResponseBody {
        return baseApiManager.notificationApi.registerNotification(payload)
    }

    suspend fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?,
    ): ResponseBody {
        return baseApiManager.notificationApi.updateRegisterNotification(id, payload)
    }

    suspend fun getUserNotificationId(id: Long): NotificationUserDetail {
        return baseApiManager.notificationApi.getUserNotificationId(id)
    }

    suspend fun updateAccountPassword(payload: UpdatePasswordPayload?): Response<ResponseBody?>? {
        return baseApiManager.userDetailsService?.updateAccountPassword(payload)
    }

    fun getGuarantorTemplate(loanId: Long?): Observable<GuarantorTemplatePayload?>? {
        return baseApiManager.guarantorApi?.getGuarantorTemplate(loanId)
            ?.onErrorResumeNext(
                Function<Throwable?, ObservableSource<out GuarantorTemplatePayload>> {
                    Observable.just(
                        FakeRemoteDataSource.guarantorTemplatePayload,
                    )
                },
            )
    }

    fun getGuarantorList(loanId: Long): Observable<List<GuarantorPayload?>?>? {
        return baseApiManager.guarantorApi?.getGuarantorList(loanId)
            ?.onErrorResumeNext(
                Function<Throwable?, ObservableSource<out List<GuarantorPayload>>> {
                    Observable.just(
                        FakeRemoteDataSource.guarantorsList,
                    )
                },
            )
    }

    fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?,
    ): Observable<ResponseBody?>? {
        return baseApiManager.guarantorApi?.createGuarantor(loanId, payload)
            ?.onErrorResumeNext(
                Function<Throwable?, ObservableSource<out ResponseBody>> {
                    val responseBody = ResponseBody.create(
                        MediaType
                            .parse("text/plain"),
                        "Guarantor Added Successfully",
                    )
                    Observable.just(responseBody)
                },
            )
    }

    fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?,
    ): Observable<ResponseBody?>? {
        return baseApiManager.guarantorApi?.updateGuarantor(payload, loanId, guarantorId)
            ?.onErrorResumeNext(
                Function<Throwable?, ObservableSource<out ResponseBody>> {
                    Observable.just(
                        ResponseBody.create(
                            MediaType
                                .parse("plain/text"),
                            "Guarantor Updated Successfully",
                        ),
                    )
                },
            )
    }

    fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Observable<ResponseBody?>? {
        return baseApiManager.guarantorApi?.deleteGuarantor(loanId, guarantorId)
            ?.onErrorResumeNext(
                Function<Throwable?, ObservableSource<out ResponseBody>> {
                    Observable.just(
                        ResponseBody.create(
                            MediaType
                                .parse("plain/text"),
                            "Guarantor Deleted Successfully",
                        ),
                    )
                },
            )
    }
}
