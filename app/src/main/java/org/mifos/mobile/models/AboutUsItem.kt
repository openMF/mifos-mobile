package org.mifos.mobile.models

import org.mifos.mobile.ui.enums.AboutUsListItemId

data class AboutUsItem(
    val name: Int,
    val iconUrl: Int? = null,
    val itemId: AboutUsListItemId
)
