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

    val authenticationApi by lazy { ktorfitClient.authenticationApi }

    val beneficiaryApi by lazy { ktorfitClient.beneficiaryApi }

    val clientsApi by lazy { ktorfitClient.clientsApi }

    val loanAccountsListApi by lazy { ktorfitClient.loanAccountsListApi }

    val savingAccountsListApi by lazy { ktorfitClient.savingAccountsListApi }

    val recentTransactionsApi by lazy { ktorfitClient.recentTransactionsApi }

    val clientChargeApi by lazy { ktorfitClient.clientChargeApi }

    val thirdPartyTransferApi by lazy { ktorfitClient.thirdPartyTransferApi }

    val registrationApi by lazy { ktorfitClient.registrationApi }

    val notificationApi by lazy { ktorfitClient.notificationApi }

    val userDetailsApi by lazy { ktorfitClient.userDetailsApi }

    val guarantorApi by lazy { ktorfitClient.guarantorApi }
}
