/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network

open class DataManager(
    private val ktorfitClient: KtorfitClient? = null,
) {

    open val authenticationApi by lazy { ktorfitClient!!.authenticationApi }

    open val beneficiaryApi by lazy { ktorfitClient!!.beneficiaryApi }

    open val clientsApi by lazy { ktorfitClient!!.clientsApi }

    open val loanAccountsListApi by lazy { ktorfitClient!!.loanAccountsListApi }

    open val savingAccountsListApi by lazy { ktorfitClient!!.savingAccountsListApi }

    open val recentTransactionsApi by lazy { ktorfitClient!!.recentTransactionsApi }

    open val clientChargeApi by lazy { ktorfitClient!!.clientChargeApi }

    open val thirdPartyTransferApi by lazy { ktorfitClient!!.thirdPartyTransferApi }

    open val registrationApi by lazy { ktorfitClient!!.registrationApi }

    open val notificationApi by lazy { ktorfitClient!!.notificationApi }

    open val userDetailsApi by lazy { ktorfitClient!!.userDetailsApi }

    open val guarantorApi by lazy { ktorfitClient!!.guarantorApi }

    open val shareAccountApi by lazy { ktorfitClient!!.shareAccountApi }

    open val pocketApi by lazy { ktorfitClient!!.pocketApi }
}
