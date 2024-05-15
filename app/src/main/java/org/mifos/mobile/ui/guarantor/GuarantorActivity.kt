package org.mifos.mobile.ui.guarantor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.guarantor.navigation.GuarantorScreen
import org.mifos.mobile.ui.guarantor.navigation.SetUpGuarantorNavGraph
import org.mifos.mobile.utils.Constants

@AndroidEntryPoint
class GuarantorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loanId = intent.getLongExtra(Constants.LOAN_ID, -1)
        enableEdgeToEdge()
        setContent {
            MifosMobileTheme {
                val navController = rememberNavController()
                SetUpGuarantorNavGraph(
                    startDestination = GuarantorScreen.GuarantorList.route,
                    navController = navController,
                    loanId = loanId,
                    navigateBack = { finish() }
                )
            }
        }
    }
}

