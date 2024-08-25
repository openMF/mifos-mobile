package org.mifos.mobile.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.auth.navigation.AuthenticationNavigation
import org.mifos.mobile.navigation.RootNavGraph

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MifosMobileTheme {
                val navController = rememberNavController()
                RootNavGraph(
                    startDestination = AuthenticationNavigation.AuthenticationBase.route,
                    navController = navController,
                )
            }
        }
    }
}
