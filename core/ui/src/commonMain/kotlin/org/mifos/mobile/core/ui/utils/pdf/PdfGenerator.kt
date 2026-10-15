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
 * Platform-specific PDF generator interface.
 * Implementations should handle HTML to PDF conversion using platform-specific capabilities.
 */
expect class PdfGenerator {
    /**
     * Generates a PDF from HTML content and triggers sharing/saving.
     *
     * @param htmlContent The HTML string containing the styled content.
     * @param fileName The name of the PDF file to generate (without extension).
     * @param pageConfig Page configuration (size, orientation, margins) - required.
     */
    suspend fun generateAndSharePdf(htmlContent: String, fileName: String, pageConfig: PageConfig)
}
