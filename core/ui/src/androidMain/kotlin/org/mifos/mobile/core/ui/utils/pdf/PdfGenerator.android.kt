/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils.pdf

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.getSystemService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Android implementation of PDF generator using WebView print adapter.
 */
actual class PdfGenerator {

    private var context: Context? = null

    fun setContext(context: Context) {
        this.context = context
    }

    actual suspend fun generateAndSharePdf(
        htmlContent: String,
        fileName: String,
        pageConfig: PageConfig,
    ) {
        withContext(Dispatchers.Main) {
            val appContext = context ?: throw Exception("Context not initialized")

            val finalHtml = run {
                val orientation = if (pageConfig.orientation == Orientation.LANDSCAPE) {
                    "landscape"
                } else {
                    "portrait"
                }
                val size = when (pageConfig.size) {
                    PageSize.A4 -> "A4"
                    PageSize.LETTER -> "letter"
                }
                val pageCss =
                    "@page { size: $size $orientation; margin: ${pageConfig.marginMm}mm; }"
                htmlContent.replace("/* PAGE_CONFIG_PLACEHOLDER */", pageCss)
            }

            var webView: WebView? = WebView(appContext)

            try {
                suspendCancellableCoroutine { continuation ->
                    val currentWebView = webView ?: return@suspendCancellableCoroutine

                    continuation.invokeOnCancellation {
                        currentWebView.stopLoading()
                        currentWebView.destroy()
                        webView = null
                    }

                    currentWebView.settings.javaScriptEnabled = false
                    currentWebView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            try {
                                val printManager = appContext.getSystemService<PrintManager>()
                                    ?: throw Exception("PrintManager not available")

                                val attributes = createPrintAttributes(pageConfig)
                                val nativeAdapter = view?.createPrintDocumentAdapter(fileName)
                                    ?: throw Exception("PrintDocumentAdapter is null")

                                printManager.print(fileName, nativeAdapter, attributes)

                                if (continuation.isActive) continuation.resume(Unit)
                            } catch (e: Exception) {
                                if (continuation.isActive) continuation.resumeWithException(e)
                            }
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            errorCode: Int,
                            description: String?,
                            failingUrl: String?,
                        ) {
                            if (continuation.isActive) {
                                continuation.resumeWithException(Exception("WebView Error: $description"))
                            }
                        }
                    }

                    currentWebView.loadDataWithBaseURL(null, finalHtml, "text/html", "UTF-8", null)
                }
            } finally {
                // If webView wasn't nulled by cancellation, clean it up now
                webView?.let { activeView ->
                    activeView.post {
                        activeView.stopLoading()
                        activeView.destroy()
                    }
                    webView = null
                }
            }
        }
    }

    private fun createPrintAttributes(pageConfig: PageConfig): PrintAttributes {
        val isLandscape = pageConfig.orientation == Orientation.LANDSCAPE
        val mediaSize = when (pageConfig.size) {
            PageSize.A4 -> if (isLandscape) {
                PrintAttributes.MediaSize.ISO_A4.asLandscape()
            } else {
                PrintAttributes.MediaSize.ISO_A4
            }

            PageSize.LETTER -> if (isLandscape) {
                PrintAttributes.MediaSize.NA_LETTER.asLandscape()
            } else {
                PrintAttributes.MediaSize.NA_LETTER
            }
        }
        val marginMils = (pageConfig.marginMm * 39.3701).toInt()

        return PrintAttributes.Builder()
            .setMediaSize(mediaSize)
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .setMinMargins(
                PrintAttributes.Margins(
                    marginMils,
                    marginMils,
                    marginMils,
                    marginMils,
                ),
            )
            .build()
    }
}
