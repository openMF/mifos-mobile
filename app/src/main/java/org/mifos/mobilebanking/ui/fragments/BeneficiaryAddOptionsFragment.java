package org.mifos.mobilebanking.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.BeneficiaryState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.utils.CheckSelfPermissionAndRequest;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.Toaster;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 5/7/17.
 */

public class BeneficiaryAddOptionsFragment extends BaseFragment {

    private View rootView;

    public static BeneficiaryAddOptionsFragment newInstance() {
        BeneficiaryAddOptionsFragment fragment = new BeneficiaryAddOptionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_beneficiary_add_options, container, false);
        setToolbarTitle(getString(R.string.add_beneficiary));
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    /**
     * Opens {@link BeneficiaryApplicationFragment} with {@link BeneficiaryState} as
     * {@code BeneficiaryState.CREATE_MANUAL}
     */
    @OnClick(R.id.ll_add_beneficiary_manually)
    public void addManually() {
        ((BaseActivity) getActivity()).replaceFragment(BeneficiaryApplicationFragment.
                newInstance(BeneficiaryState.CREATE_MANUAL, null), true, R.id.container);
    }

    /**
     * It first checks CAMERA runtime permission and if it returns true then it opens
     * {@link QrCodeReaderFragment} , if it returns false then ask for permissions.
     */
    @OnClick(R.id.ll_add_beneficiary_qrcode)
    public void addUsingQrCode() {

        if (CheckSelfPermissionAndRequest.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)) {
            ((BaseActivity) getActivity()).replaceFragment(QrCodeReaderFragment.
                    newInstance(), true, R.id.container);
        } else {
            requestPermission();
        }
    }

    /**
     * Uses {@link CheckSelfPermissionAndRequest} to check for runtime permissions
     */
    private void requestPermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                (BaseActivity) getActivity(),
                Manifest.permission.CAMERA,
                Constants.PERMISSIONS_REQUEST_CAMERA,
                getResources().getString(
                        R.string.dialog_message_camera_permission_denied_prompt),
                getResources().getString(R.string.dialog_message_camera_permission_never_ask_again),
                Constants.PERMISSIONS_CAMERA_STATUS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_CAMERA : {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((BaseActivity) getActivity()).replaceFragment(QrCodeReaderFragment.
                            newInstance(), true, R.id.container);

                } else {

                    Toaster.show(rootView, getResources()
                            .getString(R.string.permission_denied_camera));
                }
            }
        }
    }
}
