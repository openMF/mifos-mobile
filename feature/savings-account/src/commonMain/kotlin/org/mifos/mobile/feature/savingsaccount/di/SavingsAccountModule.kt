/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.savingsaccount.savingsAccount.SavingsAccountViewmodel
import org.mifos.mobile.feature.savingsaccount.savingsAccountDetails.SavingsAccountDetailsViewModel
import org.mifos.mobile.feature.savingsaccount.savingsAccountUpdate.AccountUpdateViewModel
import org.mifos.mobile.feature.savingsaccount.savingsAccountWithdraw.AccountWithdrawViewModel

/**
 * Koin module for providing dependencies related to the Savings Account feature.
 *
 * This module declares all the ViewModels used across the savings account screens,
 * allowing Koin's dependency injection framework to construct and provide them where needed.
 * The `viewModelOf` factory automatically handles the resolution of constructor dependencies for each ViewModel.
 */
val savingsAccountModule = module {
    /**
     * Provides an instance of [SavingsAccountViewmodel].
     * This ViewModel is responsible for the main savings account list screen.
     */
    viewModelOf(::SavingsAccountViewmodel)
    /**
     * Provides an instance of [SavingsAccountDetailsViewModel].
     * This ViewModel manages the logic for the savings account details screen.
     */
    viewModelOf(::SavingsAccountDetailsViewModel)
    /**
     * Provides an instance of [AccountUpdateViewModel].
     * This ViewModel handles the logic for updating savings account details.
     */
    viewModelOf(::AccountUpdateViewModel)
    /**
     * Provides an instance of [AccountWithdrawViewModel].
     * This ViewModel is responsible for the account withdrawal screen logic.
     */
    viewModelOf(::AccountWithdrawViewModel)
}
