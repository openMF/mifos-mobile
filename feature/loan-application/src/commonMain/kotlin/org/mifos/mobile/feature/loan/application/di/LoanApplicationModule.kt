/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.loan.application.confirmDetails.ConfirmDetailsViewModel
import org.mifos.mobile.feature.loan.application.loanApplication.LoanApplyViewModel
import org.mifos.mobile.feature.loan.application.loanProductDescription.LoanProductDetailsViewModel
import org.mifos.mobile.feature.loan.application.loanType.SelectLoanTypeViewModel
import org.mifos.mobile.feature.loan.application.uploadDocs.UploadDocsViewModel

val loanApplicationModule = module {
    viewModelOf(::SelectLoanTypeViewModel)
    viewModelOf(::LoanProductDetailsViewModel)
    viewModelOf(::LoanApplyViewModel)
    viewModelOf(::UploadDocsViewModel)
    viewModelOf(::ConfirmDetailsViewModel)
}
