package org.mifos.mobile.core.model.entity.templates.loans

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class TaxGroup(
    var id: Int? = null,

    var name: String? = null,
) : Parcelable
