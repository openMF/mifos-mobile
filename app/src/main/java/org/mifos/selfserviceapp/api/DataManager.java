package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.data.User;

import retrofit2.Call;

/**
 * @author Vishwajeet
 * @since 13/6/16.
 */

public class DataManager {

    //TODO: hardcoded as of now, update it with interceptor during retrofit object creation
    public String tenantIdentifier="default";

    public final BaseApiManager mBaseApiManager;

    public DataManager(BaseApiManager baseApiManager){
        mBaseApiManager = baseApiManager;
    }

    public Call<User> login(String username, String password) {
        return mBaseApiManager.getAuthenticationApi().authenticate(username, password, tenantIdentifier);
    }
}
