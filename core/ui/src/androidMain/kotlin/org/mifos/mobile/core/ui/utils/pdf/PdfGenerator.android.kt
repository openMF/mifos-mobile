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

/**
 * Android implementation of PDF generator using WebView print adapter.
 */
import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.getSystemService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            val appContext = context ?: return@withContext

            val finalHtml = run {
                val orientation =
                    if (pageConfig.orientation == Orientation.LANDSCAPE) "landscape" else "portrait"
                val size = when (pageConfig.size) {
                    PageSize.A4 -> "A4"
                    PageSize.LETTER -> "letter"
                }
                val pageCss =
                    "@page { size: $size $orientation; margin: ${pageConfig.marginMm}mm; }"
                htmlContent.replace("/* PAGE_CONFIG_PLACEHOLDER */", pageCss)
            }

            val webView = WebView(appContext).apply {
                settings.javaScriptEnabled = false
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        // Safely handle null view and create adapter without force-unwrapping (!!)
                        val nativeAdapter = view?.createPrintDocumentAdapter(fileName) ?: return
                        val printManager = appContext.getSystemService<PrintManager>() ?: return
                        val attributes = createPrintAttributes(pageConfig)

                        // Wrap the adapter to manage lifecycle and prevent memory leaks
                        val wrappedAdapter = object : PrintDocumentAdapter() {
                            override fun onStart() {
                                nativeAdapter.onStart()
                            }

                            override fun onLayout(
                                oldAttributes: PrintAttributes?,
                                newAttributes: PrintAttributes?,
                                cancellationSignal: CancellationSignal?,
                                callback: LayoutResultCallback?,
                                extras: Bundle?,
                            ) {
                                nativeAdapter.onLayout(
                                    oldAttributes,
                                    newAttributes,
                                    cancellationSignal,
                                    callback,
                                    extras,
                                )
                            }

                            override fun onWrite(
                                pages: Array<out PageRange>?,
                                destination: ParcelFileDescriptor?,
                                cancellationSignal: CancellationSignal?,
                                callback: WriteResultCallback?,
                            ) {
                                nativeAdapter.onWrite(
                                    pages,
                                    destination,
                                    cancellationSignal,
                                    callback,
                                )
                            }

                            override fun onFinish() {
                                nativeAdapter.onFinish()
                                view.post {
                                    view.destroy()
                                }
                            }
                        }

                        printManager.print(fileName, wrappedAdapter, attributes)
                    }
                }
            }
            webView.loadDataWithBaseURL(null, finalHtml, "text/html", "UTF-8", null)
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

        // Conversion: 1 mm = 39.3701 mils
        val marginMils = (pageConfig.marginMm * 39.3701).toInt()

        return PrintAttributes.Builder()
            .setMediaSize(mediaSize)
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .setResolution(PrintAttributes.Resolution("pdf", "pdf", 600, 600))
            .setMinMargins(PrintAttributes.Margins(marginMils, marginMils, marginMils, marginMils))
            .build()
    }
}
