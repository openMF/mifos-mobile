package org.mifos.mobile.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
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

    @kotlin.jvm.JvmField
    @BindView(R.id.view_scanner)
    var mScannerView: ZXingScannerView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.btn_flash)
    var btnFlash: ImageButton? = null

    private var flashOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_scan_qr_code, container, false)
        setToolbarTitle(getString(R.string.add_beneficiary))
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView)
        mScannerView?.setAutoFocus(true)
        return rootView
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

    @Suppress("DEPRECATION")
    @OnClick(R.id.btn_flash)
    fun turnOnFlash() {
        if (flashOn) {
            flashOn = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnFlash?.setImageDrawable(resources.getDrawable(R.drawable.ic_flash_on, null))
            } else {
                btnFlash?.setImageDrawable(resources.getDrawable(R.drawable.ic_flash_on))
            }
            mScannerView?.flash = false
        } else {
            flashOn = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnFlash?.setImageDrawable(resources.getDrawable(R.drawable.ic_flash_off, null))
            } else {
                btnFlash?.setImageDrawable(resources.getDrawable(R.drawable.ic_flash_off))
            }
            mScannerView?.flash = true
        }
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
            (activity as BaseActivity?)?.replaceFragment(
                BeneficiaryApplicationFragment.newInstance(
                    BeneficiaryState.CREATE_QR,
                    beneficiary
                ), true, R.id.container
            )
        } catch (e: JsonSyntaxException) {
            Toast.makeText(
                activity, getString(R.string.invalid_qr),
                Toast.LENGTH_SHORT
            ).show()
            mScannerView?.resumeCameraPreview(this)
        }
    }

    companion object {
        fun newInstance(): QrCodeReaderFragment {
            return QrCodeReaderFragment()
        }
    }
}