package org.mifos.mobile.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.mifos.mobile.feature.auth.navigation.AuthenticationNavigation
import org.mifos.mobile.feature.auth.navigation.authenticationNavGraph
import org.mifos.mobile.feature.guarantor.navigation.GuarantorNavigation
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute
import org.mifos.mobile.feature.guarantor.navigation.guarantorNavGraph
import org.mifos.mobile.ui.activities.PassCodeActivity

@Composable
fun RootNavGraph(
    navController: NavHostController,
    startDestination: String,
    nestedStartDestination: String,
    loanId: Long = -1, // we will remove this and pass this argument when its caller function is called in this NavGraph,
    navigateBack: () -> Unit = {} // remove this as well in future
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        authenticationNavGraph(
            navController = navController,
            navigateBack = { navController.popBackStack() },
            startDestination = nestedStartDestination,
            navigateToPasscodeScreen = { startPassCodeActivity(context = context) }
        )

        guarantorNavGraph(
            startDestination = nestedStartDestination,
            navController = navController,
            navigateBack = navigateBack,
            loanId = loanId
        )
    }
}

private fun startPassCodeActivity(context: Context) {
    val intent = Intent(context, PassCodeActivity::class.java)
    intent.putExtra(org.mifos.mobile.core.common.Constants.INTIAL_LOGIN, true)
    context.startActivity(intent)
}