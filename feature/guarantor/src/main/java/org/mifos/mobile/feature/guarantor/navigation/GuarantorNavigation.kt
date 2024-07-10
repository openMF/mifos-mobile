package org.mifos.mobile.feature.guarantor.navigation

import org.mifos.mobile.core.common.Constants.INDEX
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute.GUARANTOR_ADD_SCREEN_ROUTE_BASE
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute.GUARANTOR_DETAIL_SCREEN_ROUTE_BASE
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute.GUARANTOR_LIST_SCREEN_ROUTE_BASE

sealed class GuarantorNavigation(val route: String) {
    data object GuarantorList : GuarantorNavigation(
        route = "$GUARANTOR_LIST_SCREEN_ROUTE_BASE/{$LOAN_ID}"
    )

    data object GuarantorDetails : GuarantorNavigation(
        route = "$GUARANTOR_DETAIL_SCREEN_ROUTE_BASE/{$LOAN_ID}/{$INDEX}"
    ) {
        fun passArguments(index: Int, loanId: Long) = "$GUARANTOR_DETAIL_SCREEN_ROUTE_BASE/$loanId/$index"
    }

    data object GuarantorAdd : GuarantorNavigation(
        route = "$GUARANTOR_ADD_SCREEN_ROUTE_BASE/{$LOAN_ID}/{$INDEX}"
    ) {
        fun passArguments(index: Int, loanId: Long) = "$GUARANTOR_ADD_SCREEN_ROUTE_BASE/$loanId/$index"
    }
}