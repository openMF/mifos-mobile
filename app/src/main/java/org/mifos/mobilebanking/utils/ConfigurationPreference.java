package org.mifos.mobilebanking.utils;

import android.content.Context;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import org.mifos.mobilebanking.api.local.PreferencesHelper;

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
