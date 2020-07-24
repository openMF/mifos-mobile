package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.Result

import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler

import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.BeneficiaryState
import org.mifos.mobile.ui.fragments.base.BaseFragment

/**
 * Created by dilpreet on 6/7/17.
 */
class QrCodeReaderFragment : BaseFragment(), ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mScannerView = ZXingScannerView(activity)
        mScannerView?.setAutoFocus(true)
        return mScannerView
    }

    /**
     * Sets the [me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler] callback and
     * opens Camera
     */
    override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this)
        mScannerView?.startCamera()
    }

    /**
     * Closes the Camera
     */
    override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()
    }

    /**
     * Callback for [ZXingScannerView] which retrieves data from QRCode
     *
     * @param result Contains data scanned from QRCode
     */
    override fun handleResult(result: Result) {
        val gson = Gson()
        try {
            val beneficiary = gson.fromJson(result.text, Beneficiary::class.java)
            activity?.supportFragmentManager?.popBackStack()
            (activity as BaseActivity?)?.replaceFragment(BeneficiaryApplicationFragment.newInstance(BeneficiaryState.CREATE_QR, beneficiary), true, R.id.container)
        } catch (e: JsonSyntaxException) {
            Toast.makeText(activity, getString(R.string.invalid_qr),
                    Toast.LENGTH_SHORT).show()
            mScannerView?.resumeCameraPreview(this)
        }
    }

    companion object {
        fun newInstance(): QrCodeReaderFragment {
            return QrCodeReaderFragment()
        }
    }
}