package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.register.RegisterPayload;
import org.mifos.mobilebanking.presenters.RegistrationPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.RegistrationView;
import org.mifos.mobilebanking.utils.Network;
import org.mifos.mobilebanking.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 31/7/17.
 */

public class RegistrationFragment extends BaseFragment implements RegistrationView {

    @BindView(R.id.et_account_number)
    EditText etAccountNumber;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_first_name)
    EditText etFirstName;

    @BindView(R.id.et_last_name)
    EditText etLastName;

    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;

    @BindView(R.id.rg_verification_mode)
    RadioGroup rgVerificationMode;

    @Inject
    RegistrationPresenter presenter;

    private View rootView;

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        presenter.attachView(this);

        return rootView;
    }

    @OnClick(R.id.btn_register)
    public void registerClicked() {

        if (areFieldsValidated()) {

            RadioButton radioButton =  rootView.findViewById(rgVerificationMode.
                    getCheckedRadioButtonId());

            RegisterPayload payload = new RegisterPayload();
            payload.setAccountNumber(etAccountNumber.getText().toString());
            payload.setAuthenticationMode(radioButton.getText().toString());
            payload.setEmail(etEmail.getText().toString());
            payload.setFirstName(etFirstName.getText().toString());
            payload.setLastName(etLastName.getText().toString());
            payload.setMobileNumber(etPhoneNumber.getText().toString());
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                Toaster.show(rootView, getString(R.string.error_password_not_match));
                return;
            } else {
                payload.setPassword(etPassword.getText().toString());
            }
            payload.setPassword(etPassword.getText().toString());
            payload.setUsername(etUsername.getText().toString().replace(" ", ""));

            if (Network.isConnected(getContext())) {
                presenter.registerUser(payload);
            } else {
                Toaster.show(rootView, getString(R.string.no_internet_connection));
            }
        }

    }

    private boolean areFieldsValidated() {

        if (etAccountNumber.getText().toString().trim().length() == 0) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.
                    account_number)));
            return false;
        } else if (etUsername.getText().toString().trim().length() == 0) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.
                    username)));
            return false;
        } else if (etUsername.getText().toString().trim().length() < 5) {
            Toaster.show(rootView, getString(R.string.error_validation_minimum_chars,
                    getString(R.string.username),
                    getResources().getInteger(R.integer.username_minimum_length)));
            return false;
        } else if (etUsername.getText().toString().trim().contains(" ")) {
            Toaster.show(rootView, getString(R.string.error_validation_cannot_contain_spaces,
                    getString(R.string.username), getString(R.string.not_contain_username)));
            return false;
        } else if (etFirstName.getText().length() == 0) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.
                    first_name)));
            return false;
        } else if (etLastName.getText().toString().trim().length() == 0) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.
                    last_name)));
            return false;
        } else if (etEmail.getText().toString().trim().length() == 0) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.
                    email)));
            return false;
        } else if (etPassword.getText().toString().trim().length() == 0) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.
                    password)));
            return false;
        } else if (etPassword.getText().toString().trim().length()
                                        < etPassword.getText().toString().length()) {
            Toaster.show(rootView,
                    getString(R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                    getString(R.string.password)));
            return false;
        } else if (etUsername.getText().toString().trim().length() < 6) {
            Toaster.show(rootView, getString(R.string.error_username_greater_than_six));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher( etEmail.getText().toString().trim())
                .matches()) {
            Toaster.show(rootView, getString(R.string.error_invalid_email));
            return false;
        } else if (etPassword.getText().toString().trim().length() < 6) {
            Toaster.show(rootView, getString(R.string.error_validation_minimum_chars,
                        getString(R.string.password), getResources().
                            getInteger(R.integer.password_minimum_length)));
            return false;
        }

        return true;
    }

    @Override
    public void showRegisteredSuccessfully() {
        ((BaseActivity) getActivity()).replaceFragment(RegistrationVerificationFragment.
                newInstance(), true, R.id.container);
    }

    @Override
    public void showError(String msg) {
        Toaster.show(rootView, msg);
    }

    @Override
    public void showProgress() {
        showMifosProgressDialog(getString(R.string.sign_up));
    }

    @Override
    public void hideProgress() {
        hideMifosProgressDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
