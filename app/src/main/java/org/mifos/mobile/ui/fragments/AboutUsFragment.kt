package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

import org.mifos.mobile.BuildConfig
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentAboutUsBinding
import org.mifos.mobile.ui.activities.PrivacyPolicyActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import java.util.*

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
class AboutUsFragment : BaseFragment() {
    private var _binding : FragmentAboutUsBinding? = null
    private val binding get() = _binding!!

    private val licenseLink = "https://github.com/openMF/mifos-mobile/blob/development/LICENSE.md"
    private val sourceCodeLink = "https://github.com/openMF/mifos-mobile"
    private val websiteLink = "https://openmf.github.io/mobileapps.github.io/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val rootView = binding.root
        setToolbarTitle(getString(R.string.about_us))
        binding.appVersion.text = BuildConfig.VERSION_NAME
        binding.tvCopyRight.text = getString(R.string.copy_right_mifos, Calendar.getInstance()[Calendar.YEAR].toString())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aboutWebsiteContainer.setOnClickListener {
            showWebsite()
        }

        binding.aboutLicensesContainer.setOnClickListener {
            showOpenSourceLicenses()
        }

        binding.aboutPrivacyPolicyContainer.setOnClickListener {
            showPrivacyPolicy()
        }

        binding.aboutSourcesContainer.setOnClickListener {
            showSourceCode()
        }

        binding.selfLicenseContainer.setOnClickListener {
            showSelfLicense()
        }
    }

    private fun showWebsite(){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink)))
    }

    private fun showOpenSourceLicenses() {
        startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
    }

    private fun showPrivacyPolicy() {
        startActivity(Intent(activity, PrivacyPolicyActivity::class.java))
    }

    private fun showSourceCode() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(sourceCodeLink)))
    }

    private fun showSelfLicense() {
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}