/*
 * Copyright 2025 Mifos Initiative
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
import org.mifos.mobile.core.network.services.ShareAccountService
import org.mifos.mobile.core.network.services.ThirdPartyTransferService
import org.mifos.mobile.core.network.services.UserDetailsService

interface DataManagerProvider {
    val clientsApi: ClientService?
    val authenticationApi: AuthenticationService?
    val registrationApi: RegistrationService?
    val userDetailsApi: UserDetailsService?
    val beneficiaryApi: BeneficiaryService?
    val loanAccountsListApi: LoanAccountsListService?
    val savingAccountsListApi: SavingAccountsListService?
    val recentTransactionsApi: RecentTransactionsService?
    val clientChargeApi: ClientChargeService?
    val thirdPartyTransferApi: ThirdPartyTransferService?
    val notificationApi: NotificationService?
    val guarantorApi: GuarantorService?
    val shareAccountApi: ShareAccountService?
}
