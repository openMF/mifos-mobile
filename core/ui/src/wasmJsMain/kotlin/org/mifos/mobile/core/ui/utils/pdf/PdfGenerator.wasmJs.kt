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
import org.w3c.dom.HTMLIFrameElement

/**
 * WasmJS (Web) implementation of PDF generator.
 * Uses browser's print functionality for PDF generation.
 */
actual class PdfGenerator {
    actual suspend fun generateAndSharePdf(htmlContent: String, fileName: String, pageConfig: PageConfig) {
        var printFrame: HTMLIFrameElement? = null
        try {
            val orientation = if (pageConfig.orientation == Orientation.LANDSCAPE) "landscape" else "portrait"
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

            iframe.setAttribute("style", "position:absolute;width:0;height:0;border:none;")
            document.body?.appendChild(iframe)

            val frameDoc = iframe.contentWindow?.document

            if (frameDoc != null) {
                frameDoc.open()
                frameDoc.write(finalHtml)
                frameDoc.close()

                window.setTimeout({
                    try {
                        iframe.contentWindow?.print()

                        window.setTimeout({
                            if (document.body?.contains(iframe) == true) {
                                document.body?.removeChild(iframe)
                            }
                            null
                        }, 1000)
                    } catch (e: Throwable) {
                        println("Error printing: ${e.message}")
                        if (document.body?.contains(iframe) == true) {
                            document.body?.removeChild(iframe)
                        }
                    }
                    null
                }, 500)
            } else {
                println("Could not access iframe content window")
                document.body?.removeChild(iframe)
            }
        } catch (e: Exception) {
            println("Error generating PDF on Web: ${e.message}")
            printFrame?.let {
                if (document.body?.contains(it) == true) {
                    document.body?.removeChild(it)
                }
            }
        }
    }
}
