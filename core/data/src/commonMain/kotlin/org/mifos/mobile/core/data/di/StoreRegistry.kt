/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.di

import org.koin.core.qualifier.named

/**
 * Koin qualifier registry for Store 5 instances.
 * Each qualifier uniquely identifies a Store in the DI graph.
 */
object StoreRegistry {
    val Client = named("store_client")
    val Beneficiary = named("store_beneficiary")
    val Charge = named("store_charge")
    val Transaction = named("store_transaction")
    val Guarantor = named("store_guarantor")
    val LoanDetails = named("store_loan_details")
    val SavingsDetails = named("store_savings_details")
    val Accounts = named("store_accounts")
    val LoanTemplate = named("store_loan_template")
    val SavingsTemplate = named("store_savings_template")
    val ShareProducts = named("store_share_products")
    val AccountOptions = named("store_account_options")
}
