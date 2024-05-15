package org.mifos.mobile.ui.guarantor.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Constants.GUARANTOR_ADD_SCREEN_ROUTE_BASE
import org.mifos.mobile.utils.Constants.GUARANTOR_DETAILS
import org.mifos.mobile.utils.Constants.GUARANTOR_DETAIL_SCREEN_ROUTE_BASE
import org.mifos.mobile.utils.Constants.GUARANTOR_LIST_SCREEN_ROUTE_BASE
import org.mifos.mobile.utils.Constants.GUARANTOR_STATE
import org.mifos.mobile.utils.Constants.INDEX
import org.mifos.mobile.utils.Constants.LOAN_ID
import org.mifos.mobile.utils.Constants.LOAN_STATE
import org.mifos.mobile.utils.DateHelper
import javax.inject.Inject
import javax.inject.Scope
import javax.inject.Singleton

sealed class GuarantorScreen(val route: String) {
    data object GuarantorList : GuarantorScreen(
        route = "$GUARANTOR_LIST_SCREEN_ROUTE_BASE/{$LOAN_ID}"
    )

    data object GuarantorDetails : GuarantorScreen(
        route = "$GUARANTOR_DETAIL_SCREEN_ROUTE_BASE/{$LOAN_ID}/{$INDEX}"
    ) {
        fun passArguments(index: Int, loanId: Long) = "$GUARANTOR_DETAIL_SCREEN_ROUTE_BASE/$loanId/$index"
    }

    data object GuarantorAdd : GuarantorScreen(
        route = "$GUARANTOR_ADD_SCREEN_ROUTE_BASE/{$LOAN_ID}/{$INDEX}"
    ) {
        fun passArguments(index: Int, loanId: Long) = "$GUARANTOR_ADD_SCREEN_ROUTE_BASE/$loanId/$index"
    }
}