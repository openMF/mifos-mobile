/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.util

import com.google.gson.reflect.TypeToken
import org.mifos.mobile.core.database.entity.ChargeEntity
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.UpdatePasswordPayload
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.entity.payload.LoginPayload
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.entity.register.RegisterPayload
import org.mifos.mobile.core.model.entity.register.UserVerify
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate

object FakeRemoteDataSource {
    private val mTestDataFactory = TestDataFactory()
    val clientAccounts: ClientAccounts
        get() = mTestDataFactory.getListTypePojo(
            object :
                TypeToken<ClientAccounts?>() {},
            FakeJsonName.CLIENT_ACCOUNTS_JSON,
        )
    val currentClient: Client
        get() = mTestDataFactory.getObjectTypePojo(
            Client::class.java,
            FakeJsonName.CLIENT_JSON,
        )
    val clientSavingsAccount: ClientAccounts
        get() = mTestDataFactory.getObjectTypePojo(
            ClientAccounts::class.java,
            FakeJsonName.CLIENT_SAVINGS_ACCOUNT_JSON,
        )
    val clientLoanAccount: ClientAccounts
        get() = mTestDataFactory.getObjectTypePojo(
            ClientAccounts::class.java,
            FakeJsonName.CLIENT_LOAN_ACCOUNT_JSON,
        )
    val clientShareAccount: ClientAccounts
        get() = mTestDataFactory.getObjectTypePojo(
            ClientAccounts::class.java,
            FakeJsonName.CLIENT_SHARE_ACCOUNT_JSON,
        )
    val beneficiaries: List<Beneficiary>
        get() = mTestDataFactory.getListTypePojo(
            object :
                TypeToken<List<Beneficiary>?>() {},
            FakeJsonName.BENEFICIARIES_JSON,
        )
    val beneficiaryTemplate: BeneficiaryTemplate
        get() = mTestDataFactory.getObjectTypePojo(
            BeneficiaryTemplate::class.java,
            FakeJsonName.BENEFICIARY_TEMPLATE_JSON,
        )

    fun beneficiaryPayload(): BeneficiaryPayload {
        return mTestDataFactory.getObjectTypePojo(
            BeneficiaryPayload::class.java,
            FakeJsonName.BENEFICIARY_PAYLOAD_JSON,
        )
    }

    fun beneficiaryUpdatePayload(): BeneficiaryUpdatePayload {
        return mTestDataFactory.getObjectTypePojo(
            BeneficiaryUpdatePayload::class.java,
            FakeJsonName.BENEFICIARY_UPDATE_PAYLOAD_JSON,
        )
    }

