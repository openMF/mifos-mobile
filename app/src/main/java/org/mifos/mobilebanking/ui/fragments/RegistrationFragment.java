package org.mifos.mobilebanking.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

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

    private static final String LIVE_UPDATE_ENABLED_TAG = "live_update_enabled";
    private static final String LIVE_UPDATE_DISABLED_TAG = "live_update_disabled";

    private View rootView;
    private boolean areAllInputsCorrect;
    private EditText firstInvalidInput;

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

        disableLiveUpdateForAllInputs();

        return rootView;
    }

    @OnTextChanged(R.id.et_account_number)
    public void onAccountNumberChanged() {
        if (isLiveUpdateEnabledInInput(etAccountNumber)) {
            validateAccountNumberInput();
        }
    }

    @OnTextChanged(R.id.et_username)
    public void onUsernameChanged() {
        if (isLiveUpdateEnabledInInput(etUsername)) {
            validateUsernameInput();
        }
    }

    @OnTextChanged(R.id.et_first_name)
    public void onFirstNameChanged() {
        if (isLiveUpdateEnabledInInput(etFirstName)) {
            validateFirstNameInput();
        }
    }

    @OnTextChanged(R.id.et_last_name)
    public void onLastNameChanged() {
        if (isLiveUpdateEnabledInInput(etLastName)) {
            validateLastNameInput();
        }
    }

    @OnTextChanged(R.id.et_phone_number)
    public void onPhoneNumberChanged() {
        if (isLiveUpdateEnabledInInput(etPhoneNumber)) {
            validatePhoneNumberInput();
        }
    }

    @OnTextChanged(R.id.et_email)
    public void onEmailChanged() {
        if (isLiveUpdateEnabledInInput(etEmail)) {
            validateEmailInput();
        }
    }

    @OnTextChanged(R.id.et_password)
    public void onPasswordChanged() {
        if (isLiveUpdateEnabledInInput(etPassword)) {
            validatePasswordInput();
        }
    }

    @OnTextChanged(R.id.et_confirm_password)
    public void onConfirmPasswordChanged() {
        if (isLiveUpdateEnabledInInput(etConfirmPassword)) {
            validateConfirmPasswordInput();
        }
    }

    @OnFocusChange(R.id.et_account_number)
    public void onAccountNumberLostFocus(boolean hasFocus) {
        if (!hasFocus) {
            validateAccountNumberInput();
        }
    }

    @OnFocusChange(R.id.et_username)
    public void onUsernameLostFocus(boolean hasFocus) {
        if (!hasFocus) {
            validateUsernameInput();
        }
    }

    @OnFocusChange(R.id.et_first_name)
    public void onFirstNameLostFocus(boolean hasFocus) {
        if (!hasFocus) {
            validateFirstNameInput();
        }
    }

    @OnFocusChange(R.id.et_last_name)
    public void onLastNameLostFocus(boolean hasFocus) {
        if (!hasFocus) {
            validateLastNameInput();
        }
    }

    @OnFocusChange(R.id.et_phone_number)
    public void onPhoneNumberLostFocus(boolean hasFocus) {
        if (!hasFocus) {
            validatePhoneNumberInput();
        }
    }

    @OnFocusChange(R.id.et_email)
    public void onEmailLostFocus(boolean hasFocus) {
        if (!hasFocus) {
            validateEmailInput();
        }
    }

    @OnFocusChange(R.id.et_password)
    public void onPasswordLostFocus(boolean hasFocus) {
        if (!hasFocus) {
            validatePasswordInput();
        }
    }

    @OnFocusChange(R.id.et_confirm_password)
    public void onConfirmPasswordLostFocus(boolean hasFocus) {
        if (!hasFocus) {
            validateConfirmPasswordInput();
        }
    }


    @OnClick(R.id.btn_register)
    public void registerClicked() {
        enableLiveUpdateForAllInputs();

        if (areAllInputsValidated()) {
            RadioButton radioButton =  rootView.findViewById(rgVerificationMode.
                    getCheckedRadioButtonId());

            RegisterPayload payload = new RegisterPayload();
            payload.setAccountNumber(etAccountNumber.getText().toString());
            payload.setAuthenticationMode(radioButton.getText().toString());
            payload.setEmail(etEmail.getText().toString());
            payload.setFirstName(etFirstName.getText().toString());
            payload.setLastName(etLastName.getText().toString());
            payload.setMobileNumber(etPhoneNumber.getText().toString());
            payload.setPassword(etPassword.getText().toString());
            payload.setUsername(etUsername.getText().toString().replace(" ", ""));

            if (Network.isConnected(getContext())) {
                presenter.registerUser(payload);
            } else {
                Toaster.show(rootView, getString(R.string.no_internet_connection));
            }
        }

        setFocusOnFirstInvalidInputIfExists();
    }

    private boolean areAllInputsValidated() {
        areAllInputsCorrect = true;
        firstInvalidInput = null;

        validateAccountNumberInput();
        validateUsernameInput();
        validateFirstNameInput();
        validateLastNameInput();
        validatePhoneNumberInput();
        validateEmailInput();
        validatePasswordInput();
        validateConfirmPasswordInput();
        return areAllInputsCorrect;
    }

    private void validateAccountNumberInput() {
        if (TextUtils.isEmpty(getTrimmedInputText(etAccountNumber))) {
            handleInvalidInputWithGivenMessage(etAccountNumber,
                    getString(R.string.error_validation_blank, getString(R.string.
                            account_number)));
        } else {
            handleCorrectInput(etAccountNumber);
        }
    }

    private void validateUsernameInput() {
        if (TextUtils.isEmpty(getTrimmedInputText(etUsername))) {
            handleInvalidInputWithGivenMessage(etUsername,
                    getString(R.string.error_validation_blank, getString(R.string.
                            username)));
        } else if (getTrimmedInputText(etUsername).length() < 6) {
            handleInvalidInputWithGivenMessage(etUsername,
                    getString(R.string.error_username_greater_than_six));
        } else if (getTrimmedInputText(etUsername).contains(" ")) {
            handleInvalidInputWithGivenMessage(etUsername,
                    getString(R.string.error_validation_cannot_contain_spaces,
                            getString(R.string.username),
                            getString(R.string.not_contain_username)));
        } else {
            handleCorrectInput(etUsername);
        }
    }

    private void validateFirstNameInput() {
        if (TextUtils.isEmpty(getTrimmedInputText(etFirstName))) {
            handleInvalidInputWithGivenMessage(etFirstName,
                    getString(R.string.error_validation_blank, getString(R.string.
                            first_name)));
        } else {
            handleCorrectInput(etFirstName);
        }
    }

    private void validateLastNameInput() {
        if (TextUtils.isEmpty(getTrimmedInputText(etLastName))) {
            handleInvalidInputWithGivenMessage(etLastName,
                    getString(R.string.error_validation_blank, getString(R.string.
                            last_name)));
        } else {
            handleCorrectInput(etLastName);
        }
    }

    private void validatePhoneNumberInput() {
        if (TextUtils.isEmpty(getTrimmedInputText(etPhoneNumber))) {
            handleInvalidInputWithGivenMessage(etPhoneNumber,
                    getString(R.string.error_validation_blank, getString(R.string.
                            phone_number)));
        } else {
            handleCorrectInput(etPhoneNumber);
        }
    }

    private void validateEmailInput() {
        if (TextUtils.isEmpty(getTrimmedInputText(etEmail))) {
            handleInvalidInputWithGivenMessage(etEmail,
                    getString(R.string.error_validation_blank, getString(R.string.
                            email)));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(getTrimmedInputText(etEmail))
                .matches()) {
            handleInvalidInputWithGivenMessage(etEmail,
                    getString(R.string.error_invalid_email));
        } else {
            handleCorrectInput(etEmail);
        }
    }

    private void validatePasswordInput() {
        if (getTrimmedInputText(etPassword).length() < etPassword.getText().toString().length()) {
            handleInvalidInputWithGivenMessage(etPassword,
                    getString(R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                            getString(R.string.password)));
        } else if (TextUtils.isEmpty(getTrimmedInputText(etPassword))) {
            handleInvalidInputWithGivenMessage(etPassword,
                    getString(R.string.error_validation_blank, getString(R.string.
                            password)));
        } else if (getTrimmedInputText(etPassword).length() < 6) {
            handleInvalidInputWithGivenMessage(etPassword,
                    getString(R.string.error_validation_minimum_chars,
                            getString(R.string.password), getResources().
                                    getInteger(R.integer.password_minimum_length)));
        } else {
            handleCorrectInput(etPassword);
            validateConfirmPasswordInput();
        }
    }

    private void validateConfirmPasswordInput() {
        if (!etPassword.getText().toString()
                .equals(etConfirmPassword.getText().toString())) {
            handleInvalidInputWithGivenMessage(etConfirmPassword,
                    getString(R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                            getString(R.string.password)));
            handleInvalidInputWithGivenMessage(etConfirmPassword,
                    getString(R.string.error_password_not_match));
        } else if (getTrimmedInputText(etConfirmPassword).length()
                < etConfirmPassword.getText().toString().length()) {
            handleInvalidInputWithGivenMessage(etConfirmPassword,
                    getString(R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                            getString(R.string.password)));
        } else if (TextUtils.isEmpty(getTrimmedInputText(etConfirmPassword))) {
            handleInvalidInputWithGivenMessage(etConfirmPassword,
                    getString(R.string.error_validation_blank, getString(R.string.
                            password)));
        } else if (getTrimmedInputText(etConfirmPassword).length() < 6) {
            handleInvalidInputWithGivenMessage(etConfirmPassword,
                    getString(R.string.error_validation_minimum_chars,
                            getString(R.string.password), getResources().
                                    getInteger(R.integer.password_minimum_length)));
        } else {
            handleCorrectInput(etConfirmPassword);
        }
    }

    private String getTrimmedInputText(EditText input) {
        return input.getText().toString().trim();
    }

    private void handleInvalidInputWithGivenMessage(EditText input, String errorMsg) {
        areAllInputsCorrect = false;
        setFirstInvalidInputIfNotExists(input);
        showInputError(input, errorMsg);
        enableLiveUpdateForInput(input);
    }

    private void setFirstInvalidInputIfNotExists(EditText input) {
        if (firstInvalidInput == null) {
            firstInvalidInput = input;
        }
    }

    private void showInputError(EditText input, String errorMsg) {
        TextInputLayout textInputLayout = getInputLayoutByGivenEditText(input);
        textInputLayout.setError(errorMsg);
    }

    private void handleCorrectInput(EditText input) {
        disableLiveUpdateForInput(input);
        hideInputError(input);
    }

    private void hideInputError(EditText input) {
        TextInputLayout textInputLayout = getInputLayoutByGivenEditText(input);
        textInputLayout.setError(null);
    }

    private TextInputLayout getInputLayoutByGivenEditText(EditText input) {
        return (TextInputLayout) input.getParent().getParent();
    }

    private void setFocusOnFirstInvalidInputIfExists() {
        if (firstInvalidInput != null) {
            clearFocusIfFirstInvalidInputHasItToAvoidBug();
            firstInvalidInput.requestFocus();
            forceKeyboardToAppearOnFirstInvalidInput();
        }
    }

    private void clearFocusIfFirstInvalidInputHasItToAvoidBug() {
        if (firstInvalidInput.hasFocus()) {
            firstInvalidInput.clearFocus();
        }
    }

    private void forceKeyboardToAppearOnFirstInvalidInput() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(firstInvalidInput, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private boolean isLiveUpdateEnabledInInput(EditText input) {
        return input.getTag().toString().equals(LIVE_UPDATE_ENABLED_TAG);
    }

    private void disableLiveUpdateForAllInputs() {
        disableLiveUpdateForInput(etAccountNumber);
        disableLiveUpdateForInput(etUsername);
        disableLiveUpdateForInput(etFirstName);
        disableLiveUpdateForInput(etLastName);
        disableLiveUpdateForInput(etPhoneNumber);
        disableLiveUpdateForInput(etEmail);
        disableLiveUpdateForInput(etPassword);
        disableLiveUpdateForInput(etConfirmPassword);
    }

    private void enableLiveUpdateForAllInputs() {
        enableLiveUpdateForInput(etAccountNumber);
        enableLiveUpdateForInput(etUsername);
        enableLiveUpdateForInput(etFirstName);
        enableLiveUpdateForInput(etLastName);
        enableLiveUpdateForInput(etPhoneNumber);
        enableLiveUpdateForInput(etEmail);
        enableLiveUpdateForInput(etPassword);
        enableLiveUpdateForInput(etConfirmPassword);
    }

    private void disableLiveUpdateForInput(EditText input) {
        input.setTag(LIVE_UPDATE_DISABLED_TAG);
    }

    private void enableLiveUpdateForInput(EditText input) {
        input.setTag(LIVE_UPDATE_ENABLED_TAG);
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
