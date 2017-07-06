package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.zxing.Result;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.BeneficiaryState;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by dilpreet on 6/7/17.
 */

public class QrCodeReaderFragment extends BaseFragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    public static QrCodeReaderFragment newInstance() {
        QrCodeReaderFragment fragment = new QrCodeReaderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setAutoFocus(true);
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
    @Override
    public void handleResult(Result result) {
        Gson gson = new Gson();
        Beneficiary beneficiary = gson.fromJson(result.getText(), Beneficiary.class);
        getActivity().getSupportFragmentManager().popBackStack();
        ((BaseActivity) getActivity()).replaceFragment(BeneficiaryApplicationFragment.
                newInstance(BeneficiaryState.CREATE_QR, beneficiary), true, R.id.container);
    }
}
