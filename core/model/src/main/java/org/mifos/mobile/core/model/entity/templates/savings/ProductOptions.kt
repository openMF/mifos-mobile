package org.mifos.mobile.core.model.entity.templates.savings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * Created by saksham on 01/July/2018
 */

@Parcelize
data class ProductOptions(
    var id: Int? = null,
    var name: String? = null,
) : Parcelable
