package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import org.mifos.mobile.R
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.QrCodeGenerator
import org.mifos.mobile.utils.Utils

/**
 * Created by dilpreet on 16/8/17.
 */
class QrCodeDisplayFragment : BaseFragment() {
    @kotlin.jvm.JvmField
    @BindView(R.id.iv_qr_code)
    var ivQrCode: ImageView? = null
    private var rootView: View? = null
    private var json: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            json = arguments!!.getString(Constants.QR_DATA)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_qr_code_display, container, false)
        ButterKnife.bind(this, rootView!!)
        ivQrCode!!.setImageBitmap(QrCodeGenerator.encodeAsBitmap(json))
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_qr_code_display, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_qr_code_share -> {
                val bitmapDrawable = ivQrCode!!.drawable as BitmapDrawable
                val uri = Utils.getImageUri(activity, bitmapDrawable.bitmap)
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(intent, getString(R.string.choose_option)))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(json: String?): QrCodeDisplayFragment {
            val fragment = QrCodeDisplayFragment()
            val args = Bundle()
            args.putString(Constants.QR_DATA, json)
            fragment.arguments = args
            return fragment
        }
    }
}