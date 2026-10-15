/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.savings.application.confirmDetails.SavingsConfirmDetailsViewModel
import org.mifos.mobile.feature.savings.application.fillApplication.SavingsFillApplicationViewModel
import org.mifos.mobile.feature.savings.application.savingsApplication.SavingsApplyViewModel

/**
 * Koin module for providing dependencies related to the Savings Application feature.
 *
 * This module declares the ViewModels used in the savings account application process,
 * allowing Koin's dependency injection framework to construct and provide them where needed.
 */
val savingsApplicationModule = module {
    /**
     * Provides an instance of [SavingsApplyViewModel].
     * This ViewModel manages the logic for the initial savings application screen.
     */
    viewModelOf(::SavingsApplyViewModel)
    /**
     * Provides an instance of [SavingsFillApplicationViewModel].
     * This ViewModel handles the logic for filling out the details of a new savings application.
     */
    viewModelOf(::SavingsFillApplicationViewModel)
    viewModelOf(::SavingsConfirmDetailsViewModel)
}
