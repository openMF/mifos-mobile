package org.mifos.selfserviceapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author Vishwajeet
 * @since 07/06/16
 */

public class PrefManager {
    private static final String USER_ID = "preferences_user_id";
    private static final String TOKEN = "preferences_token";
    private static final String TENANT = "preferences_tenant";
    private Context context;

    private static PrefManager instance = null;

    private PrefManager(Context context) {
        this.context = context;
    }

    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PrefManager getInstance(Context context){
        if (instance == null) {
            instance = new PrefManager(context);
        }
        return instance;
    }

    public int getInt(String preferenceKey, int preferenceDefaultValue) {
        return getPreferences().getInt(preferenceKey, preferenceDefaultValue);
    }

    public void putInt(String preferenceKey, int preferenceValue) {
        getPreferences().edit().putInt(preferenceKey, preferenceValue).commit();
    }

    public String getString(String preferenceKey, String preferenceDefaultValue) {
        return getPreferences().getString(preferenceKey, preferenceDefaultValue);
    }

    public void putString(String preferenceKey, String preferenceValue) {
        getPreferences().edit().putString(preferenceKey, preferenceValue).commit();
    }

    public void saveToken(String token) {
        putString(TOKEN, token);
    }

    public void clearToken() {
        putString(TOKEN, "");
    }

    public String getToken() {
        return getString(TOKEN, "");
    }

    public boolean isAuthenticated() {
        return !TextUtils.isEmpty(getToken());
    }

    public int getUserId() {
        return getInt(USER_ID, -1);
    }

    public void setUserId(int id) {
        putInt(USER_ID, id);
    }

    public String getTenant() {
        return getString(TENANT, "default");
    }

    public void setTenant(String tenant) {
        if (!TextUtils.isEmpty(tenant))
            putString(TENANT, tenant);
    }
}
