package org.mifos.mobile.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.auth.navigation.AuthenticationNavigation
import org.mifos.mobile.feature.auth.navigation.AuthenticationRoute
import org.mifos.mobile.navigation.RootNavGraph
import org.mifos.mobile.ui.activities.base.BaseActivity

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MifosMobileTheme {
                val navController = rememberNavController()
                RootNavGraph(
                    startDestination = AuthenticationRoute.AUTH_NAVIGATION_ROUTE,
                    navController = navController,
                    nestedStartDestination = AuthenticationNavigation.Login.route,
                )
            }
        }
    }
}
