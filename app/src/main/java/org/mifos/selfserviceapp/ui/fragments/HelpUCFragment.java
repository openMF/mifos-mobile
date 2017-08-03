package org.mifos.selfserviceapp.ui.fragments;

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;


public class HelpUCFragment extends BaseFragment {

    private View rootView;

    public static HelpUCFragment getInstance() {
        HelpUCFragment helpUCFragment = new HelpUCFragment();
        return helpUCFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_help_uc, container, false);
        return rootView;
    }

}
