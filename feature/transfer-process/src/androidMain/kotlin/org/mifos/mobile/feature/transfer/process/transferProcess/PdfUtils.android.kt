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

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

actual fun generateBillPdf(billData: TransferBillData): ByteArray {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Header background
    val headerPaint = android.graphics.Paint()
    headerPaint.color = android.graphics.Color.parseColor("#2196F3")
    canvas.drawRect(0f, 0f, 595f, 120f, headerPaint)

    // Header text
    val headerTextPaint = android.graphics.Paint()
    headerTextPaint.color = android.graphics.Color.WHITE
    headerTextPaint.textSize = 32f
    headerTextPaint.typeface = android.graphics.Typeface.create(
        android.graphics.Typeface.DEFAULT,
        android.graphics.Typeface.BOLD,
    )
    canvas.drawText("TRANSFER RECEIPT", 50f, 60f, headerTextPaint)

    val subHeaderPaint = android.graphics.Paint()
    subHeaderPaint.color = android.graphics.Color.WHITE
    subHeaderPaint.textSize = 14f
    canvas.drawText("Transaction Confirmation", 50f, 90f, subHeaderPaint)
    canvas.drawText(billData.date, 50f, 110f, subHeaderPaint)

    // Content
    val labelPaint = android.graphics.Paint()
    labelPaint.textSize = 12f
    labelPaint.color = android.graphics.Color.GRAY

    val valuePaint = android.graphics.Paint()
    valuePaint.textSize = 16f
    valuePaint.color = android.graphics.Color.BLACK
    valuePaint.typeface = android.graphics.Typeface.create(
        android.graphics.Typeface.DEFAULT,
        android.graphics.Typeface.BOLD,
    )

    var yPos = 180f

    // Transaction ID
    canvas.drawText("TRANSACTION ID", 50f, yPos, labelPaint)
    canvas.drawText(billData.transferId, 50f, yPos + 25f, valuePaint)
    yPos += 70f

    // Amount
    canvas.drawText("AMOUNT TRANSFERRED", 50f, yPos, labelPaint)
    canvas.drawText(billData.amount, 50f, yPos + 25f, valuePaint)
    yPos += 70f

    // From Account
    canvas.drawText("FROM ACCOUNT", 50f, yPos, labelPaint)
    canvas.drawText(billData.fromAccount, 50f, yPos + 25f, valuePaint)
    yPos += 70f

    // To Account
    canvas.drawText("TO ACCOUNT", 50f, yPos, labelPaint)
    canvas.drawText(billData.toAccount, 50f, yPos + 25f, valuePaint)
    yPos += 70f

    // Date
    canvas.drawText("DATE", 50f, yPos, labelPaint)
    canvas.drawText(billData.date, 50f, yPos + 25f, valuePaint)

    // Footer
    val footerPaint = android.graphics.Paint()
    footerPaint.textSize = 10f
    footerPaint.color = android.graphics.Color.GRAY
    canvas.drawText(
        "This is a computer-generated receipt and does not require a signature.",
        50f,
        780f,
        footerPaint,
    )

    pdfDocument.finishPage(page)
    val outputStream = java.io.ByteArrayOutputStream()
    pdfDocument.writeTo(outputStream)
    pdfDocument.close()
    return outputStream.toByteArray()
}

actual fun savePdfToFile(pdfData: ByteArray, fileName: String) {
    try {
        // Get application context
        val context = getApplicationContext()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore for Android 10+
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues,
            )

            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(pdfData)
                    outputStream.flush()
                }
                println("PDF saved successfully using MediaStore")
            }
        } else {
            // Use legacy method for Android 9 and below
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            val file = File(downloadsDir, fileName)
            FileOutputStream(file).use { it.write(pdfData) }
            println("PDF saved successfully to: ${file.absolutePath}")
        }
    } catch (e: Exception) {
        println("Failed to save PDF: ${e.message}")
    }
}

private fun getApplicationContext(): Context {
    return try {
        Class.forName("android.app.ActivityThread")
            .getMethod("currentApplication")
            .invoke(null) as Context
    } catch (e: Exception) {
        throw IllegalStateException("Could not get application context", e)
    }
}
