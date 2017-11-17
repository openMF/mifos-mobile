package org.mifos.mobilebanking.ui.fragments;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.QrCodeGenerator;
import org.mifos.mobilebanking.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 16/8/17.
 */

public class QrCodeDisplayFragment extends BaseFragment {


    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;

    private View rootView;
    private String json;

    public static QrCodeDisplayFragment newInstance(String json) {
        QrCodeDisplayFragment fragment = new QrCodeDisplayFragment();
        Bundle args = new Bundle();
        args.putString(Constants.QR_DATA, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            json = getArguments().getString(Constants.QR_DATA);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_qr_code_display, container, false);
        ButterKnife.bind(this, rootView);

        ivQrCode.setImageBitmap(QrCodeGenerator.encodeAsBitmap(json));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_qr_code_display, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_qr_code_share:
                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivQrCode.getDrawable();
                Uri uri = Utils.getImageUri(getActivity(), bitmapDrawable.getBitmap());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, getString(R.string.choose_option)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
