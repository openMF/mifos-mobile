/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositories

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.network.dto.accounts.AccountsResponseDto
import org.mifos.mobile.core.network.dto.auth.UserDto
import org.mifos.mobile.core.network.dto.beneficiary.BeneficiaryListResponseDto
import org.mifos.mobile.core.network.dto.charges.ChargeResponseDto
import org.mifos.mobile.core.network.dto.client.ClientResponseDto
import org.mifos.mobile.core.network.dto.common.PageResponseDto
import org.mifos.mobile.core.network.dto.guarantor.GuarantorListResponseDto
import org.mifos.mobile.core.network.dto.guarantor.GuarantorTemplateResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanAccountResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.notification.NotificationUserDetailResponseDto
import org.mifos.mobile.core.network.dto.payloads.BeneficiaryCreatePayloadDto
import org.mifos.mobile.core.network.dto.payloads.BeneficiaryUpdatePayloadDto
import org.mifos.mobile.core.network.dto.payloads.GuarantorApplicationPayloadDto
import org.mifos.mobile.core.network.dto.payloads.LoanAccountApplicationPayloadDto
import org.mifos.mobile.core.network.dto.payloads.LoanWithdrawPayloadDto
import org.mifos.mobile.core.network.dto.payloads.LoginPayloadDto
import org.mifos.mobile.core.network.dto.payloads.NotificationRegisterPayloadDto
import org.mifos.mobile.core.network.dto.payloads.RegisterPayloadDto
import org.mifos.mobile.core.network.dto.payloads.SavingsAccountUpdatePayloadDto
import org.mifos.mobile.core.network.dto.payloads.SavingsAccountWithdrawPayloadDto
import org.mifos.mobile.core.network.dto.payloads.ShareApplicationPayloadDto
import org.mifos.mobile.core.network.dto.payloads.TransferPayloadDto
import org.mifos.mobile.core.network.dto.payloads.UpdatePasswordPayloadDto
import org.mifos.mobile.core.network.dto.payloads.UserVerifyPayloadDto
import org.mifos.mobile.core.network.dto.pocket.PocketCommandResponse
import org.mifos.mobile.core.network.dto.pocket.PocketDelinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketLinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketResponseDto
import org.mifos.mobile.core.network.dto.products.share.ShareProductDetailsResponseDto
import org.mifos.mobile.core.network.dto.products.share.ShareProductResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsAccountApplicationPayloadDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.templates.accounts.AccountOptionsTemplateResponseDto
import org.mifos.mobile.core.network.dto.templates.beneficiary.BeneficiaryTemplateDto
import org.mifos.mobile.core.network.dto.templates.loan.LoanTemplateResponseDto
import org.mifos.mobile.core.network.dto.templates.savings.SavingsAccountTemplateResponseDto
import org.mifos.mobile.core.network.dto.transaction.LoanTransactionDetailsResponseDto
import org.mifos.mobile.core.network.dto.transaction.SavingsTransactionDetailsResponseDto
import org.mifos.mobile.core.network.dto.transaction.TransactionResponseDto
import org.mifos.mobile.core.network.services.AuthenticationService
import org.mifos.mobile.core.network.services.BeneficiaryService
import org.mifos.mobile.core.network.services.ClientChargeService
import org.mifos.mobile.core.network.services.ClientService
import org.mifos.mobile.core.network.services.GuarantorService
import org.mifos.mobile.core.network.services.LoanAccountsListService
import org.mifos.mobile.core.network.services.NotificationService
import org.mifos.mobile.core.network.services.PocketService
import org.mifos.mobile.core.network.services.RecentTransactionsService
import org.mifos.mobile.core.network.services.RegistrationService
import org.mifos.mobile.core.network.services.SavingAccountsListService
import org.mifos.mobile.core.network.services.ShareAccountService
import org.mifos.mobile.core.network.services.ThirdPartyTransferService
import org.mifos.mobile.core.network.services.UserDetailsService

