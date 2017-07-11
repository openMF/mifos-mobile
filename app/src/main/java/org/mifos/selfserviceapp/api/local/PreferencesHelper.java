package org.mifos.selfserviceapp.api.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.mifos.selfserviceapp.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Vishwajeet
 * @since 07/06/16
 */

@Singleton
public class PreferencesHelper {
    private static final String USER_ID = "preferences_user_id";
    private static final String TOKEN = "preferences_token";
    private static final String TENANT = "preferences_tenant";
    private static final String CLIENT_ID = "preferences_client";
    private static final String OFFICE_NAME = "preferences_office_name";
    private static final String PASSCODE = "preferences_passcode";
    private SharedPreferences sharedPreferences;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
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

    public void putBoolean(String preferenceKey, boolean preferenceValue) {
        sharedPreferences.edit().putBoolean(preferenceKey, preferenceValue).apply();
    }

    public boolean getBoolean(String preferenceKey, boolean preferenceDefaultValue) {
        return sharedPreferences.getBoolean(preferenceKey, preferenceDefaultValue);
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

    public void setPasscode(String passcode) {
        putString(PASSCODE, passcode);
    }

    public String getPasscode() {
        return getString(PASSCODE, "");
    }

    public void setClientId(long clientId) {
        putLong(CLIENT_ID, clientId);
    }

    public long getClientId() {
        return getLong(CLIENT_ID, -1);
    }

    public void setOfficeName(String officeName) {
        putString(OFFICE_NAME, officeName);
    }

    public String getOfficeName() {
        return getString(OFFICE_NAME, "");
    }
}
