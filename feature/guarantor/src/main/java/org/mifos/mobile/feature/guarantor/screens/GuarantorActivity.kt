package org.mifos.mobile.feature.guarantor.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import org.mifos.mobile.feature.guarantor.navigation.GuarantorScreen
import org.mifos.mobile.feature.guarantor.navigation.SetUpGuarantorNavGraph
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@AndroidEntryPoint
class GuarantorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loanId = intent.getLongExtra(LOAN_ID, -1)
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