open class BaseFakeClientService : ClientService {
    override fun clients(): Flow<PageResponseDto<ClientResponseDto>> = error("Not implemented")
    override fun getClientForId(clientId: Long): Flow<ClientResponseDto> = error("Not implemented")
    override fun getClientImage(clientId: Long): Flow<HttpResponse> = error("Not implemented")
    override fun getClientAccounts(clientId: Long): Flow<AccountsResponseDto> =
        error("Not implemented")
    override fun getAccounts(clientId: Long, accountType: String?): Flow<AccountsResponseDto> =
        error("Not implemented")
}

open class BaseFakeNotificationService : NotificationService {
    override fun getUserNotificationId(clientId: Long): Flow<NotificationUserDetailResponseDto> =
        error("Not implemented")
    override suspend fun registerNotification(payload: NotificationRegisterPayloadDto?): HttpResponse =
        error("Not implemented")
    override suspend fun updateRegisterNotification(
        clientId: Long,
        payload: NotificationRegisterPayloadDto?,
    ): HttpResponse = error("Not implemented")
}

open class BaseFakeBeneficiaryService : BeneficiaryService {
    override fun beneficiaryList(): Flow<List<BeneficiaryListResponseDto>> = error("Not implemented")
    override fun beneficiaryTemplate(): Flow<BeneficiaryTemplateDto> = error("Not implemented")
    override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryCreatePayloadDto?): HttpResponse =
        error("Not implemented")
    override suspend fun updateBeneficiary(
        beneficiaryId: Long,
        payload: BeneficiaryUpdatePayloadDto?,
    ): HttpResponse = error("Not implemented")
    override suspend fun deleteBeneficiary(beneficiaryId: Long): HttpResponse = error("Not implemented")
}

open class BaseFakeClientChargeService : ClientChargeService {
    override fun getClientChargeList(clientId: Long): Flow<PageResponseDto<ChargeResponseDto>> =
        error("Not implemented")
    override fun getChargeList(chargeType: String, chargeTypeId: Long): Flow<List<ChargeResponseDto>> =
        error("Not implemented")
}

open class BaseFakeGuarantorService : GuarantorService {
    override fun getGuarantorTemplate(loanId: Long): Flow<GuarantorTemplateResponseDto> = error("Not implemented")
    override fun getGuarantorList(loanId: Long): Flow<List<GuarantorListResponseDto>> = error("Not implemented")
    override suspend fun createGuarantor(loanId: Long, payload: GuarantorApplicationPayloadDto?): HttpResponse =
        error("Not implemented")
    override suspend fun updateGuarantor(
        payload: GuarantorApplicationPayloadDto?,
        loanId: Long,
        guarantorId: Long,
    ): HttpResponse = error("Not implemented")
    override suspend fun deleteGuarantor(loanId: Long, guarantorId: Long): HttpResponse = error("Not implemented")
}

open class BaseFakeLoanAccountsListService : LoanAccountsListService {
    override fun getLoanAccountsDetail(loanId: Long): Flow<LoanAccountResponseDto>? = error("Not implemented")
    override fun getLoanWithAssociations(
        loanId: Long,
        associationType: String?,
    ): Flow<LoanWithAssociationsResponseDto> = error("Not implemented")
    override fun getLoanTemplate(clientId: Long?): Flow<LoanTemplateResponseDto> =
        error("Not implemented")
    override fun getLoanTemplateByProduct(
        clientId: Long?,
        productId: Int?,
    ): Flow<LoanTemplateResponseDto> = error("Not implemented")
    override suspend fun createLoansAccount(
        loansPayload: LoanAccountApplicationPayloadDto?,
    ): HttpResponse = error("Not implemented")
    override suspend fun updateLoanAccount(
        loanId: Long,
        loansPayload: LoanAccountApplicationPayloadDto?,
    ): HttpResponse = error("Not implemented")
    override suspend fun withdrawLoanAccount(
        loanId: Long,
        loanWithdraw: LoanWithdrawPayloadDto?,
    ): HttpResponse = error("Not implemented")
    override fun getLoanTransactionDetails(
        loanId: Long,
        transactionId: Long,
    ): Flow<LoanTransactionDetailsResponseDto> = error("Not implemented")
}

