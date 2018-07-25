package org.mifos.mobilebanking.models.guarantor

/*
 * Created by saksham on 23/July/2018
 */

import com.google.gson.annotations.SerializedName

data class GuarantorApplicationPayload(

        var guarantorType: GuarantorType,

        @SerializedName("firstname")
        var firstName: String,

        @SerializedName("lastname")
        var lastName: String,

        var officeName: String
)