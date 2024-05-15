package org.mifos.mobile.models.guarantor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * Created by saksham on 23/July/2018
 */

@Parcelize
class GuarantorType(
    var id: Long? = null,
    var value: String? = null,
    var code: String? = null,
): Parcelable