open class BaseFakeRecentTransactionsService : RecentTransactionsService {
    override fun getRecentTransactionsList(
        clientId: Long,
        offset: Int?,
        limit: Int?,
    ): Flow<PageResponseDto<TransactionResponseDto>> = error("Not implemented")
}

open class BaseFakeSavingAccountsListService : SavingAccountsListService {
    override fun getSavingsWithAssociations(
        accountId: Long,
        associationType: String?,
    ): Flow<SavingsWithAssociationsResponseDto> = error("Not implemented")
    override fun accountTransferTemplate(
        accountId: Long?,
        accountType: Long?,
    ): Flow<AccountOptionsTemplateResponseDto> = error("Not implemented")
    override suspend fun makeTransfer(transferPayload: TransferPayloadDto?): HttpResponse = error("Not implemented")
    override fun getSavingsAccountApplicationTemplate(clientId: Long?): Flow<SavingsAccountTemplateResponseDto> =
        error("Not implemented")
    override fun getSavingsAccountApplicationTemplateByProduct(
        clientId: Long?,
        productId: Long?,
    ): Flow<SavingsAccountTemplateResponseDto> = error("Not implemented")
    override suspend fun submitSavingAccountApplication(
        payload: SavingsAccountApplicationPayloadDto?,
    ): HttpResponse = error("Not implemented")
    override suspend fun updateSavingsAccountUpdate(
        accountsId: Long,
        payload: SavingsAccountUpdatePayloadDto?,
    ): HttpResponse = error("Not implemented")
    override suspend fun submitWithdrawSavingsAccount(
        savingsId: Long,
        payload: SavingsAccountWithdrawPayloadDto?,
    ): HttpResponse = error("Not implemented")
    override fun getSavingsAccountTransactionDetails(
        savingsId: Long,
        transactionId: Long,
    ): Flow<SavingsTransactionDetailsResponseDto> = error("Not implemented")
}

open class BaseFakeThirdPartyTransferService : ThirdPartyTransferService {
    override fun accountTransferTemplate(): Flow<AccountOptionsTemplateResponseDto> = error("Not implemented")
    override suspend fun makeTransfer(transferPayload: TransferPayloadDto?): HttpResponse = error("Not implemented")
}

open class BaseFakeAuthenticationService : AuthenticationService {
    override suspend fun authenticate(loginPayload: LoginPayloadDto): UserDto = error("Not implemented")
}

open class BaseFakeUserDetailsService : UserDetailsService {
    override suspend fun updateAccountPassword(payload: UpdatePasswordPayloadDto?): HttpResponse =
        error("Not implemented")
}

open class BaseFakeRegistrationService : RegistrationService {
    override suspend fun registerUser(registerPayload: RegisterPayloadDto?): HttpResponse = error("Not implemented")
    override suspend fun verifyUser(userVerify: UserVerifyPayloadDto?): HttpResponse = error("Not implemented")
}

open class BaseFakeShareAccountService : ShareAccountService {
    override fun getShareProducts(clientId: Long?): Flow<PageResponseDto<ShareProductResponseDto>> =
        error("Not implemented")
    override fun getShareProductById(
        productId: Long,
        clientId: Long?,
    ): Flow<ShareProductDetailsResponseDto> = error("Not implemented")
    override suspend fun submitShareApplication(
        payload: ShareApplicationPayloadDto?,
    ): HttpResponse = error("Not implemented")
    override fun getShareAccountDetails(
        accountId: Long,
        associations: String,
    ): Flow<ShareWithAssociationsResponseDto> = error("Not implemented")
}

open class BaseFakePocketService : PocketService {
    override suspend fun getPocketAccounts(): PocketResponseDto = error("Not implemented")
    override suspend fun linkAccounts(
        command: String,
        request: PocketLinkRequest,
    ): PocketCommandResponse = error("Not implemented")
    override suspend fun delinkAccounts(
        command: String,
        request: PocketDelinkRequest,
    ): PocketCommandResponse = error("Not implemented")
}
