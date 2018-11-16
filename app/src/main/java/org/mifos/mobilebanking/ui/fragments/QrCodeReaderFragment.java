package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.Result;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.beneficiary.Beneficiary;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.BeneficiaryState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;

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

    /**
     * Sets the {@link me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler} callback and
     * opens Camera
     */
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    /**
     * Closes the Camera
     */
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    /**
     * Callback for {@link ZXingScannerView} which retrieves data from QRCode
     * @param result Contains data scanned from QRCode
     */
    @Override
    public void handleResult(Result result) {
        Gson gson = new Gson();
        try {
            Beneficiary beneficiary = gson.fromJson(result.getText(), Beneficiary.class);
            getActivity().getSupportFragmentManager().popBackStack();
            ((BaseActivity) getActivity()).replaceFragment(BeneficiaryApplicationFragment.
                    newInstance(BeneficiaryState.CREATE_QR, beneficiary), true, R.id.container);
        } catch (JsonSyntaxException e) {
            Toast.makeText(getActivity(), getString(R.string.invalid_qr),
                    Toast.LENGTH_SHORT).show();
            mScannerView.resumeCameraPreview(this);
        }
    }
}
