package org.mifos.mobile.core.model.entity.templates.account

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class AccountType(

    var id: Int? = null,

    var code: String? = null,

    var value: String? = null,
) : Parcelable
