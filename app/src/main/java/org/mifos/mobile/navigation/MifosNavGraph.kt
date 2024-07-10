package org.mifos.mobile.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.mifos.mobile.feature.auth.navigation.AuthenticationNavigation
import org.mifos.mobile.feature.auth.navigation.authenticationNavGraph
import org.mifos.mobile.ui.activities.PassCodeActivity

@Composable
fun RootNavGraph(
    navController: NavHostController,
    startDestination: String,
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationNavGraph(
            navController = navController,
            navigateBack = { navController.popBackStack() },
            startDestination = AuthenticationNavigation.Login.route,
            navigateToPasscodeScreen = { startPassCodeActivity(context = context) }
        )
    }
}

private fun startPassCodeActivity(context: Context) {
    val intent = Intent(context, PassCodeActivity::class.java)
    intent.putExtra(org.mifos.mobile.core.common.Constants.INTIAL_LOGIN, true)
    context.startActivity(intent)
}