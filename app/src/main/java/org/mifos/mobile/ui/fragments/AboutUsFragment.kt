package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.net.Uri
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
*/
class AboutUsFragment : BaseFragment() {
    private val licenseLink = "https://github.com/openMF/mifos-mobile/blob/development/LICENSE.md"
    private val sourceCodeLink = "https://github.com/openMF/mifos-mobile"
    private val websiteLink = "https://openmf.github.io/mobileapps.github.io/"

    @kotlin.jvm.JvmField
    @BindView(R.id.app_version)
    var tvAppVersion: TextView? = null


    @kotlin.jvm.JvmField
    @BindView(R.id.tv_copy_right)
    var tvCopyRight: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_about_us, container, false)
        ButterKnife.bind(this, rootView!!)
        setToolbarTitle(getString(R.string.about_us))
        tvAppVersion?.text = BuildConfig.VERSION_NAME
        tvCopyRight?.text = getString(R.string.copy_right_mifos, Calendar.getInstance()[Calendar.YEAR].toString())
        return rootView
    }

    @OnClick(R.id.about_website_container)
    fun showWebsite(){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink)))
    }

    @OnClick(R.id.about_licenses_container)
    fun showOpenSourceLicenses() {
        startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
    }

    @OnClick(R.id.about_privacy_policy_container)
    fun showPrivacyPolicy() {
        startActivity(Intent(activity, PrivacyPolicyActivity::class.java))
    }

    @OnClick(R.id.about_sources_container)
    fun showSourceCode() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(sourceCodeLink)))
    }

    @OnClick(R.id.self_license_container)
    fun showSelfLicense() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(licenseLink)))
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