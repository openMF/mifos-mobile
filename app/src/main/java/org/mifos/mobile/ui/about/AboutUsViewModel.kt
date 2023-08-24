package org.mifos.mobile.ui.about

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mifos.mobile.R
import org.mifos.mobile.models.AboutUsItem
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor() : ViewModel() {

    val aboutUsList: List<AboutUsItem> = listOf(
        AboutUsItem(R.string.app_version_text, null),
        AboutUsItem(R.string.official_website, R.drawable.ic_website),
        AboutUsItem(R.string.licenses, R.drawable.ic_law_icon),
        AboutUsItem(R.string.privacy_policy, R.drawable.ic_privacy_policy),
        AboutUsItem(R.string.sources, R.drawable.ic_source_code),
        AboutUsItem(R.string.license_string_with_value, null)
    )

}