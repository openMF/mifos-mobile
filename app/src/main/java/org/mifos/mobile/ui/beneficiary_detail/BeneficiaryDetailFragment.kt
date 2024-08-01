package org.mifos.mobile.ui.beneficiary_detail;

/*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.beneficiary.beneficiary_detail.BeneficiaryDetailScreen
import org.mifos.mobile.feature.beneficiary.beneficiary_detail.BeneficiaryDetailViewModel

@AndroidEntryPoint
class BeneficiaryDetailFragment : BaseFragment() {

    private val viewModel: BeneficiaryDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.setBeneficiary(it.getParcelable(Constants.BENEFICIARY))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return mifosComposeView(requireContext()) {
            BeneficiaryDetailScreen(
                navigateBack = {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                },
                updateBeneficiary = {
                    updateBeneficiaryClicked()
                }
            )
        }
    }

    private fun updateBeneficiaryClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            org.mifos.mobile.ui.beneficiary_application.BeneficiaryApplicationComposeFragment.newInstance(
                BeneficiaryState.UPDATE,
                viewModel.getBeneficiary()
            ),
            true,
            R.id.container,
        )
    }

    override fun onPause() {
        super.onPause()
        (activity as? BaseActivity)?.showToolbar()
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
    }

    companion object {
        fun newInstance(beneficiary: Beneficiary?): BeneficiaryDetailFragment {
            val fragment = BeneficiaryDetailFragment()
            val args = Bundle().apply {
                putParcelable(Constants.BENEFICIARY, beneficiary)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
 */