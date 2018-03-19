package org.mifos.mobilebanking.utils;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.BaseApiManager;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.ui.activities.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dilpreet on 11/03/18.
 */

public class ConfigurationDialogFragmentCompat extends PreferenceDialogFragmentCompat implements
        DialogPreference.TargetFragment, TextWatcher {

    @BindView(R.id.et_tenant)
    EditText etTenant;

    @BindView(R.id.et_base_url)
    EditText etBaseUrl;

    @BindView(R.id.til_tenant)
    TextInputLayout tilTenant;

    @BindView(R.id.til_base_url)
    TextInputLayout tilBaseUrl;

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
        etTenant.addTextChangedListener(this);
        etBaseUrl.addTextChangedListener(this);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult && !isFieldEmpty() && isUrlValid()) {
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

    public boolean isFieldEmpty() {
        if (etBaseUrl.getText().toString().trim().length() == 0) {
            return true;
        }
        if (etTenant.getText().toString().trim().length() == 0) {
            return  true;
        }
        return  false;
    }

    public boolean isUrlValid() {
        try {
            new URL(etBaseUrl.getText().toString());
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() == 0) {
            if (etBaseUrl.getText().toString().length() == 0) {
                tilBaseUrl.setErrorEnabled(true);
                tilBaseUrl.setError(getString(R.string.error_validation_blank,
                        getString(R.string.base_url)));
            }
            if (etTenant.getText().toString().length() == 0) {
                tilTenant.setErrorEnabled(true);
                tilTenant.setError(getString(R.string.error_validation_blank,
                        getString(R.string.tenant)));
            }
        } else {
            if (etBaseUrl.getText().toString().length() != 0) {
                tilBaseUrl.setErrorEnabled(false);
            }
            if (etTenant.getText().toString().length() != 0) {
                tilTenant.setErrorEnabled(false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}