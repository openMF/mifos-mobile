package org.mifos.mobilebanking.models.beneficary

import com.google.gson.annotations.SerializedName

/**
 * Created by dilpreet on 16/6/17.
 */

data class BeneficiaryUpdatePayload @JvmOverloads constructor(

    @SerializedName("name") var name: String? = null,

    @SerializedName("transferLimit") var transferLimit: Int = 0)
