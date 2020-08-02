package org.mifos.mobile

import com.google.gson.reflect.TypeToken

import org.mifos.mobile.models.*
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.models.payload.LoansPayload
import org.mifos.mobile.models.payload.LoginPayload
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.models.register.RegisterPayload
import org.mifos.mobile.models.register.UserVerify
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate

/**
 * Created by dilpreet on 26/6/17.
 */
object FakeRemoteDataSource {
    private val mTestDataFactory = TestDataFactory()
    val clientAccounts: ClientAccounts
        get() = mTestDataFactory.getListTypePojo<ClientAccounts>(object : TypeToken<ClientAccounts?>() {}, FakeJsonName.CLIENT_ACCOUNTS_JSON)
    val currentClient: Client
        get() = mTestDataFactory.getObjectTypePojo(Client::class.java,
                FakeJsonName.CLIENT_JSON)
    val clientSavingsAccount: ClientAccounts
        get() = mTestDataFactory.getObjectTypePojo(ClientAccounts::class.java,
                FakeJsonName.CLIENT_SAVINGS_ACCOUNT_JSON)
    val clientLoanAccount: ClientAccounts
        get() = mTestDataFactory.getObjectTypePojo(ClientAccounts::class.java,
                FakeJsonName.CLIENT_LOAN_ACCOUNT_JSON)
    val clientShareAccount: ClientAccounts
        get() = mTestDataFactory.getObjectTypePojo(ClientAccounts::class.java,
                FakeJsonName.CLIENT_SHARE_ACCOUNT_JSON)
    val beneficiaries: List<Beneficiary>
        get() = mTestDataFactory.getListTypePojo<List<Beneficiary>>(object : TypeToken<List<Beneficiary>?>() {}, FakeJsonName.BENEFICIARIES_JSON)
    val beneficiaryTemplate: BeneficiaryTemplate
        get() = mTestDataFactory.getObjectTypePojo(BeneficiaryTemplate::class.java,
                FakeJsonName.BENEFICIARY_TEMPLATE_JSON)

    fun beneficiaryPayload(): BeneficiaryPayload {
        return mTestDataFactory.getObjectTypePojo(BeneficiaryPayload::class.java,
                FakeJsonName.BENEFICIARY_PAYLOAD_JSON)
    }

    fun beneficiaryUpdatePayload(): BeneficiaryUpdatePayload {
        return mTestDataFactory.getObjectTypePojo(BeneficiaryUpdatePayload::class.java,
                FakeJsonName.BENEFICIARY_UPDATE_PAYLOAD_JSON)
    }

