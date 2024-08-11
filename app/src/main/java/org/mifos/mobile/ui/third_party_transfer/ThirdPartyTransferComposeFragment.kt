package org.mifos.mobile.ui.third_party_transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.beneficiary.presentation.BeneficiaryAddOptionsFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.transfer_process.TransferProcessComposeFragment
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.common.utils.getTodayFormatted
import org.mifos.mobile.feature.third.party.transfer.third_party_transfer.ThirdPartyTransferPayload
import org.mifos.mobile.feature.third.party.transfer.third_party_transfer.ThirdPartyTransferScreen

@AndroidEntryPoint
class ThirdPartyTransferComposeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as? BaseActivity)?.hideToolbar()
        return mifosComposeView(requireContext()) {

        }
    }

    private fun reviewTransfer(thirdPartyTransferPayload: ThirdPartyTransferPayload) {
        val transferPayload = TransferPayload().apply {
            fromAccountId = thirdPartyTransferPayload.payFromAccount.accountId
            fromClientId = thirdPartyTransferPayload.payFromAccount.clientId
            fromAccountNumber = thirdPartyTransferPayload.payFromAccount.accountNo
            fromAccountType = thirdPartyTransferPayload.payFromAccount.accountType?.id
            fromOfficeId = thirdPartyTransferPayload.payFromAccount.officeId
            toOfficeId = thirdPartyTransferPayload.beneficiaryAccount.officeId
            toAccountId = thirdPartyTransferPayload.beneficiaryAccount.accountId
            toClientId = thirdPartyTransferPayload.beneficiaryAccount.clientId
            toAccountNumber = thirdPartyTransferPayload.beneficiaryAccount.accountNo
            toAccountType = thirdPartyTransferPayload.beneficiaryAccount.accountType?.id
            transferDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, getTodayFormatted())
            transferAmount = thirdPartyTransferPayload.transferAmount
            transferDescription = thirdPartyTransferPayload.transferRemark
        }

        (activity as? BaseActivity)?.replaceFragment(
            TransferProcessComposeFragment.newInstance(transferPayload, org.mifos.mobile.core.model.enums.TransferType.TPT,),
            true, R.id.container
        )
    }


    private fun addBeneficiary() {
        (activity as BaseActivity?)?.replaceFragment(
            BeneficiaryAddOptionsFragment.newInstance(),
            true,
            R.id.container,
        )
    }


    companion object {
        fun newInstance(): ThirdPartyTransferComposeFragment {
            return ThirdPartyTransferComposeFragment()
        }
    }
}
