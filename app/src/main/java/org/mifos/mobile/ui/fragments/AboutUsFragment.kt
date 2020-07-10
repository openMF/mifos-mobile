package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.mifos.mobile.BuildConfig
import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.PrivacyPolicyActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import java.util.*

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/   class AboutUsFragment : BaseFragment() {
    @kotlin.jvm.JvmField
    @BindView(R.id.tv_app_version)
    var tvAppVersion: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tv_copy_right)
    var tvCopyRight: TextView? = null
    var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_about_us, container, false)
        ButterKnife.bind(this, rootView!!)
        setToolbarTitle(getString(R.string.about_us))
        tvAppVersion!!.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)
        tvCopyRight!!.text = getString(R.string.copy_right_mifos, Calendar.getInstance()[Calendar.YEAR].toString())
        return rootView
    }

    @OnClick(R.id.tv_licenses)
    fun showOpenSourceLicenses() {
        startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
    }

    @OnClick(R.id.tv_privacy_policy)
    fun showPrivacyPolicy() {
        startActivity(Intent(activity, PrivacyPolicyActivity::class.java))
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun newInstance(): AboutUsFragment {
            val fragment = AboutUsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}