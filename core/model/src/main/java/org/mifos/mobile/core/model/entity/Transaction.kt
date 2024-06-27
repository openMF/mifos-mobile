package org.mifos.mobile.core.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.client.Type

/**
 * @author Vishwajeet
 * @since 10/8/16.
 */

@Parcelize
data class Transaction(

    var id: Long? = null,

    var officeId: Long? = null,

    var officeName: String? = null,

    var type: Type,

    var date: List<Int> = ArrayList(),

    var currency: Currency? = null,

    var amount: Double? = null,

    var submittedOnDate: List<Int> = ArrayList(),

    var reversed: Boolean? = null,

    ) : Parcelable
