package org.mifos.mobile.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mifos.mobile.MifosSelfServiceApp.Companion.context
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.AboutUsItem
import org.mifos.mobile.core.model.enums.AboutUsListItemId
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

    val aboutUsItems =  listOf(
        AboutUsItem(
            title = context?.getString(R.string.app_version_text),
            itemId = AboutUsListItemId.APP_VERSION_TEXT
        ),
        AboutUsItem(
            title = context?.getString(R.string.official_website),
            iconUrl = R.drawable.ic_website,
            itemId = AboutUsListItemId.OFFICE_WEBSITE
        ),
        AboutUsItem(
            title = context?.getString(R.string.licenses),
            iconUrl = R.drawable.ic_law_icon,
            itemId = AboutUsListItemId.LICENSES
        ),
        AboutUsItem(
            title = context?.getString(R.string.privacy_policy),
            iconUrl = R.drawable.ic_privacy_policy,
            itemId = AboutUsListItemId.PRIVACY_POLICY
        ),
        AboutUsItem(
            title = context?.getString(R.string.sources),
            iconUrl = R.drawable.ic_source_code,
            itemId = AboutUsListItemId.SOURCE_CODE
        ),
        AboutUsItem(
            title = copyrightText,
            subtitle = R.string.license_string_with_value,
            itemId = AboutUsListItemId.LICENSES_STRING_WITH_VALUE
        )
    )

    fun navigateToItem(itemId: AboutUsListItemId) {
        _aboutUsItemEvent.value = itemId
    }

}