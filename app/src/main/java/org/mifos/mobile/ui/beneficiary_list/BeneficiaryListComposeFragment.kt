package org.mifos.mobile.ui.beneficiary_list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryNavigation

@AndroidEntryPoint
class BeneficiaryListComposeFragment : BaseFragment() {

    companion object {
        fun newInstance(): BeneficiaryListComposeFragment {
            return BeneficiaryListComposeFragment()
        }
    }
}