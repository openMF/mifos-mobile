package org.mifos.mobile.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.mifos.mobile.R;
import org.mifos.mobile.models.register.UserVerify;
import org.mifos.mobile.presenters.RegistrationVerificationPresenter;
import org.mifos.mobile.ui.activities.LoginActivity;
import org.mifos.mobile.ui.activities.base.BaseActivity;
import org.mifos.mobile.ui.fragments.base.BaseFragment;
import org.mifos.mobile.ui.views.RegistrationVerificationView;
import org.mifos.mobile.utils.Toaster;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 31/7/17.
 */

public class RegistrationVerificationFragment extends BaseFragment implements
        RegistrationVerificationView {

    @BindView(R.id.et_request_id)
    EditText etRequestId;

    @BindView(R.id.et_authentication_token)
    EditText etToken;

    @Inject
    RegistrationVerificationPresenter presenter;

    private View rootView;

    public static RegistrationVerificationFragment newInstance() {
        RegistrationVerificationFragment fragment = new RegistrationVerificationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_registration_verification, container, false);

        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        presenter.attachView(this);

        return rootView;
    }

    @OnClick(R.id.btn_verify)
    public void verifyClicked() {
        UserVerify userVerify = new UserVerify();
        userVerify.setAuthenticationToken(etToken.getText().toString());
        userVerify.setRequestId(etRequestId.getText().toString());
        presenter.verifyUser(userVerify);
    }

    @Override
    public void showVerifiedSuccessfully() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        Toast.makeText(getContext(), getString(R.string.verified), Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void showError(String msg) {
        Toaster.show(rootView, msg);
    }

    @Override
    public void showProgress() {
        showMifosProgressDialog(getString(R.string.verifying));
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
