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

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream

/**
 * Desktop (JVM) implementation of PDF generator.
 * Uses OpenHTMLToPDF library for HTML to PDF conversion.
 */
actual class PdfGenerator {
    actual suspend fun generateAndSharePdf(htmlContent: String, fileName: String, pageConfig: PageConfig) {
        withContext(Dispatchers.IO) {
            try {
                val downloadsDir = File(System.getProperty("user.home"), "Downloads")
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }

                val outputFile = File(downloadsDir, "$fileName.pdf")

                val finalHtml = run {
                    val orientation = if (pageConfig.orientation == Orientation.LANDSCAPE) "landscape" else "portrait"
                    val size = when (pageConfig.size) {
                        PageSize.A4 -> "A4"
                        PageSize.LETTER -> "letter"
                    }
                    val pageCss = "@page { size: $size $orientation; margin: ${pageConfig.marginMm}mm; }"

                    htmlContent.replace(
                        "/* PAGE_CONFIG_PLACEHOLDER */",
                        pageCss,
                    )
                }

                val htmlFile = File(downloadsDir, "$fileName.html")
                htmlFile.writeText(finalHtml)

                FileOutputStream(outputFile).use { outputStream ->
                    PdfRendererBuilder()
                        .useFastMode()
                        .withHtmlContent(finalHtml, null)
                        .toStream(outputStream)
                        .run()
                }

                if (Desktop.isDesktopSupported()) {
                    val desktop = Desktop.getDesktop()
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        desktop.open(outputFile)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}
