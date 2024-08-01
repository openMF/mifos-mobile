package org.mifos.mobile.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.FloatingActionButtonContent
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosIcons
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.feature.account.client_account.screens.ClientAccountsTabRow
import org.mifos.mobile.feature.account.client_account.utils.ClientAccountsScreenTopBar

@Composable
fun PrivacyPolicyScreen(
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    MFScaffold(
        topBarTitleResId= R.string.privacy_policy,
        navigateBack= navigateBack,
        scaffoldContent = {
            WebView(
                url = context.getString(R.string.privacy_policy_host_url)
            )
        }
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    url: String
) {

    var isLoading by remember { mutableStateOf(true) }

    Column {
        Spacer(modifier = Modifier.height(20.dp))
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    overScrollMode = WebView.OVER_SCROLL_NEVER
                    this.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                            return if (Uri.parse(url)?.host?.endsWith(context.getString(R.string.privacy_policy_host)) == true) {
                                false
                            } else {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                true
                            }
                        }

                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading = false
                        }
                    }
                    loadUrl(url)
                }
            },
            update = { webView ->
                webView.loadUrl(url)
            }
        )
        if (isLoading) {
            MifosProgressIndicator()
        }
    }
}