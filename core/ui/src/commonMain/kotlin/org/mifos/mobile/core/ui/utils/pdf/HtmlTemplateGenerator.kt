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

import kotlinx.html.BODY
import kotlinx.html.body
import kotlinx.html.stream.createHTML

/**
 * Base class for HTML template generation using kotlinx-html.
 * Provides common utility methods for generating type-safe, PDF-ready HTML content.
 */
abstract class HtmlTemplateGenerator {

    /**
     * Generate HTML content from data.
     * Subclasses should implement this to create their specific HTML structure.
     * Generates XHTML-compliant HTML for OpenHTMLToPDF compatibility.
     */
    fun generateHtml(): String {
        val bodyContent = createHTML().body {
            generateBody()
        }

        return """
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${getTitle()}</title>
    <style type="text/css">
        ${getCommonStyles()}
        ${getAdditionalStyles()}
    </style>
</head>
$bodyContent
</html>
        """.trimIndent()
    }

    /**
     * Get the document title.
     * Subclasses should override this.
     */
    protected abstract fun getTitle(): String

    /**
     * Generate the body content.
     * Subclasses should implement this to create their specific HTML structure.
     */
    protected abstract fun BODY.generateBody()

    /**
     * Get additional CSS styles specific to the subclass.
     * Subclasses can override this to add custom styles.
     */
    protected open fun getAdditionalStyles(): String = ""

    /**
     * Get common CSS styles for professional PDF output.
     */
    protected fun getCommonStyles(): String {
        return """
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Roboto', 'Helvetica', 'Arial', sans-serif;
            font-size: 8pt;
            line-height: 1.3;
            color: #333;
            background: #fff;
        }
        
        .container {
            padding: 10px;
            max-width: 100%;
        }
        
        .header {
            margin-bottom: 12px;
            padding-bottom: 10px;
            border-bottom: 2px solid #1976d2;
        }
        
        h1 {
            color: #1976d2;
            font-size: 14pt;
            font-weight: 500;
            margin-bottom: 10px;
        }
        
        h2 {
            color: #1976d2;
            font-size: 11pt;
            font-weight: 500;
            margin-bottom: 8px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 15px;
            font-size: 6pt;
        }
        
        th, td {
            border: 1px solid #e0e0e0;
            padding: 6px 8px;
            text-align: left;
        }
        
        thead th {
            background-color: #f5f5f5;
            font-weight: 600;
            color: #424242;
        }
        
        thead tr.header-group th {
            background-color: #e3f2fd;
            font-weight: 600;
            border-bottom: 2px solid #1976d2;
        }
        
        .center {
            text-align: center !important;
        }
        
        .right {
            text-align: right !important;
        }
        
        .r-amount {
            text-align: right !important;
        }
        
        tbody tr:nth-child(even) {
            background-color: #fafafa;
        }
        
        tbody tr:hover {
            background-color: #f5f5f5;
        }
        
        tfoot td {
            background-color: #e3f2fd;
            font-weight: 600;
            border-top: 2px solid #1976d2;
        }
        
        .footer {
            margin-top: 20px;
            padding-top: 15px;
            border-top: 1px solid #e0e0e0;
            text-align: center;
            font-size: 8pt;
            color: #666;
        }
        
        .powered-by {
            margin-top: 5px;
            font-style: italic;
        }
        
        @media print {
            body {
                background: #fff;
            }
            
            .container {
                padding: 0;
            }
            
            table {
                page-break-inside: auto;
            }
            
            tr {
                page-break-inside: avoid;
                page-break-after: auto;
            }
        }
        /* PAGE_CONFIG_PLACEHOLDER */
        """.trimIndent()
    }
}
