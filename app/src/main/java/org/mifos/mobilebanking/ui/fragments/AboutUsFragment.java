package org.mifos.mobilebanking.ui.fragments;

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;


public class AboutUsFragment extends BaseFragment {

    private View rootView;

    public static AboutUsFragment getInstance() {
        AboutUsFragment aboutUsFragment = new AboutUsFragment();
        return aboutUsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        return rootView;
    }

}