    val loanAccount: LoanAccount
        get() = mTestDataFactory.getObjectTypePojo(
            LoanAccount::class.java,
            FakeJsonName.LOAN_ACCOUNT_JSON,
        )
    val loanTemplate: LoanTemplate
        get() = mTestDataFactory.getObjectTypePojo(
            LoanTemplate::class.java,
            FakeJsonName.LOAN_TEMPLATE_JSON,
        )
    val loansPayload: LoansPayload
        get() = mTestDataFactory.getObjectTypePojo(
            LoansPayload::class.java,
            FakeJsonName.LOAN_PAYLOAD_JSON,
        )
    val loanTemplateByTemplate: LoanTemplate
        get() = mTestDataFactory.getObjectTypePojo(
            LoanTemplate::class.java,
            FakeJsonName.LOAN_TEMPLATE_BY_PRODUCT_JSON,
        )
    val loanAccountWithTransaction: LoanWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(
            LoanWithAssociations::class.java,
            FakeJsonName.LOAN_ACCOUNT_WITH_TRANSACTIONS_JSON,
        )
    val loanAccountWithEmptyTransaction: LoanWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(
            LoanWithAssociations::class.java,
            FakeJsonName.LOAN_ACCOUNT_WITH_EMPTY_TRANSACTIONS_JSON,
        )
    val loanAccountRepaymentSchedule: LoanWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(
            LoanWithAssociations::class.java,
            FakeJsonName.LOAN_ACCOUNT_WITH_REPAYMENT_SCHEDULE_JSON,
        )
    val loanAccountEmptyRepaymentSchedule: LoanWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(
            LoanWithAssociations::class.java,
            FakeJsonName.LOAN_ACCOUNT_WITH_EMPTY_REPAYMENT_SCHEDULE_JSON,
        )
    val user: User
        get() = mTestDataFactory.getObjectTypePojo(
            User::class.java,
            FakeJsonName.USER_JSON,
        )
    val clients: Page<Client?>?
        get() = mTestDataFactory.getListTypePojo<Page<Client?>?>(
            object :
                TypeToken<Page<Client?>?>() {},
            FakeJsonName.CLIENTS_JSON,
        )
    val noClients: Page<Client?>?
        get() = mTestDataFactory.getListTypePojo<Page<Client?>?>(
            object :
                TypeToken<Page<Client?>?>() {},
            FakeJsonName.CLIENTS_NOT_FOUND_JSON,
        )
    val transactions: Page<Transaction?>?
        get() = mTestDataFactory.getListTypePojo<Page<Transaction?>?>(
            object :
                TypeToken<Page<Transaction?>?>() {},
            FakeJsonName.TRANSACTIONS_JSON,
        )
    val savingsWithAssociations: SavingsWithAssociations
        get() = mTestDataFactory.getObjectTypePojo(
            SavingsWithAssociations::class.java,
            FakeJsonName.SAVINGS_WITH_ASSOCIATIONS,
        )
    val accountOptionsTemplate: AccountOptionsTemplate
        get() = mTestDataFactory.getObjectTypePojo(
            AccountOptionsTemplate::class.java,
            FakeJsonName.ACCOUNT_OPTION_TEMPLATE,
        )
    val transferPayload: TransferPayload
        get() = mTestDataFactory.getObjectTypePojo(
            TransferPayload::class.java,
            FakeJsonName.TRANFER_PAYLOAD_JSON,
        )
    val registerPayload: RegisterPayload
        get() = mTestDataFactory.getObjectTypePojo(
            RegisterPayload::class.java,
            FakeJsonName.REGISTER,
        )
    val loginPayload: LoginPayload
        get() = mTestDataFactory.getObjectTypePojo(
            LoginPayload::class.java,
            FakeJsonName.LOGIN,
        )
    val charge: Page<ChargeEntity?>?
        get() = mTestDataFactory.getListTypePojo<Page<ChargeEntity?>?>(
            object :
                TypeToken<Page<ChargeEntity?>?>() {},
            FakeJsonName.CHARGE,
        )
    val savingsCharge: List<ChargeEntity>
        get() = mTestDataFactory.getListTypePojo(
            object :
                TypeToken<List<ChargeEntity>?>() {},
            FakeJsonName.SAVING_CHARGE,
        )
    val loanCharge: List<ChargeEntity>
        get() = mTestDataFactory.getListTypePojo(
            object :
                TypeToken<List<ChargeEntity>?>() {},
            FakeJsonName.LOAN_CHARGE,
        )
    val userVerify: UserVerify
        get() = mTestDataFactory.getObjectTypePojo(
            UserVerify::class.java,
            FakeJsonName.USER_VERIFY_JSON,
        )
    val guarantorTemplatePayload: GuarantorTemplatePayload
        get() = mTestDataFactory.getObjectTypePojo(
            GuarantorTemplatePayload::class.java,
            FakeJsonName.GUARANTOR_TEMPLATE,
        )
    val guarantorsList: List<GuarantorPayload>
        get() = mTestDataFactory.getListTypePojo(
            object :
                TypeToken<List<GuarantorPayload>?>() {},
            FakeJsonName.GUARANTOR_LIST,
        )
    val savingAccountApplicationTemplate: SavingsAccountTemplate
        get() = mTestDataFactory.getObjectTypePojo(
            SavingsAccountTemplate::class.java,
            FakeJsonName.SAVINGS_ACCOUNT_TEMPLATE,
        )
    val updatePasswordPayload: UpdatePasswordPayload
        get() = mTestDataFactory.getObjectTypePojo(
            UpdatePasswordPayload::class.java,
            FakeJsonName.UPDATE_PASSWORD_PAYLOAD_JSON,
        )
}
