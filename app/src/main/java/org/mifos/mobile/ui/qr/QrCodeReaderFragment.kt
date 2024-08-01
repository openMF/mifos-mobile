package org.mifos.mobile.ui.qr

/*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.qr.qr.QrCodeReaderScreen
import org.mifos.mobile.ui.beneficiary_application.BeneficiaryApplicationComposeFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment

/**
 * Created by dilpreet on 6/7/17.
 */
@AndroidEntryPoint
class QrCodeReaderFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as? BaseActivity)?.hideToolbar()
        return mifosComposeView(requireContext()) {
            QrCodeReaderScreen(
                qrScanned = { qrScanned(it) },
                navigateBack = { activity?.supportFragmentManager?.popBackStack() }
            )
        }
    }

    private fun qrScanned(text: String) {
        val gson = Gson()
        try {
            val beneficiary = gson.fromJson(text, Beneficiary::class.java)
            activity?.supportFragmentManager?.popBackStack()
            (activity as BaseActivity?)?.replaceFragment(
                BeneficiaryApplicationComposeFragment.newInstance(
                    BeneficiaryState.CREATE_QR,
                    beneficiary,
                ),
                true,
                R.id.container,
            )
        } catch (e: JsonSyntaxException) {
            Toast.makeText(
                activity,
                getString(R.string.invalid_qr),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    companion object {
        fun newInstance(): QrCodeReaderFragment {
            return QrCodeReaderFragment()
        }
    }
}
 */