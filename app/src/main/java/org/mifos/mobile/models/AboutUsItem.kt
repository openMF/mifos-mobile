package org.mifos.mobile.models

import org.mifos.mobile.ui.enums.AboutUsListItemId

data class AboutUsItem(
    val title: String?,
    val subtitle: Int? = null,
    val iconUrl: Int? = null,
    val itemId: AboutUsListItemId
)
