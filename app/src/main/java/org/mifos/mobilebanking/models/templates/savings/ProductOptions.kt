package org.mifos.mobilebanking.models.templates.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * Created by saksham on 01/July/2018
 */

@Parcelize
data class ProductOptions(
        var id: Int? = null,
        var name: String? = null
) : Parcelable
