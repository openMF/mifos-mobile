package org.mifos.selfserviceapp.utils;

import com.google.gson.Gson;

import org.mifos.selfserviceapp.models.mifoserror.MifosError;

import retrofit2.HttpException;
import rx.plugins.RxJavaPlugins;

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
            RxJavaPlugins.getInstance().getErrorHandler().handleError(throwable);
        }
        return errorMessage;
    }
}
