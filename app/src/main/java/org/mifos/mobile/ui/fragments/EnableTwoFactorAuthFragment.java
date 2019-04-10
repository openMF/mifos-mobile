package org.mifos.mobile.ui.fragments;

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import org.mifos.mobile.R;
import org.mifos.mobile.ui.fragments.base.BaseFragment;
import org.mifos.mobile.utils.Constants;
import org.mifos.mobile.utils.QrCodeGenerator;
import org.mifos.mobile.utils.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EnableTwoFactorAuthFragment extends BaseFragment {

    @BindView(R.id.tv_key)
    TextView tvKey;

    @BindView(R.id.iv_qrcode)
    ImageView ivQrcde;

    @BindView(R.id.et_code)
    EditText etCode;

    View rootView;
    GoogleAuthenticator gAuth;
    String sharedKey;
    SharedPreferences sharedPreferences;

    public static EnableTwoFactorAuthFragment newInstance() {
        EnableTwoFactorAuthFragment fragment = new EnableTwoFactorAuthFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_enable_two_factor_auth, container, false);
        ButterKnife.bind(this, rootView);
        setToolbarTitle(getString(R.string.title_two_factor));

        sharedPreferences = getActivity()
                .getSharedPreferences(Constants.TWO_FACTOR_AUTHENTICATION,
                Context.MODE_PRIVATE);

        System.setProperty("com.warrenstrange.googleauth.rng.algorithmProvider", "AndroidOpenSSL");
        gAuth = new GoogleAuthenticator();

        GoogleAuthenticatorKey key = gAuth.createCredentials();
        sharedKey = key.getKey();

        tvKey.setText(sharedKey);
        ivQrcde.setImageBitmap(QrCodeGenerator.encodeAsBitmap(sharedKey));

        return rootView;
    }

    @OnClick(R.id.btn_submit)
    void verify() {
        String stCode = etCode.getText().toString();
        if (!stCode.isEmpty()) {
            try {
                Integer code = Integer.parseInt(stCode);
                boolean isCodeValid = gAuth.authorize(sharedKey, code);
                if (isCodeValid) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.TWO_FACTOR_AUTHENTICATION_KEY, sharedKey);
                    editor.putBoolean(Constants.IS_TWO_FACTOR_AUTHENTICATION, true);
                    editor.apply();
                    Intent intent = new Intent(getActivity(), getActivity().getClass());
                    intent.putExtra(Constants.HAS_SETTINGS_CHANGED, true);
                    startActivity(intent);
                    getActivity().finish();
                    Toaster.show(rootView, getString(R.string.totp_enabled));
                } else {
                    Toaster.show(rootView, getString(R.string.incorrect_code));
                }
            } catch (Exception e) {
                Toaster.show(rootView, getString(R.string.invalid_code));
            }
        } else {
            Toaster.show(rootView, getString(R.string.enter_verification_code));
        }
    }

    @OnClick(R.id.tv_key)
    void copyText() {
        ClipboardManager clipboard = (ClipboardManager)
                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.key_label),
                tvKey.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), getString(R.string.toast_copied), Toast.LENGTH_SHORT).show();
    }

}
