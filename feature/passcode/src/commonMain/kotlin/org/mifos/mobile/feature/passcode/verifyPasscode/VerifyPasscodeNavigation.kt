@file:Suppress("MatchingDeclarationName")


package org.mifos.mobile.feature.passcode.verifyPasscode

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithRootPushTransitions

sealed class VerifyPasscodeRoute {
    @Serializable
    data class Standard(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : VerifyPasscodeRoute()

    @Serializable
    data object Biometric : VerifyPasscodeRoute()

    @Serializable
    data object Pin : VerifyPasscodeRoute()
}

fun NavController.navigateToVerifyPasscodeScreen(
    navOptions: NavOptions? = null
) {
    navigate(VerifyPasscodeRoute.Standard, navOptions)
}

fun NavGraphBuilder.passcodeDestination(
    onPasscodeConfirm: (String, String, String, String, String) -> Unit,
) {
    composableWithRootPushTransitions<VerifyPasscodeRoute.Standard> {
        VerifyPasscodeScreen(
            onPasscodeConfirm = onPasscodeConfirm,
        )
    }
}