package org.mifos.mobilebanking.ui.activities;

import android.os.Bundle;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.fragments.BeneficiaryAddOptionsFragment;

public class AddBeneficiary extends HomeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragment(BeneficiaryAddOptionsFragment.
                newInstance(), true, R.id.container);
    }
}
