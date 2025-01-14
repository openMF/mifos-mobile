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

class DataManager(
    private val ktorfitClient: KtorfitClient,
) {

    internal val authenticationApi by lazy { ktorfitClient.authenticationApi }

    internal val beneficiaryApi by lazy { ktorfitClient.beneficiaryApi }

    internal val clientsApi by lazy { ktorfitClient.clientsApi }

    internal val loanAccountsListApi by lazy { ktorfitClient.loanAccountsListApi }

    internal val savingAccountsListApi by lazy { ktorfitClient.savingAccountsListApi }

    internal val recentTransactionsApi by lazy { ktorfitClient.recentTransactionsApi }

    internal val clientChargeApi by lazy { ktorfitClient.clientChargeApi }

    internal val thirdPartyTransferApi by lazy { ktorfitClient.thirdPartyTransferApi }

    internal val registrationApi by lazy { ktorfitClient.registrationApi }

    internal val notificationApi by lazy { ktorfitClient.notificationApi }

    internal val userDetailsApi by lazy { ktorfitClient.userDetailsApi }

    internal val guarantorApi by lazy { ktorfitClient.guarantorApi }
}
