package org.mifos.mobile.utils;

import android.content.Context;
import android.util.AttributeSet;

import org.mifos.mobile.api.local.PreferencesHelper;

import androidx.preference.DialogPreference;

/**
 * Created by dilpreet on 11/03/18.
 */

public class ConfigurationPreference extends DialogPreference {

    private PreferencesHelper preferencesHelper;

    public ConfigurationPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ConfigurationPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConfigurationPreference(Context context) {
        super(context);
        init();
    }

    private void init() {
        setPersistent(false);
        preferencesHelper = new PreferencesHelper(getContext());
    }

    public String getBaseUrl() {
        return preferencesHelper.getBaseUrl();
    }

    public void updateConfigurations(String baseUrl, String tenant) {
        preferencesHelper.updateConfiguration(baseUrl, tenant);
    }

    public String getTenant() {
        return preferencesHelper.getTenant();
    }
}
