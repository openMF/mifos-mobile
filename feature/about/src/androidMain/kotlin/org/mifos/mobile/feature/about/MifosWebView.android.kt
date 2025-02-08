/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.about

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun MifosWebView(
    htmlContent: String,
    onLoadingChange: (isLoading: Boolean) -> Unit,
    modifier: Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                overScrollMode = WebView.OVER_SCROLL_NEVER
                this.webViewClient = object : WebViewClient() {
                    @Deprecated("Deprecated in Java")
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest?,
                    ): Boolean {
                        return if (request?.url.toString().contains("jpg") ||
                            request?.url.toString().contains("png") ||
                            request?.url.toString().contains("attachment_id")
                        ) {
                            true
                        } else {
                            openUrl(request?.url.toString())
                            true
                        }
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        onLoadingChange(true)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        onLoadingChange(false)
                    }
                }
                loadUrl(htmlContent)
            }
        },
        update = { webView ->
            webView.loadUrl(htmlContent)
        },
    )
}
