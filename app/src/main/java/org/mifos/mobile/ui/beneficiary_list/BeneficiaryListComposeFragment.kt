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
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryRoute
import org.mifos.mobile.navigation.RootNavGraph

@AndroidEntryPoint
class BeneficiaryListComposeFragment : BaseFragment() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
                val navController = rememberNavController()
                RootNavGraph(
                    startDestination = BeneficiaryRoute.BENEFICIARY_NAVIGATION_ROUTE,
                    navController = navController,
                    nestedStartDestination = BeneficiaryNavigation.BeneficiaryList.route,
                )

//            BeneficiaryListScreen(
//                navigateBack = { activity?.supportFragmentManager?.popBackStack() },
//                addBeneficiaryClicked = { addBeneficiary() },
//                onBeneficiaryItemClick = { position, beneficiaryList ->
//                    onItemClick(
//                        position = position,
//                        beneficiaryList = beneficiaryList
//                    )
//                },
//            )
        }
    }

//    private fun onItemClick(position: Int, beneficiaryList: List<Beneficiary>) {
//        (activity as? BaseActivity)?.replaceFragment(
//            org.mifos.mobile.ui.beneficiary_detail.BeneficiaryDetailFragment.newInstance(beneficiaryList[position]),
//            true,
//            R.id.container,
//        )
//    }
//
//    private fun addBeneficiary() {
//        (activity as? BaseActivity)?.replaceFragment(
//            BeneficiaryAddOptionsFragment.newInstance(),
//            true,
//            R.id.container,
//        )
//    }

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