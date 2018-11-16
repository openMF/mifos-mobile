package org.mifos.mobilebanking.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.Result;
import com.isseiaoki.simplecropview.CropImageView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.beneficiary.Beneficiary;
import org.mifos.mobilebanking.presenters.QrCodeImportPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.BeneficiaryState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.QrCodeImportView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.Toaster;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by manishkumar on 19/05/18.
 */

public class QrCodeImportFragment extends BaseFragment implements QrCodeImportView {

    private View rootView;
    private Uri qrUri;
    private String uriValue;
    private RectF mFrameRect = null;
    private InputStream inputStream = null;


    @BindView(R.id.iv_crop_qr_code)
    CropImageView cropImageView;

    @Inject
    QrCodeImportPresenter qrCodeImportPresenter;

    public static QrCodeImportFragment newInstance(Uri uri) {

        QrCodeImportFragment fragment = new QrCodeImportFragment();
        Bundle args = new Bundle();
        args.putString(Constants.QR_IMAGE_URI, uri.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uriValue = getArguments().getString(Constants.QR_IMAGE_URI);
            qrUri = Uri.parse(uriValue);
        }
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_qr_code_import, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        setToolbarTitle(getString(R.string.import_qr));

        ButterKnife.bind(this, rootView);
        //load the uri

        setBitmapImage(qrUri);
        cropImageView.setCompressFormat(Bitmap.CompressFormat.JPEG);
        cropImageView.setOutputMaxSize(150, 150);
        cropImageView.load(qrUri)
                .initialFrameRect(mFrameRect)
                .executeAsCompletable();
        cropImageView.setCropMode(CropImageView.CropMode.FREE);
        cropImageView.setInitialFrameScale(0.8f);
        qrCodeImportPresenter.attachView(this);
        return rootView;
    }


    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data
        outState.putParcelable(Constants.FRAME_RECT, cropImageView.getActualCropRect());
        outState.putParcelable(Constants.SOURCE_URI, cropImageView.getSourceUri());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // restore data
            mFrameRect = savedInstanceState.getParcelable(Constants.FRAME_RECT);
            qrUri = savedInstanceState.getParcelable(Constants.SOURCE_URI);
        }
    }


    @OnClick(R.id.btn_proceed)
    public void proceed () {
        qrCodeImportPresenter.getDecodedResult(qrUri, cropImageView);
    }


    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
    @Override
    public void showErrorReadingQr(String message) {
        Toaster.show(rootView, message);
    }



    /**
     * CallBack for{@link CropImageView} which retrieves data from QRCode
     * Opens {@link BeneficiaryApplicationFragment} with {@link BeneficiaryState} as
     * {@code BeneficiaryState.CREATE_QR}
     * @param result contains the results from decoded QR bitmap
     */
    @Override
    public void handleDecodedResult(Result result) {

        Gson gson = new Gson();
        try {
            Beneficiary beneficiary = gson.fromJson(result.getText(), Beneficiary.class);
            getActivity().getSupportFragmentManager().popBackStack();
            ((BaseActivity) getActivity()).replaceFragment(BeneficiaryApplicationFragment.
                    newInstance(BeneficiaryState.CREATE_QR, beneficiary),
                    true, R.id.container);
        } catch (JsonSyntaxException e) {
            Toast.makeText(getActivity(), getString(R.string.invalid_qr),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Shows {@link org.mifos.mobilebanking.utils.ProgressBarHandler}
     */
    @Override
    public void showProgress() {
        showProgressBar();
    }


    /**
     * Hides {@link org.mifos.mobilebanking.utils.ProgressBarHandler}
     */
    @Override
    public void hideProgress() {
        hideProgressBar();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
        qrCodeImportPresenter.detachView();
    }

    /**
     * Initializing UI
     * @param qrImageUri contains Uri of qr code image
     */
    public void setBitmapImage(Uri qrImageUri) {
        try {
            inputStream = getContext().getContentResolver().openInputStream(qrImageUri);
        } catch (FileNotFoundException e) {
            Toaster.show(rootView, getString(R.string.error_fetching_image));
        }

        Bitmap b = BitmapFactory.decodeStream(inputStream, null, null);
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            Toaster.show(rootView, getString(R.string.error_fetching_image));
        }
        cropImageView.setImageBitmap(b);

    }

}
