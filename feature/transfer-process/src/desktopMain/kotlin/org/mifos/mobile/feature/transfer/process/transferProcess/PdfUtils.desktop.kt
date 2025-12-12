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

import java.io.File

actual fun generateBillPdf(billData: TransferBillData): ByteArray {
    // For desktop, generate a simple text-based receipt
    val receipt = buildString {
        appendLine("TRANSFER RECEIPT")
        appendLine("Transaction Confirmation")
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
    return receipt.toByteArray()
}

actual fun savePdfToFile(pdfData: ByteArray, fileName: String) {
    try {
        // Save to user's Downloads directory
        val homeDir = System.getProperty("user.home")
        val sanitizedName = fileName.replace(Regex("[^a-zA-Z0-9._-]"), "_")
        val file = File(homeDir, "Downloads/$sanitizedName")
        file.parentFile?.mkdirs()
        file.writeBytes(pdfData)
    } catch (e: Exception) {
        println("Error saving file: ${e.message}")
        throw e
    }
}
