package org.mifos.mobilebanking;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by dilpreet on 29/7/17.
 */

public class RetrofitUtils {

    public static Exception get404Exception() {
        return new HttpException(Response.error(404, ResponseBody.create(MediaType.
                parse("application/json"), "Not Found")));
    }

    public static Exception get401Exception() {
        return new HttpException(Response.error(401, ResponseBody.create(MediaType.
                parse("application/json"), "UnAuthorized")));
    }

}
