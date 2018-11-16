package org.mifos.mobilebanking.utils;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.mifos.mobilebanking.models.beneficiary.Beneficiary;
import org.mifos.mobilebanking.models.templates.account.AccountType;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by dilpreet on 6/7/17.
 */

public class QrCodeGenerator {

    /**
     * Generate a QRCode which stores {@code str} in the form of {@link Bitmap}
     * @param str Data which need to stored in QRCode
     * @return Returns a {@link Bitmap} of QRCode
     */
    public static Bitmap encodeAsBitmap(String str) {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 200, 200, null);
        } catch (IllegalArgumentException iae) {
            return null;
        } catch (WriterException e) {
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * Provides a string which contains json data for creating a {@link Beneficiary}
     * @param accountNumber Account Number of client
     * @param officeName Office Name of Client
     * @param accountType {@link org.mifos.mobilebanking.ui.enums.AccountType} i.e. SAVINGS or LOAN
     * @return Returns a string with account details
     */
    public static String getAccountDetailsInString(String accountNumber, String officeName,
                                                   org.mifos.mobilebanking.ui.enums.AccountType
                                                           accountType) {
        int accountId = -1;
        if (accountType == org.mifos.mobilebanking.ui.enums.AccountType.SAVINGS) {
            accountId = 0;
        } else if (accountType == org.mifos.mobilebanking.ui.enums.AccountType.LOAN) {
            accountId = 1;
        }

        AccountType type = new AccountType();
        type.setId(accountId);
        Beneficiary payload = new Beneficiary();
        payload.setAccountNumber(accountNumber);
        payload.setAccountType(type);
        payload.setOfficeName(officeName);

        Gson gson = new Gson();
        return gson.toJson(payload);
    }
}
