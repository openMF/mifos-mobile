package org.mifos.mobile.ui.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.feature.qr.qr_code_display.QrCodeDisplayScreen
import org.mifos.mobile.feature.qr.qr_code_display.QrCodeDisplayViewModel

// Can be deleted after fully implementing compose navigation
// Used in LoanAccountDetail and SavingsAccountDetail Fragments
@AndroidEntryPoint
class QrCodeDisplayComposeFragment : BaseFragment() {

    private val viewModel by viewModels<QrCodeDisplayViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? BaseActivity)?.hideToolbar()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
//            QrCodeDisplayScreen(
//                navigateBack = { activity?.onBackPressed() }
//            )
        }
    }

    companion object {
        fun newInstance(json: String?): QrCodeDisplayComposeFragment {
            val fragment = QrCodeDisplayComposeFragment()
            val args = Bundle()
            args.putString(Constants.QR_DATA, json)
            fragment.arguments = args
            return fragment
        }
    }
}