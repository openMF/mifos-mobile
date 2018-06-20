package org.mifos.mobilebanking.models.register

import com.google.gson.annotations.SerializedName

/**
 * Created by dilpreet on 31/7/17.
 */

data class UserVerify (

    @SerializedName("requestId") var requestId: String? = null,

    @SerializedName("authenticationToken") var authenticationToken: String? = null
)
