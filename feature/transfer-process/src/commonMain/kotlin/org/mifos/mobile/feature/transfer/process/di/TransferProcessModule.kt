/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.transfer.process.makeTransfer.MakeTransferViewModel
import org.mifos.mobile.feature.transfer.process.transferProcess.TransferProcessViewModel

/**
 * Koin module for providing dependencies related to the Transfer Process feature.
 *
 * This module declares the ViewModels used in the transfer process workflow,
 * allowing Koin's dependency injection framework to construct and provide them where needed.
 */
val TransferProcessModule = module {
    /**
     * Provides an instance of [TransferProcessViewModel].
     * This ViewModel manages the logic for the final transfer processing and authentication.
     */
    viewModelOf(::TransferProcessViewModel)
    /**
     * Provides an instance of [MakeTransferViewModel].
     * This ViewModel handles the logic for making transfers and form validation.
     */
    viewModelOf(::MakeTransferViewModel)
}
