package org.mifos.mobile.utils

import com.google.gson.Gson
import io.reactivex.plugins.RxJavaPlugins
import org.mifos.mobile.models.mifoserror.MifosError
import retrofit2.HttpException

object MFErrorParser {
    const val LOG_TAG = "MFErrorParser"
    private val gson = Gson()
    fun parseError(serverResponse: String?): MifosError {
        return gson.fromJson(serverResponse, MifosError::class.java)
    }

    @kotlin.jvm.JvmStatic
    fun errorMessage(throwableError: Throwable?): String? {
        var errorMessage: String? = ""
        try {
            if (throwableError is HttpException) {
                errorMessage = throwableError.response().errorBody().string()
                errorMessage = parseError(errorMessage).errors[0].defaultUserMessage
            }
        } catch (throwable: Throwable) {
            RxJavaPlugins.getErrorHandler()
        }
        return errorMessage
    }
}