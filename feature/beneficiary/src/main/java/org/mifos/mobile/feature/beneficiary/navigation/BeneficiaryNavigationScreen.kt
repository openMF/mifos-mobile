package org.mifos.mobile.feature.beneficiary.navigation

import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryRoute.ADD_BENEFICIARY_SCREEN_ROUTE
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryRoute.BENEFICIARY_APPLICATION_SCREEN_ROUTE
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryRoute.BENEFICIARY_DETAIL_SCREEN_ROUTE
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryRoute.BENEFICIARY_LIST_ROUTE

sealed class BeneficiaryNavigation(val route: String) {
    data object AddBeneficiary: BeneficiaryNavigation(route = ADD_BENEFICIARY_SCREEN_ROUTE)
    data class BeneficiaryDetail(val beneficiary: Beneficiary?) :
        BeneficiaryNavigation(route = BENEFICIARY_DETAIL_SCREEN_ROUTE)
    data class BeneficiaryApplication(
       val beneficiaryState: BeneficiaryState?,
       val beneficiary: Beneficiary?,
    ) : BeneficiaryNavigation(route = BENEFICIARY_APPLICATION_SCREEN_ROUTE)
    data object BeneficiaryList : BeneficiaryNavigation(route = BENEFICIARY_LIST_ROUTE)
}