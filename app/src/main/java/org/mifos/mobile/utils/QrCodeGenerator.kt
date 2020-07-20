package org.mifos.mobile.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.ui.enums.AccountType

/**
 * Created by dilpreet on 6/7/17.
 */
object QrCodeGenerator {
    /**
     * Generate a QRCode which stores `str` in the form of [Bitmap]
     * @param str Data which need to stored in QRCode
     * @return Returns a [Bitmap] of QRCode
     */
    fun encodeAsBitmap(str: String?): Bitmap? {
        val result: BitMatrix
        result = try {
            MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 200, 200, null)
        } catch (iae: IllegalArgumentException) {
            return null
        } catch (e: WriterException) {
            return null
        }
        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result[x, y]) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    /**
     * Provides a string which contains json data for creating a [Beneficiary]
     * @param accountNumber Account Number of client
     * @param officeName Office Name of Client
     * @param accountType [org.mifos.mobile.ui.enums.AccountType] i.e. SAVINGS or LOAN
     * @return Returns a string with account details
     */
    fun getAccountDetailsInString(
            accountNumber: String?, officeName: String?,
            accountType: AccountType
    ): String {
        var accountId = -1
        if (accountType === AccountType.SAVINGS) {
            accountId = 0
        } else if (accountType === AccountType.LOAN) {
            accountId = 1
        }
        val type = org.mifos.mobile.models.templates.account.AccountType()
        type.id = accountId
        val payload = Beneficiary()
        payload.accountNumber = accountNumber
        payload.accountType = type
        payload.officeName = officeName
        val gson = Gson()
        return gson.toJson(payload)
    }
}