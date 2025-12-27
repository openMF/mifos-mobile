/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.fakeServices

import org.mifos.mobile.core.network.DataManagerProvider
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

class FakeDataManager(
    override val clientsApi: ClientService? = null,
    override val authenticationApi: AuthenticationService? = null,
    override val registrationApi: RegistrationService? = null,
    override val userDetailsApi: UserDetailsService? = null,
    override val beneficiaryApi: BeneficiaryService? = null,
    override val loanAccountsListApi: LoanAccountsListService? = null,
    override val savingAccountsListApi: SavingAccountsListService? = null,
    override val recentTransactionsApi: RecentTransactionsService? = null,
    override val clientChargeApi: ClientChargeService? = null,
    override val thirdPartyTransferApi: ThirdPartyTransferService? = null,
    override val notificationApi: NotificationService? = null,
    override val guarantorApi: GuarantorService? = null,
    override val shareAccountApi: ShareAccountService? = null,
) : DataManagerProvider
