/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.transferProcess

import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToURL

actual fun generateBillPdf(billData: TransferBillData): ByteArray {
    // Generate text-based receipt similar to desktop
    val receipt = buildString {
        appendLine("TRANSFER RECEIPT")
        appendLine("Transaction Confirmation")
        appendLine(billData.date)
        appendLine()
        appendLine("TRANSACTION ID")
        appendLine(billData.transferId)
        appendLine()
        appendLine("AMOUNT TRANSFERRED")
        appendLine(billData.amount)
        appendLine()
        appendLine("FROM ACCOUNT")
        appendLine(billData.fromAccount)
        appendLine()
        appendLine("TO ACCOUNT")
        appendLine(billData.toAccount)
        appendLine()
        appendLine("DATE")
        appendLine(billData.date)
        appendLine()
        appendLine("REMARK")
        appendLine(billData.remark)
        appendLine()
        appendLine("This is a computer-generated receipt and does not require a signature.")
    }
    return receipt.encodeToByteArray()
}

actual fun savePdfToFile(pdfData: ByteArray, fileName: String) {
    require(fileName.isNotBlank()) { "Filename cannot be empty" }
    try {
        // For iOS, save to documents
        val nsData = pdfData.toNSData()
        val sanitizedName = fileName.replace(Regex("[^a-zA-Z0-9._-]"), "_")
        val finalName = if (sanitizedName.endsWith(".pdf")) sanitizedName else "$sanitizedName.pdf"
        val fileManager = platform.Foundation.NSFileManager.defaultManager
        val urls = fileManager.URLsForDirectory(
            platform.Foundation.NSDocumentDirectory,
            platform.Foundation.NSUserDomainMask,
        )
        val documentsURL = urls.firstOrNull() as? platform.Foundation.NSURL
            ?: error("Could not access documents directory")
        val fileURL = documentsURL.URLByAppendingPathComponent(finalName)
        val success = nsData.writeToURL(fileURL, atomically = true)
        check(success) { "Failed to write file to $fileURL" }
    } catch (e: Exception) {
        println("Error saving file: ${e.message}")
        throw e
    }
}

private fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData.data()
    return usePinned {
        NSData.dataWithBytes(it.addressOf(0), length = size.toULong())
    }
}
