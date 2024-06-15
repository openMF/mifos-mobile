package org.mifos.mobile.ui.client_charge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedSerializable

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
@AndroidEntryPoint
class ClientChargeComposeFragment : BaseFragment() {

    private val viewModel: ClientChargeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? BaseActivity)?.hideToolbar()
        if (arguments != null) {
            viewModel.initArgs(
                clientId = arguments?.getLong(Constants.CLIENT_ID),
                chargeType = arguments?.getCheckedSerializable(ChargeType::class.java, Constants.CHARGE_TYPE) as ChargeType
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            ClientChargeScreen (
                navigateBack = { activity?.onBackPressed() }
            )
        }
    }

    companion object {
        fun newInstance(clientId: Long?, chargeType: ChargeType?): ClientChargeComposeFragment {
            val clientChargeFragment = ClientChargeComposeFragment()
            val args = Bundle()
            if (clientId != null) {
                args.putLong(Constants.CLIENT_ID, clientId)
            }
            args.putSerializable(Constants.CHARGE_TYPE, chargeType)
            clientChargeFragment.arguments = args
            return clientChargeFragment
        }
    }
}
