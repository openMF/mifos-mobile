package org.mifos.mobile.ui.qr_code_display

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentQrCodeDisplayBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.QrCodeGenerator
import org.mifos.mobile.utils.Utils

/**
 * Created by dilpreet on 16/8/17.
 */
@AndroidEntryPoint
class QrCodeDisplayFragment : BaseFragment() {

    private var _binding: FragmentQrCodeDisplayBinding? = null
    private val binding get() = _binding!!

    private var json: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? BaseActivity)?.showToolbar()
        if (arguments != null) {
            json = arguments?.getString(Constants.QR_DATA)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQrCodeDisplayBinding.inflate(inflater, container, false)
        binding.ivQrCode.setImageBitmap(QrCodeGenerator.encodeAsBitmap(json))
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_qr_code_display, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_qr_code_share -> {
                val bitmapDrawable = binding.ivQrCode.drawable as BitmapDrawable
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
