package org.mifos.mobilebanking.ui.fragments;

/*
 * Created by saksham on 13/July/2018
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.models.UpdatePasswordPayload;
import org.mifos.mobilebanking.presenters.UpdatePasswordPresenter;
import org.mifos.mobilebanking.ui.activities.SettingsActivity;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.UpdatePasswordView;
import org.mifos.mobilebanking.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePasswordFragment extends BaseFragment implements UpdatePasswordView,
        TextWatcher, View.OnFocusChangeListener {

    @BindView(R.id.til_new_password)
    TextInputLayout tilNewPassword;

    @BindView(R.id.til_confirm_new_password)
    TextInputLayout tilConfirmNewPassword;

    @Inject
    UpdatePasswordPresenter presenter;

    @Inject
    PreferencesHelper preferencesHelper;

    private View rootView;
    private UpdatePasswordPayload payload;
    private boolean isFocusLostNewPassword = false;
    private boolean isFocusLostConfirmPassword = false;

    public static UpdatePasswordFragment newInstance() {
        return new UpdatePasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_update_password, container, false);
        setToolbarTitle(getString(R.string.change_password));
        ButterKnife.bind(this, rootView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);

        tilNewPassword.getEditText().addTextChangedListener(this);
        tilConfirmNewPassword.getEditText().addTextChangedListener(this);
        tilNewPassword.getEditText().setOnFocusChangeListener(this);
        tilConfirmNewPassword.getEditText().setOnFocusChangeListener(this);
        return rootView;
    }

    @OnClick(R.id.btn_update_password)
    void updatePassword() {
        if (isFieldsCompleted()) {
            presenter.updateAccountPassword(getUpdatePasswordPayload());
        }
    }

    private boolean isFieldsCompleted() {
        boolean rv = true;
        String newPassword = tilNewPassword.getEditText().getText().toString().trim();
        String repeatPassword = tilConfirmNewPassword.getEditText().getText().toString().trim();

        if (!checkNewPasswordFieldsComplete()) {
            rv = false;
        }
        if (!checkConfirmPasswordFieldsComplete()) {
            rv = false;
        }
        if (!newPassword.equals(repeatPassword)) {
            Toaster.show(rootView, getString(R.string.error_password_not_match));
            rv = false;
        }
        return rv;
    }

    private UpdatePasswordPayload getUpdatePasswordPayload() {
        payload = new UpdatePasswordPayload();
        payload.setPassword(tilNewPassword.getEditText().getText().toString().trim());
        payload.setRepeatPassword(tilConfirmNewPassword.getEditText().getText().toString().trim());
        return payload;
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void showPasswordUpdatedSuccessfully() {
        Toast.makeText(getContext(), getString(R.string.string_changed_successfully,
                getString(R.string.password)), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getContext(), SettingsActivity.class));
    }

    @Override
    public void showProgress() {
        showMifosProgressDialog(getString(R.string.progress_message_loading));
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (tilNewPassword.getEditText().hasFocus() && isFocusLostNewPassword) {
            checkNewPasswordFieldsComplete();
        }
        if (tilConfirmNewPassword.getEditText().hasFocus() && isFocusLostConfirmPassword) {
            checkConfirmPasswordFieldsComplete();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.et_new_password && !isFocusLostNewPassword && !hasFocus) {
            checkNewPasswordFieldsComplete();
            isFocusLostNewPassword = true;
        }
        if (v.getId() == R.id.et_confirm_password && !isFocusLostConfirmPassword && !hasFocus) {
            checkConfirmPasswordFieldsComplete();
            isFocusLostConfirmPassword = true;
        }
    }

    private boolean checkNewPasswordFieldsComplete() {
        String newPassword = tilNewPassword.getEditText().getText().toString();
        isFocusLostNewPassword = true;
        if (newPassword.isEmpty()) {
            tilNewPassword.setError(
                    getString(R.string.error_validation_blank,
                            getString(R.string.new_password)));
            return false;
        }
        if (newPassword.length() < 6) {
            tilNewPassword.setError(
                    getString(R.string.error_validation_minimum_chars,
                            getString(R.string.new_password),
                            getResources().getInteger(R.integer.password_minimum_length)));
            return false;
        }
        tilNewPassword.setErrorEnabled(false);
        return true;
    }

    private boolean checkConfirmPasswordFieldsComplete() {
        String confirmPassword = tilConfirmNewPassword.getEditText().getText().toString();
        isFocusLostConfirmPassword = true;
        if (confirmPassword.isEmpty()) {
            tilConfirmNewPassword.setError(
                    getString(R.string.error_validation_blank,
                            getString(R.string.confirm_password)));
            return false;
        }
        if (confirmPassword.length() < 6) {
            tilConfirmNewPassword.setError(
                    getString(R.string.error_validation_minimum_chars,
                            getString(R.string.confirm_password),
                            getResources().getInteger(R.integer.password_minimum_length)));
            return false;
        }
        tilConfirmNewPassword.setErrorEnabled(false);
        return true;
    }
}
