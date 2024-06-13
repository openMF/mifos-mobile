package org.mifos.mobile.ui.beneficiary_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.beneficiary.presentation.BeneficiaryAddOptionsFragment
import org.mifos.mobile.ui.beneficiary_detail.BeneficiaryDetailFragment
//import org.mifos.mobile.ui.fragments.BeneficiaryListFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment


@AndroidEntryPoint
class BeneficiaryListsFragment : BaseFragment() {

    private val viewModel: BeneficiaryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadBeneficiaries()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            MifosMobileTheme {
                BeneficiaryListScreen(
                    navigateBack = { activity?.supportFragmentManager?.popBackStack() },
                    addBeneficiaryClicked = { addBeneficiary() },
                    onBeneficiaryItemClick = { position, beneficiaryList ->
                        if (beneficiaryList != null) {
                            onItemClick(
                                position = position,
                                beneficiaryList = beneficiaryList
                            )
                        }
                    },
                    retryLoadingBeneficiary = { loadBeneficiary() },
                )
            }
        }
    }

    private fun onItemClick(position: Int, beneficiaryList: List<Beneficiary?>) {
        (activity as BaseActivity?)?.replaceFragment(
            BeneficiaryDetailFragment.newInstance(
                beneficiaryList[position],
            ),
            true,
            R.id.container,
        )
    }

    private fun addBeneficiary() {
        (activity as BaseActivity?)?.replaceFragment(
            BeneficiaryAddOptionsFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    private fun loadBeneficiary() {
        viewModel.loadBeneficiaries()
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
        viewModel.loadBeneficiaries()
    }

    companion object {
        fun newInstance(): BeneficiaryListsFragment {
            return BeneficiaryListsFragment()
        }
    }
}