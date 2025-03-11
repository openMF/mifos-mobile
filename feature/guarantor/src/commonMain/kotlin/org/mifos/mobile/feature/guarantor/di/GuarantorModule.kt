package org.mifos.mobile.feature.guarantor.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.mobile.feature.guarantor.screens.guarantorAdd.AddGuarantorViewModel
import org.mifos.mobile.feature.guarantor.screens.guarantorDetails.GuarantorDetailViewModel
import org.mifos.mobile.feature.guarantor.screens.guarantorList.GuarantorListViewModel

val GuarantorModule = module {
    viewModelOf(::AddGuarantorViewModel)
    viewModelOf(::GuarantorDetailViewModel)
    viewModelOf(::GuarantorListViewModel)
}