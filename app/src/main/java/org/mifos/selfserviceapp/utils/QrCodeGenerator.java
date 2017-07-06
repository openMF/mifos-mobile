package org.mifos.selfserviceapp.utils;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.models.templates.account.AccountType;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by dilpreet on 6/7/17.
 */

public class QrCodeGenerator {

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

    public static String getAccountDetailsInString(String accountNumber, String officeName,
                                                   org.mifos.selfserviceapp.ui.enums.AccountType
                                                           accountType) {
        int accountId = -1;
        if (accountType == org.mifos.selfserviceapp.ui.enums.AccountType.SAVINGS) {
            accountId = 0;
        } else if (accountType == org.mifos.selfserviceapp.ui.enums.AccountType.LOAN) {
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
