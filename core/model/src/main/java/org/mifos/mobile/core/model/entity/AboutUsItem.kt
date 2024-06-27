package org.mifos.mobile.core.model.entity

import org.mifos.mobile.core.model.enums.AboutUsListItemId

data class AboutUsItem(
    val title: String?,
    val subtitle: Int? = null,
    val iconUrl: Int? = null,
    val itemId: AboutUsListItemId
)
