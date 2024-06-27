package org.mifos.mobile.ui.about

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.PrivacyPolicyActivity
import org.mifos.mobile.core.model.enums.AboutUsListItemId
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants.LICENSE_LINK
import org.mifos.mobile.core.common.Constants.SOURCE_CODE_LINK
import org.mifos.mobile.core.common.Constants.WEBSITE_LINK

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
@AndroidEntryPoint
class AboutUsFragment : BaseFragment() {

    private val viewModel: AboutUsViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    AboutUsScreen(viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.aboutUsItemEvent.observe(viewLifecycleOwner) {
            when (it) {
                AboutUsListItemId.OFFICE_WEBSITE -> {
                    startActivity(WEBSITE_LINK)
                }

                AboutUsListItemId.LICENSES -> {
                    startActivity(LICENSE_LINK)
                }

                AboutUsListItemId.PRIVACY_POLICY -> {
                    startActivity(PrivacyPolicyActivity::class.java)
                }

                AboutUsListItemId.SOURCE_CODE -> {
                    startActivity(SOURCE_CODE_LINK)
                }

                AboutUsListItemId.LICENSES_STRING_WITH_VALUE -> {
                    startActivity(OssLicensesMenuActivity::class.java)
                }

                else -> {}
            }
        }
    }

    fun startActivity(uri: String) {
        context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }

    fun <T : Activity> startActivity(clazz: Class<T>) {
        context?.startActivity(Intent(context, clazz))
    }

    companion object {
        fun newInstance(): AboutUsFragment {
            return AboutUsFragment()
        }
    }
}
