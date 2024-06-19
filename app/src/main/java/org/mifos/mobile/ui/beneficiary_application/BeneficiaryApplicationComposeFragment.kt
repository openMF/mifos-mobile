package org.mifos.mobile.ui.beneficiary_application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.BeneficiaryState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedSerializable

/**
 * Created by dilpreet on 16/6/17.
 */
@AndroidEntryPoint
class BeneficiaryApplicationComposeFragment : BaseFragment() {

    private val viewModel: BeneficiaryApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            viewModel.initArgs(
                beneficiaryState = arguments?.getCheckedSerializable(BeneficiaryState::class.java, Constants.BENEFICIARY_STATE) as BeneficiaryState ,
                beneficiary = arguments?.getCheckedParcelable(Beneficiary::class.java, Constants.BENEFICIARY)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            BeneficiaryApplicationScreen(
                navigateBack = { activity?.onBackPressed() }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
    }

    companion object {
        fun newInstance(
            beneficiaryState: BeneficiaryState?,
            beneficiary: Beneficiary?,
        ): BeneficiaryApplicationComposeFragment {
            val fragment = BeneficiaryApplicationComposeFragment()
            val args = Bundle()
            args.putSerializable(Constants.BENEFICIARY_STATE, beneficiaryState)
            if (beneficiary != null) {
                args.putParcelable(Constants.BENEFICIARY, beneficiary)
            }
            fragment.arguments = args
            return fragment
        }
    }
}