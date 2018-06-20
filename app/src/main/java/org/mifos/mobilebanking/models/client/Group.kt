package org.mifos.mobilebanking.models.client

import com.google.gson.annotations.SerializedName

/**
 * Created by dilpreet on 10/7/17.
 */

data class Group (

    @SerializedName("id") var id: Int,

    @SerializedName("accountNo") var accountNo: String,

    @SerializedName("name") var name: String
)
