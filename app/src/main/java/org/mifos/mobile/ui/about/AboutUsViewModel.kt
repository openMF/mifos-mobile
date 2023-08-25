package org.mifos.mobile.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mifos.mobile.MifosSelfServiceApp.Companion.context
import org.mifos.mobile.R
import org.mifos.mobile.models.AboutUsItem
import org.mifos.mobile.ui.enums.AboutUsListItemId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor() : ViewModel() {

    private val _aboutUsItemEvent = MutableLiveData<AboutUsListItemId>()
    val aboutUsItemEvent: LiveData<AboutUsListItemId> get() = _aboutUsItemEvent

    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private val copyrightText =
        context?.getString(R.string.copy_right_mifos)
            ?.replace("%1\$s", currentYear.toString())

    val aboutUsItems: List<AboutUsItem> = listOf(
        AboutUsItem(
            context?.getString(R.string.app_version_text),
            null,
            null,
            AboutUsListItemId.APP_VERSION_TEXT
        ),
        AboutUsItem(
            context?.getString(R.string.official_website),
            null,
            R.drawable.ic_website,
            AboutUsListItemId.OFFICE_WEBSITE
        ),
        AboutUsItem(
            context?.getString(R.string.licenses),
            null,
            R.drawable.ic_law_icon,
            AboutUsListItemId.LICENSES
        ),
        AboutUsItem(
            context?.getString(R.string.privacy_policy),
            null,
            R.drawable.ic_privacy_policy,
            AboutUsListItemId.PRIVACY_POLICY
        ),
        AboutUsItem(
            context?.getString(R.string.sources),
            null,
            R.drawable.ic_source_code,
            AboutUsListItemId.SOURCE_CODE
        ),
        AboutUsItem(
            copyrightText,
            R.string.license_string_with_value,
            null,
            AboutUsListItemId.LICENSES_STRING_WITH_VALUE
        )
    )

    fun navigateToItem(itemId: AboutUsListItemId) {
        _aboutUsItemEvent.value = itemId
    }

}