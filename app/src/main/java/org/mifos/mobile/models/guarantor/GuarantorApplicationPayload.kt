package org.mifos.mobile.models.guarantor

import com.google.gson.annotations.SerializedName

/*
 * Created by saksham on 23/July/2018
 */

data class GuarantorApplicationPayload(

    @SerializedName("guarantorTypeId")
    var guarantorTypeId: Long?,

    @SerializedName("firstname")
    var firstName: String?,

    @SerializedName("lastname")
    var lastName: String?,

    @SerializedName("city")
    var city: String? = ""
)
