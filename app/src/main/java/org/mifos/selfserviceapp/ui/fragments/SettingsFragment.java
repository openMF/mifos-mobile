package org.mifos.selfserviceapp.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.utils.LanguageHelper;

/**
 * Created by dilpreet on 02/10/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.
        OnSharedPreferenceChangeListener {

    private String[] languages;
    private LanguageCallback languageCallback;


    public static SettingsFragment newInstance(LanguageCallback languageCallback) {
        SettingsFragment fragment = new SettingsFragment();
        fragment.setLanguageCallback(languageCallback);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preference);
        languages = getActivity().getResources().getStringArray(R.array.languages);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);
    }
    public void setLanguageCallback(LanguageCallback languageCallback) {
        this.languageCallback = languageCallback;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            LanguageHelper.setLocale(getContext(), listPreference.getValue());
            languageCallback.updateNavDrawer();
            //refresh settings fragment
            setPreferenceScreen(null);
            addPreferencesFromResource(R.xml.settings_preference);
        }
    }

    public interface LanguageCallback {
        void updateNavDrawer();
    }
}
