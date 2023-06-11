package org.mifos.mobile.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityPrivacyPolicyBinding
import org.mifos.mobile.ui.activities.base.BaseActivity

/**
 * @author Rajan Maurya
 * On 11/03/19.
 */
class PrivacyPolicyActivity : BaseActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding
    private var showOrHideWebViewInitialUse = "show"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarTitle(getString(R.string.privacy_policy))
        showBackButton()

        // Force links and redirects to open in the WebView instead of in a browser
        binding.webView.webViewClient = WebViewClient()

        // Enable Javascript
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

        // REMOTE RESOURCE
        binding.webView.settings.domStorageEnabled = true
        binding.webView.overScrollMode = WebView.OVER_SCROLL_NEVER
        binding.webView.loadUrl(getString(R.string.privacy_policy_host_url))
        binding.webView.webViewClient = MyWebViewClient()
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    internal inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (Uri.parse(url)?.host
                    ?.endsWith(getString(R.string.privacy_policy_host)) == true
            ) {
                return false
            }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            return true
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            // only make it invisible the FIRST time the app is run
            if (showOrHideWebViewInitialUse == "show") {
                binding.webView.visibility = View.INVISIBLE
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            showOrHideWebViewInitialUse = "hide"
            binding.progressBar.visibility = View.GONE
            view.visibility = View.VISIBLE
            super.onPageFinished(view, url)
        }
    }
}
