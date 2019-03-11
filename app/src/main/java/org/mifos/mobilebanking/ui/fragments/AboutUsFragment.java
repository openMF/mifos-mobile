package org.mifos.mobilebanking.ui.fragments;

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import org.mifos.mobilebanking.BuildConfig;
import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.activities.PrivacyPolicyActivity;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AboutUsFragment extends BaseFragment {

    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    @BindView(R.id.tv_copy_right)
    TextView tvCopyRight;

    View rootView;

    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, rootView);
        setToolbarTitle(getString(R.string.about_us));

        tvAppVersion.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME));

        tvCopyRight.setText(getString(R.string.copy_right_mifos,
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR))));

        return rootView;
    }

    @OnClick(R.id.tv_licenses)
    void showOpenSourceLicenses() {
        startActivity(new Intent(getActivity(), OssLicensesMenuActivity.class));
    }

    @OnClick(R.id.tv_privacy_policy)
    void showPrivacyPolicy() {
        startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
    }
}
