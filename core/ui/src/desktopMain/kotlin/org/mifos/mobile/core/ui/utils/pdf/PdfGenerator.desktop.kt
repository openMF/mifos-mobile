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
import com.openhtmltopdf.svgsupport.BatikSVGDrawer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * Desktop (JVM) implementation of PDF generator.
 * Uses OpenHTMLToPDF library for HTML to PDF conversion.
 */
actual class PdfGenerator {
    actual suspend fun generateAndSharePdf(
        htmlContent: String,
        fileName: String,
        pageConfig: PageConfig,
    ) {
        val outputFile = withContext(Dispatchers.Main) {
            val fileChooser = JFileChooser().apply {
                dialogTitle = "Save PDF Report"
                fileFilter = FileNameExtensionFilter("PDF Documents", "pdf")
                selectedFile = File("$fileName.pdf")
            }

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                var file = fileChooser.selectedFile

                // Ensure .pdf extension if user manually typed a name
                if (!file.name.lowercase().endsWith(".pdf")) {
                    file = File(file.absolutePath + ".pdf")
                }

                // Check for "Locked File" scenario (common on Windows)
                if (file.exists() && !file.canWrite()) {
                    JOptionPane.showMessageDialog(
                        null,
                        "The file is currently open in another program. Please close it and try again.",
                        "Permission Denied",
                        JOptionPane.ERROR_MESSAGE,
                    )
                    null
                } else {
                    file
                }
            } else {
                null
            }
        } ?: return

        withContext(Dispatchers.IO) {
            try {
                val finalHtml = run {
                    val orientation =
                        if (pageConfig.orientation == Orientation.LANDSCAPE) "landscape" else "portrait"
                    val size = when (pageConfig.size) {
                        PageSize.A4 -> "A4"
                        PageSize.LETTER -> "letter"
                    }
                    val pageCss =
                        "@page { size: $size $orientation; margin: ${pageConfig.marginMm}mm; }"

                    htmlContent.replace(
                        "/* PAGE_CONFIG_PLACEHOLDER */",
                        pageCss,
                    )
                }

                FileOutputStream(outputFile).use { outputStream ->
                    PdfRendererBuilder()
                        .useSVGDrawer(BatikSVGDrawer())
                        .useFastMode()
                        .withHtmlContent(finalHtml, null)
                        .toStream(outputStream)
                        .run()
                }

                // 3. Open the file after generation
                if (Desktop.isDesktopSupported()) {
                    val desktop = Desktop.getDesktop()
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        try {
                            desktop.open(outputFile)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}
