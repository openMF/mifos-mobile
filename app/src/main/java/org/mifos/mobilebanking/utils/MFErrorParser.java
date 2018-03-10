package org.mifos.mobilebanking.utils;

import com.google.gson.Gson;

import org.mifos.mobilebanking.models.mifoserror.MifosError;

import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.HttpException;

public class MFErrorParser {

    public static final String LOG_TAG = "MFErrorParser";

    private static Gson gson = new Gson();

    public static MifosError parseError(String serverResponse) {
        return gson.fromJson(serverResponse, MifosError.class);
    }

    public static String errorMessage(Throwable throwableError) {
        String errorMessage = "";
        try {
            if (throwableError instanceof HttpException) {
                errorMessage = ((HttpException) throwableError).response().errorBody().string();
                errorMessage =  MFErrorParser.parseError(errorMessage).getErrors()
                        .get(0).getDefaultUserMessage();
            }
        } catch (Throwable throwable) {
            RxJavaPlugins.getErrorHandler();
        }
        return errorMessage;
    }
}
