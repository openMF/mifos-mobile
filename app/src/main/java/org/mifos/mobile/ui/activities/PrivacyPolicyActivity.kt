package org.mifos.mobile.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.databinding.ActivityPrivacyPolicyBinding
import org.mifos.mobile.feature.guarantor.navigation.GuarantorNavigation
import org.mifos.mobile.feature.guarantor.navigation.GuarantorRoute
import org.mifos.mobile.navigation.RootNavGraph
import org.mifos.mobile.ui.activities.base.BaseActivity

/**
 * @author Rajan Maurya
 * On 11/03/19.
 */
class PrivacyPolicyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
        }
    }
}
