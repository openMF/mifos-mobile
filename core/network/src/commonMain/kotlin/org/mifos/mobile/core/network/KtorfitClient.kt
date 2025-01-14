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

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import org.mifos.mobile.core.network.services.createAuthenticationService
import org.mifos.mobile.core.network.services.createBeneficiaryService
import org.mifos.mobile.core.network.services.createClientChargeService
import org.mifos.mobile.core.network.services.createClientService
import org.mifos.mobile.core.network.services.createGuarantorService
import org.mifos.mobile.core.network.services.createLoanAccountsListService
import org.mifos.mobile.core.network.services.createNotificationService
import org.mifos.mobile.core.network.services.createRecentTransactionsService
import org.mifos.mobile.core.network.services.createRegistrationService
import org.mifos.mobile.core.network.services.createSavingAccountsListService
import org.mifos.mobile.core.network.services.createThirdPartyTransferService
import org.mifos.mobile.core.network.services.createUserDetailsService
import org.mifos.mobile.core.network.utils.FlowConverterFactory

class KtorfitClient(
    ktorfit: Ktorfit,
) {

    internal val authenticationApi by lazy { ktorfit.createAuthenticationService() }

    internal val beneficiaryApi by lazy { ktorfit.createBeneficiaryService() }

    internal val clientsApi by lazy { ktorfit.createClientService() }

    internal val loanAccountsListApi by lazy { ktorfit.createLoanAccountsListService() }

    internal val savingAccountsListApi by lazy { ktorfit.createSavingAccountsListService() }

    internal val recentTransactionsApi by lazy { ktorfit.createRecentTransactionsService() }

    internal val clientChargeApi by lazy { ktorfit.createClientChargeService() }

    internal val thirdPartyTransferApi by lazy { ktorfit.createThirdPartyTransferService() }

    internal val registrationApi by lazy { ktorfit.createRegistrationService() }

    internal val notificationApi by lazy { ktorfit.createNotificationService() }

    internal val userDetailsApi by lazy { ktorfit.createUserDetailsService() }

    internal val guarantorApi by lazy { ktorfit.createGuarantorService() }

    class Builder internal constructor() {
        private lateinit var baseURL: String
        private lateinit var httpClient: HttpClient

        fun baseURL(baseURL: String): Builder {
            this.baseURL = baseURL
            return this
        }

        fun httpClient(ktorHttpClient: HttpClient): Builder {
            this.httpClient = ktorHttpClient
            return this
        }

        fun build(): KtorfitClient {
            val ktorfitBuilder = Ktorfit.Builder()
                .httpClient(httpClient)
                .baseUrl(baseURL)
                .converterFactories(FlowConverterFactory())
                .build()

            return KtorfitClient(ktorfitBuilder)
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}
