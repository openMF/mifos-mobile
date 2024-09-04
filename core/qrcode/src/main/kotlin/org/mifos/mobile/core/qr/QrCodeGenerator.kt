/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.qr

import android.graphics.Bitmap
import android.graphics.Color
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.account.AccountType
import org.mifos.mobile.core.model.enums.AccountType as EnumsAccountType

object QrCodeGenerator {
    private const val QR_CODE_SIZE = 200

    /**
     * Generate a QRCode which stores `str` in the form of [Bitmap]
     * @param str Data which need to stored in QRCode
     * @return Returns a [Bitmap] of QRCode or null if generation fails
     */
    fun encodeAsBitmap(str: String?): Bitmap? {
        if (str.isNullOrEmpty()) return null

        return try {
            val result = MultiFormatWriter().encode(
                str,
                BarcodeFormat.QR_CODE,
                QR_CODE_SIZE,
                QR_CODE_SIZE,
                null,
            )
            createBitmapFromBitMatrix(result)
        } catch (e: Exception) {
            null
        }
    }

    private fun createBitmapFromBitMatrix(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height) { index ->
            if (matrix[index % width, index / width]) Color.BLACK else Color.WHITE
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }

    /**
     * Provides a string which contains json data for creating a [Beneficiary]
     * @param accountNumber Account Number of client
     * @param officeName Office Name of Client
     * @param accountType [EnumsAccountType] i.e. SAVINGS or LOAN
     * @return Returns a string with account details
     */
    fun getAccountDetailsInString(
        accountNumber: String?,
        officeName: String?,
        accountType: EnumsAccountType,
    ): String {
        val payload = Beneficiary().apply {
            this.accountNumber = accountNumber
            this.accountType = AccountType().apply {
                id = when (accountType) {
                    EnumsAccountType.SAVINGS -> 0
                    EnumsAccountType.LOAN -> 1
                    EnumsAccountType.SHARE -> -1
                }
            }
            this.officeName = officeName
        }

        return Gson().toJson(payload)
    }
}
