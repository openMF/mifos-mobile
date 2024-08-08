package org.mifos.mobile.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import org.mifos.mobile.feature.guarantor.navigation.GuarantorNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuarantorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}

