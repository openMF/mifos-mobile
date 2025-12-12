/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.transferProcess

import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.NSMutableData
import platform.UIKit.UIGraphicsBeginPDFContextToData
import platform.UIKit.UIGraphicsBeginPDFPage
import platform.UIKit.UIGraphicsEndPDFContext

actual fun generateBillPdf(billData: TransferBillData): ByteArray {
    val data = NSMutableData()
    UIGraphicsBeginPDFContextToData(data, CGRectMake(0.0, 0.0, 595.0, 842.0), null)
    UIGraphicsBeginPDFPage()
    // Basic PDF generation - text-based for simplicity
    UIGraphicsEndPDFContext()

    // For now, return text-based receipt
    val receipt = """TRANSFER RECEIPT
        |Transaction ID: ${billData.transferId}
        |Amount: ${billData.amount}
        |From: ${billData.fromAccount}
        |To: ${billData.toAccount}
        |Date: ${billData.date}
    """.trimMargin()
    return receipt.toByteArray()
}

actual fun savePdfToFile(pdfData: ByteArray, fileName: String) {
    // For iOS, save to documents
    val nsData = pdfData.toNSData()
    val fileManager = platform.Foundation.NSFileManager.defaultManager
    val urls = fileManager.URLsForDirectory(
        platform.Foundation.NSDocumentDirectory,
        platform.Foundation.NSUserDomainMask,
    )
    val documentsURL = urls.firstOrNull() as? platform.Foundation.NSURL ?: return
    val fileURL = documentsURL.URLByAppendingPathComponent(fileName)
    nsData.writeToURL(fileURL, atomically = true)
}

private fun ByteArray.toNSData(): NSData = NSData.create(
    bytes = this.refTo(0),
    length = this.size.toULong(),
)
