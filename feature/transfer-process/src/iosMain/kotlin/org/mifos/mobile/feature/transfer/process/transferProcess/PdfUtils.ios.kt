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

import platform.Foundation.NSData

actual fun generateBillPdf(billData: TransferBillData): ByteArray {
    // Generate text-based receipt for iOS
    val receipt = """TRANSFER RECEIPT
        |Transaction ID: ${billData.transferId}
        |Amount: ${billData.amount}
        |From: ${billData.fromAccount}
        |To: ${billData.toAccount}
        |Date: ${billData.date}
        |Remark: ${billData.remark}
    """.trimMargin()
    return receipt.toByteArray()
}

actual fun savePdfToFile(pdfData: ByteArray, fileName: String) {
    // For iOS, save to documents
    val nsData = pdfData.toNSData()
    val sanitizedName = fileName.replace(Regex("[^a-zA-Z0-9._-]"), "_")
    val fileManager = platform.Foundation.NSFileManager.defaultManager
    val urls = fileManager.URLsForDirectory(
        platform.Foundation.NSDocumentDirectory,
        platform.Foundation.NSUserDomainMask,
    )
    val documentsURL = urls.firstOrNull() as? platform.Foundation.NSURL ?: return
    val fileURL = documentsURL.URLByAppendingPathComponent(sanitizedName)
    nsData.writeToURL(fileURL, atomically = true)
}

private fun ByteArray.toNSData(): NSData = NSData.create(
    bytes = this.refTo(0),
    length = this.size.toULong(),
)
