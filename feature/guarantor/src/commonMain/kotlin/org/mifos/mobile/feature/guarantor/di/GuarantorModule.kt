/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.guarantor.screens.guarantorAdd.AddGuarantorViewModel
import org.mifos.mobile.feature.guarantor.screens.guarantorDetails.GuarantorDetailViewModel
import org.mifos.mobile.feature.guarantor.screens.guarantorList.GuarantorListViewModel

/**
 * Koin module for providing dependencies related to the Guarantor feature.
 *
 * This module declares the ViewModels used in the guarantor management workflow,
 * allowing Koin's dependency injection framework to construct and provide them where needed.
 */
val GuarantorModule = module {
    /**
     * Provides an instance of [AddGuarantorViewModel].
     * This ViewModel manages the logic for adding new guarantors.
     */
    viewModelOf(::AddGuarantorViewModel)
    /**
     * Provides an instance of [GuarantorDetailViewModel].
     * This ViewModel handles the logic for viewing guarantor details.
     */
    viewModelOf(::GuarantorDetailViewModel)
    /**
     * Provides an instance of [GuarantorListViewModel].
     * This ViewModel manages the logic for displaying the list of guarantors.
     */
    viewModelOf(::GuarantorListViewModel)
}
