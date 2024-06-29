package org.mifos.mobile.ui.beneficiary_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.beneficiary.presentation.BeneficiaryAddOptionsFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.feature.beneficiary.beneficiary_list.BeneficiaryListScreen
import org.mifos.mobile.feature.guarantor.R

@AndroidEntryPoint
class BeneficiaryListComposeFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            BeneficiaryListScreen(
                navigateBack = { activity?.supportFragmentManager?.popBackStack() },
                addBeneficiaryClicked = { addBeneficiary() },
                onBeneficiaryItemClick = { position, beneficiaryList ->
                    onItemClick(
                        position = position,
                        beneficiaryList = beneficiaryList
                    )
                },
            )
        }
    }

    private fun onItemClick(position: Int, beneficiaryList: List<Beneficiary>) {
        (activity as? BaseActivity)?.replaceFragment(
            org.mifos.mobile.ui.beneficiary_detail.BeneficiaryDetailFragment.newInstance(beneficiaryList[position]),
            true,
            R.id.container,
        )
    }

    private fun addBeneficiary() {
        (activity as? BaseActivity)?.replaceFragment(
            BeneficiaryAddOptionsFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
    }

    companion object {
        fun newInstance(): BeneficiaryListComposeFragment {
            return BeneficiaryListComposeFragment()
        }
    }
}