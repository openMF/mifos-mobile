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

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CompletableDeferred
import org.w3c.dom.HTMLIFrameElement

/**
 * JS implementation of PDF generator.
 * Uses iframe with browser's print functionality to print the generated HTML content.
 */
actual class PdfGenerator {
    actual suspend fun generateAndSharePdf(
        htmlContent: String,
        fileName: String,
        pageConfig: PageConfig,
    ) {
        val completionSignal = CompletableDeferred<Unit>()
        var printFrame: HTMLIFrameElement? = null

        try {
            // FIX: Use checkNotNull instead of manual throw (fixes UseCheckOrError)
            val docBody = checkNotNull(document.body) {
                "Cannot generate PDF: document.body is null"
            }

            val orientation = if (pageConfig.orientation == Orientation.LANDSCAPE) {
                "landscape"
            } else {
                "portrait"
            }
            val size = when (pageConfig.size) {
                PageSize.A4 -> "A4"
                PageSize.LETTER -> "letter"
            }
            val pageCss = "@page { size: $size $orientation; margin: ${pageConfig.marginMm}mm; }"

            val finalHtml = htmlContent.replace(
                "/* PAGE_CONFIG_PLACEHOLDER */",
                pageCss,
            )

            val iframe = document.createElement("iframe") as HTMLIFrameElement
            printFrame = iframe

            iframe.setAttribute(
                "style",
                "position:fixed;visibility:hidden;width:0;height:0;border:none;",
            )
            docBody.appendChild(iframe)

            val frameDoc = checkNotNull(iframe.contentWindow?.document) {
                "Could not access iframe content window"
            }

            frameDoc.open()
            frameDoc.write(finalHtml)
            iframe.onload = {
                window.setTimeout(
                    {
                        try {
                            iframe.contentWindow?.let {
                                it.print()
                                completionSignal.complete(Unit)
                            } ?: completionSignal.completeExceptionally(
                                IllegalStateException("Could not access iframe content window"),
                            )
                        } catch (e: Exception) {
                            completionSignal.completeExceptionally(e)
                        }
                        null
                    },
                    0,
                )
            }
            frameDoc.close()

            completionSignal.await()
        } catch (e: Exception) {
            println("Error generating PDF on Web: ${e.message}")
            throw e
        } finally {
            printFrame?.let { frame ->
                window.setTimeout(
                    {
                        if (document.body?.contains(frame) == true) {
                            document.body?.removeChild(frame)
                        }
                        null
                    },
                    2000,
                )
            }
        }
    }
}
