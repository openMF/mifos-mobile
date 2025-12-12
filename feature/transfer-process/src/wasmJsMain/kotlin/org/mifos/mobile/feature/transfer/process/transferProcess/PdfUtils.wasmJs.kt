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

actual fun generateBillPdf(billData: TransferBillData): ByteArray {
    // For WASM JS, generate text-based receipt
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
    try {
        val sanitizedName = fileName.replace(Regex("[^a-zA-Z0-9._-]"), "_")
        val url = js("URL.createObjectURL(new Blob([pdfData], {type: 'text/plain'}))") as String
        val link = js("document.createElement('a')") as org.w3c.dom.HTMLAnchorElement
        link.href = url
        link.download = sanitizedName
        js("document.body.appendChild(link)")
        link.click()
        js("document.body.removeChild(link)")
        js("URL.revokeObjectURL(url)")
    } catch (e: Exception) {
        println("Error downloading file: ${e.message}")
        throw e
    }
}
