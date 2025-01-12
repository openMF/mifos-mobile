/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network

import org.mifos.mobile.core.network.services.AuthenticationService
import org.mifos.mobile.core.network.services.BeneficiaryService
import org.mifos.mobile.core.network.services.ClientChargeService
import org.mifos.mobile.core.network.services.ClientService
import org.mifos.mobile.core.network.services.GuarantorService
import org.mifos.mobile.core.network.services.LoanAccountsListService
import org.mifos.mobile.core.network.services.NotificationService
import org.mifos.mobile.core.network.services.RecentTransactionsService
import org.mifos.mobile.core.network.services.RegistrationService
import org.mifos.mobile.core.network.services.SavingAccountsListService
import org.mifos.mobile.core.network.services.ThirdPartyTransferService
import org.mifos.mobile.core.network.services.UserDetailsService

class BaseApiManager(
    private val authenticationService: AuthenticationService,
    private val clientsService: ClientService,
    private val savingAccountsListService: SavingAccountsListService,
    private val loanAccountsListService: LoanAccountsListService,
    private val recentTransactionsService: RecentTransactionsService,
    private val clientChargeService: ClientChargeService,
    private val beneficiaryService: BeneficiaryService,
    private val thirdPartyTransferService: ThirdPartyTransferService,
    private val registrationService: RegistrationService,
    private val notificationService: NotificationService,
    private val userDetailsService: UserDetailsService,
    private val guarantorService: GuarantorService,
) {
    val authenticationApi: AuthenticationService
        get() = authenticationService
    val clientsApi: ClientService
        get() = clientsService
    val savingAccountsListApi: SavingAccountsListService
        get() = savingAccountsListService
    val loanAccountsListApi: LoanAccountsListService
        get() = loanAccountsListService
    val recentTransactionsApi: RecentTransactionsService
        get() = recentTransactionsService
    val clientChargeApi: ClientChargeService
        get() = clientChargeService
    val beneficiaryApi: BeneficiaryService
        get() = beneficiaryService
    val thirdPartyTransferApi: ThirdPartyTransferService
        get() = thirdPartyTransferService
    val registrationApi: RegistrationService
        get() = registrationService
    val notificationApi: NotificationService
        get() = notificationService
    val userDetailsApi: UserDetailsService
        get() = userDetailsService
    val guarantorApi: GuarantorService
        get() = guarantorService
}
