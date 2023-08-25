package org.mifos.mobile.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mifos.mobile.R
import org.mifos.mobile.models.AboutUsItem
import org.mifos.mobile.ui.enums.AboutUsListItemId
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor() : ViewModel() {

    private val _aboutUsItemEvent = MutableLiveData<AboutUsListItemId>()
    val aboutUsItemEvent: LiveData<AboutUsListItemId> get() = _aboutUsItemEvent

    val aboutUsItems: List<AboutUsItem> = listOf(
        AboutUsItem(R.string.app_version_text, null, AboutUsListItemId.APP_VERSION_TEXT),
        AboutUsItem(
            R.string.official_website,
            R.drawable.ic_website,
            AboutUsListItemId.OFFICE_WEBSITE
        ),
        AboutUsItem(R.string.licenses, R.drawable.ic_law_icon, AboutUsListItemId.LICENSES),
        AboutUsItem(
            R.string.privacy_policy,
            R.drawable.ic_privacy_policy,
            AboutUsListItemId.PRIVACY_POLICY
        ),
        AboutUsItem(R.string.sources, R.drawable.ic_source_code, AboutUsListItemId.SOURCE_CODE),
        AboutUsItem(
            R.string.license_string_with_value,
            null,
            AboutUsListItemId.LICENSES_STRING_WITH_VALUE
        )
    )

    fun navigateToItem(itemId: AboutUsListItemId) {
        _aboutUsItemEvent.value = itemId
    }

}