package org.mifos.mobile.ui.qrCodeReader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.Result
import dagger.hilt.android.AndroidEntryPoint
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.BeneficiaryState
import org.mifos.mobile.ui.fragments.BeneficiaryApplicationFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment

@AndroidEntryPoint
class QrCodeReaderFragment : BaseFragment(), ResultHandler {

    private lateinit var scannerView: ZXingScannerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        scannerView = ZXingScannerView(requireContext()).apply {
            setResultHandler(this@QrCodeReaderFragment)
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    QrCodeReaderScreen(
                        scannerView = scannerView,
                        onBackPressed = {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    )
                }
            }
        }
    }

    override fun handleResult(result: Result?) {
        val gson = Gson()
        try {
            val beneficiary = gson.fromJson(result?.text, Beneficiary::class.java)
            activity?.supportFragmentManager?.popBackStack()
            (activity as BaseActivity?)?.replaceFragment(
                BeneficiaryApplicationFragment.newInstance(
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
            scannerView.resumeCameraPreview(this)
        }
    }

    override fun onPause() {
        super.onPause()
        (activity as? BaseActivity)?.showToolbar()
        scannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    companion object {
        fun newInstance(): QrCodeReaderFragment {
            return QrCodeReaderFragment()
        }
    }
}
