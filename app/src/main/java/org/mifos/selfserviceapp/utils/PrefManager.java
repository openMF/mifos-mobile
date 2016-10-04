package org.mifos.selfserviceapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.mifos.selfserviceapp.data.User;
import org.mifos.selfserviceapp.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Vishwajeet
 * @since 07/06/16
 */

@Singleton
public class PrefManager {

    private static final String USER_ID = "preferences_user_id";
    private static final String TOKEN = "preferences_token";
    private static final String TENANT = "preferences_tenant";
    private static final String USER_DETAILS = "user_details";

    private SharedPreferences sharedPreferences;
    private static Gson gson;

    @Inject
    public PrefManager(@ApplicationContext Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
    }

    public int getInt(String preferenceKey, int preferenceDefaultValue) {
        return sharedPreferences.getInt(preferenceKey, preferenceDefaultValue);
    }

    public void putInt(String preferenceKey, int preferenceValue) {
        sharedPreferences.edit().putInt(preferenceKey, preferenceValue).apply();
    }

    public long getLong(String preferenceKey, long preferenceDefaultValue) {
        return sharedPreferences.getLong(preferenceKey, preferenceDefaultValue);
    }

    public void putLong(String preferenceKey, long preferenceValue) {
        sharedPreferences.edit().putLong(preferenceKey, preferenceValue).apply();
    }

    public String getString(String preferenceKey, String preferenceDefaultValue) {
        return sharedPreferences.getString(preferenceKey, preferenceDefaultValue);
    }

    public void putString(String preferenceKey, String preferenceValue) {
        sharedPreferences.edit().putString(preferenceKey, preferenceValue).apply();
    }

    /**
     * This Method is for saving the Logged In User Details in SharedPreferences
     * @param user User
     */
    public void saveUser(User user) {
        putString(USER_DETAILS, gson.toJson(user));
    }

    /**
     * This Method For loading the User Details from the SharedPreferences
     * @return User
     */
    public User getUser() {
        return gson.fromJson(getString(USER_DETAILS, "null"), User.class);
    }

    /**
     * This Method is for saving the Access Token in SharedPreferences
     * @param token Access Token
     */
    public void saveToken(String token) {
        putString(TOKEN, token);
    }

    /**
     * This Method for clearing the Access Token from the SharedPreferences
     */
    public void clearToken() {
        putString(TOKEN, "");
    }

    /**
     * This Method for loading the Access Token from the SharedPreferences
     * @return String Access Token
     */
    public String getToken() {
        return getString(TOKEN, "");
    }

    public boolean isAuthenticated() {
        return !TextUtils.isEmpty(getToken());
    }

    public long getUserId() {
        return getLong(USER_ID, -1);
    }

    public void setUserId(long id) {
        putLong(USER_ID, id);
    }

    public String getTenant() {
        return getString(TENANT, "default");
    }

    public void setTenant(String tenant) {
        if (!TextUtils.isEmpty(tenant)) {
            putString(TENANT, tenant);
        }
    }
}
