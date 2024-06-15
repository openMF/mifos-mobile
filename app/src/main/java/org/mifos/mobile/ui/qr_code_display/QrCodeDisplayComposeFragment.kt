package org.mifos.mobile.ui.qr_code_display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants

@AndroidEntryPoint
class QrCodeDisplayComposeFragment : BaseFragment() {

    private val viewModel by viewModels<QrCodeDisplayViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? BaseActivity)?.hideToolbar()
        if (arguments != null) {
            viewModel.setQrString(arguments?.getString(Constants.QR_DATA))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            QrCodeDisplayScreen(
                navigateBack = { activity?.onBackPressed() }
            )
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
