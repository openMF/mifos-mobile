package org.mifos.mobile.feature.registration.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.mifos.mobile.feature.registration.screens.RegistrationScreen
import org.mifos.mobile.feature.registration.screens.RegistrationVerificationScreen

@Composable
fun RegistrationNavGraph(
    startDestination: String, navController: NavHostController, navigateBack: () -> Unit, startLoginActivity: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {

        registrationRoute(
            navigateBack = navigateBack,
            onVerified = { navController.navigate( RegistrationScreen.RegistrationVerification.route ) }
        )

        registrationVerificationRoute(
            navigateBack = { navController.popBackStack() },
            onVerified = { startLoginActivity.invoke() }
        )
    }
}


fun NavGraphBuilder.registrationRoute(
    navigateBack: () -> Unit,
    onVerified: () -> Unit
) {
    composable(route= RegistrationScreen.Registration.route){
        RegistrationScreen(
            onVerified = onVerified,
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.registrationVerificationRoute(
    navigateBack: () -> Unit,
    onVerified: () -> Unit
) {
    composable(route= RegistrationScreen.RegistrationVerification.route){
        RegistrationVerificationScreen(
            onVerified = onVerified,
            navigateBack = navigateBack
        )
    }
}
