package org.mifos.mobile.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import org.mifos.mobile.feature.guarantor.navigation.GuarantorNavigation
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute
import org.mifos.mobile.navigation.RootNavGraph

@AndroidEntryPoint
class GuarantorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loanId = intent.getLongExtra(LOAN_ID, -1)
        enableEdgeToEdge()
        setContent {
            MifosMobileTheme {
                val navController = rememberNavController()
                RootNavGraph(
                    startDestination = GuarantorRoute.GUARANTOR_NAVIGATION_ROUTE,
                    navController = navController,
                    nestedStartDestination = GuarantorNavigation.GuarantorList.route,
                    loanId = loanId,
                    navigateBack = { finish() }
                )
            }
        }
    }
}