    val loanAccount: LoanAccount
        get() = mTestDataFactory.getObjectTypePojo(LoanAccount::class.java,
                FakeJsonName.LOAN_ACCOUNT_JSON)
    val loanTemplate: LoanTemplate
        get() = mTestDataFactory.getObjectTypePojo(LoanTemplate::class.java,
                FakeJsonName.LOAN_TEMPLATE_JSON)
    val loansPayload: LoansPayload
        get() = mTestDataFactory.getObjectTypePojo(LoansPayload::class.java,
                FakeJsonName.LOAN_PAYLOAD_JSON)
    val loanTemplateByTemplate: LoanTemplate
        get() = mTestDataFactory.getObjectTypePojo(LoanTemplate::class.java,
                FakeJsonName.LOAN_TEMPLATE_BY_PRODUCT_JSON)
    val loanAccountWithTransaction: LoanWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(LoanWithAssociations::class.java,
                FakeJsonName.LOAN_ACCOUNT_WITH_TRANSACTIONS_JSON)
    val loanAccountWithEmptyTransaction: LoanWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(LoanWithAssociations::class.java,
                FakeJsonName.LOAN_ACCOUNT_WITH_EMPTY_TRANSACTIONS_JSON)
    val loanAccountRepaymentSchedule: LoanWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(LoanWithAssociations::class.java,
                FakeJsonName.LOAN_ACCOUNT_WITH_REPAYMENT_SCHEDULE_JSON)
    val loanAccountEmptyRepaymentSchedule: LoanWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(LoanWithAssociations::class.java,
                FakeJsonName.LOAN_ACCOUNT_WITH_EMPTY_REPAYMENT_SCHEDULE_JSON)
    val user: User
        get() = mTestDataFactory.getObjectTypePojo(User::class.java,
                FakeJsonName.USER_JSON)
    val clients: Page<Client?>?
        get() = mTestDataFactory.getListTypePojo<Page<Client?>?>(object : TypeToken<Page<Client?>?>() {}, FakeJsonName.CLIENTS_JSON)
    val noClients: Page<Client?>?
        get() = mTestDataFactory.getListTypePojo<Page<Client?>?>(object : TypeToken<Page<Client?>?>() {}, FakeJsonName.CLIENTS_NOT_FOUND_JSON)
    val transactions: Page<Transaction?>?
        get() = mTestDataFactory.getListTypePojo<Page<Transaction?>?>(object : TypeToken<Page<Transaction?>?>() {}, FakeJsonName.TRANSACTIONS_JSON)
    val savingsWithAssociations: SavingsWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(SavingsWithAssociations::class.java,
                FakeJsonName.SAVINGS_WITH_ASSOCIATIONS)
    val accountOptionsTemplate: AccountOptionsTemplate
        get() = mTestDataFactory.getObjectTypePojo(AccountOptionsTemplate::class.java,
                FakeJsonName.ACCOUNT_OPTION_TEMPLATE)
    val transferPayload: TransferPayload
        get() = mTestDataFactory.getObjectTypePojo(TransferPayload::class.java,
                FakeJsonName.TRANFER_PAYLOAD_JSON)
    val registerPayload: RegisterPayload
        get() = mTestDataFactory.getObjectTypePojo(RegisterPayload::class.java,
                FakeJsonName.REGISTER)
    val loginPayload: LoginPayload
        get() = mTestDataFactory.getObjectTypePojo(LoginPayload::class.java,
                FakeJsonName.LOGIN)
    val charge: Page<Charge?>?
        get() = mTestDataFactory.getListTypePojo<Page<Charge?>?>(object : TypeToken<Page<Charge?>?>() {}, FakeJsonName.CHARGE)
    val savingsCharge: List<Charge>
        get() = mTestDataFactory.getListTypePojo<List<Charge>>(object : TypeToken<List<Charge>?>() {}, FakeJsonName.SAVING_CHARGE)
    val loanCharge: List<Charge>
        get() = mTestDataFactory.getListTypePojo<List<Charge>>(object : TypeToken<List<Charge>?>() {}, FakeJsonName.LOAN_CHARGE)
    val userVerify: UserVerify
        get() = mTestDataFactory.getObjectTypePojo(UserVerify::class.java,
                FakeJsonName.USER_VERIFY_JSON)
    val guarantorTemplatePayload: GuarantorTemplatePayload
        get() = mTestDataFactory.getObjectTypePojo(GuarantorTemplatePayload::class.java,
                FakeJsonName.GUARANTOR_TEMPLATE)
    val guarantorsList: List<GuarantorPayload>
        get() = mTestDataFactory.getListTypePojo<List<GuarantorPayload>>(object : TypeToken<List<GuarantorPayload>?>() {}, FakeJsonName.GUARANTOR_LIST)
    val savingAccountApplicationTemplate: SavingsAccountTemplate
        get() = mTestDataFactory.getObjectTypePojo(SavingsAccountTemplate::class.java,
                FakeJsonName.SAVINGS_ACCOUNT_TEMPLATE)
    val updatePasswordPayload: UpdatePasswordPayload
        get() = mTestDataFactory.getObjectTypePojo(UpdatePasswordPayload::class.java,
                FakeJsonName.UPDATE_PASSWORD_PAYLOAD_JSON)
}