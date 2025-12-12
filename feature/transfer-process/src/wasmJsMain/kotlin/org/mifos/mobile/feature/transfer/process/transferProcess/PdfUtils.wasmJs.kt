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
    // For WASM JS, do nothing
    println("PDF generated for WASM JS, but not saved")
}
