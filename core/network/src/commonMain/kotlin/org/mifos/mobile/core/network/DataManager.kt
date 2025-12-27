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
) : DataManagerProvider {

    override val authenticationApi by lazy { ktorfitClient.authenticationApi }

    override val beneficiaryApi by lazy { ktorfitClient.beneficiaryApi }

    override val clientsApi by lazy { ktorfitClient.clientsApi }

    override val loanAccountsListApi by lazy { ktorfitClient.loanAccountsListApi }

    override val savingAccountsListApi by lazy { ktorfitClient.savingAccountsListApi }

    override val recentTransactionsApi by lazy { ktorfitClient.recentTransactionsApi }

    override val clientChargeApi by lazy { ktorfitClient.clientChargeApi }

    override val thirdPartyTransferApi by lazy { ktorfitClient.thirdPartyTransferApi }

    override val registrationApi by lazy { ktorfitClient.registrationApi }

    override val notificationApi by lazy { ktorfitClient.notificationApi }

    override val userDetailsApi by lazy { ktorfitClient.userDetailsApi }

    override val guarantorApi by lazy { ktorfitClient.guarantorApi }

    override val shareAccountApi by lazy { ktorfitClient.shareAccountApi }
}
