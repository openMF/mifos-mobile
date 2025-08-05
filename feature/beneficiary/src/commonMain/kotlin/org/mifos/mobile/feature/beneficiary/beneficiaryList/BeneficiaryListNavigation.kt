package org.mifos.mobile.feature.beneficiary.beneficiaryList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.ui.composableWithPushTransitions

@Serializable
data object  BeneficiaryListNavRoute


fun NavController.navigateToBeneficiaryListScreen() {
    this.navigate(BeneficiaryListNavRoute)
}

fun NavGraphBuilder.beneficiaryListScreen(
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit,
    onBeneficiaryItemClick: (position: Int) -> Unit,
) {
    composableWithPushTransitions<BeneficiaryListNavRoute> {
        BeneficiaryListScreen(
            navigateBack = navigateBack,
            addBeneficiaryClicked = addBeneficiaryClicked,
            onBeneficiaryItemClick = onBeneficiaryItemClick,
        )
    }
}
