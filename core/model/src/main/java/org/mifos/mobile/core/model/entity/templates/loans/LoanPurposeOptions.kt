package org.mifos.mobile.core.model.entity.templates.loans

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class LoanPurposeOptions(

    var id: Int? = null,

    var name: String? = null,

    var position: Int? = null,

    var description: String? = null,

    @SerializedName("isActive")
    var active: Boolean? = null,

) : Parcelable
