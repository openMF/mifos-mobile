package org.mifos.mobilebanking.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.BaseApiManager;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.ui.activities.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 11/03/18.
 */

public class ConfigurationDialogFragmentCompat extends PreferenceDialogFragmentCompat implements
        DialogPreference.TargetFragment {

    @BindView(R.id.et_tenant)
    EditText etTenant;

    @BindView(R.id.et_base_url)
    EditText etBaseUrl;

    private PreferencesHelper preferencesHelper;

    @Override
    protected View onCreateDialogView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.preference_configuration, null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        ButterKnife.bind(this, view);
        preferencesHelper = new PreferencesHelper(getContext());

        ConfigurationPreference preference = (ConfigurationPreference) getPreference();

        etBaseUrl.setText(preference.getBaseUrl());
        etTenant.setText(preference.getTenant());

        etBaseUrl.setSelection(etBaseUrl.getText().length());
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            ConfigurationPreference preference = (ConfigurationPreference) getPreference();
            preference.updateConfigurations(etBaseUrl.getText().toString(), etTenant.getText().
                    toString());

            preferencesHelper.clear();
            String baseUrl = preferencesHelper.getBaseUrl();
            String tenant = preferencesHelper.getTenant();
            BaseApiManager.createService(baseUrl, tenant, "");

            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            getActivity().finish();
        }
    }

    @Override
    public Preference findPreference(CharSequence key) {
        return getPreference();
    }
}
