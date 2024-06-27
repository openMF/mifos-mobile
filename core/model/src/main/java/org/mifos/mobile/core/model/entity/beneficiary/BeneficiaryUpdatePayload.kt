package org.mifos.mobile.core.model.entity.beneficiary

/**
 * Created by dilpreet on 16/6/17.
 */

data class BeneficiaryUpdatePayload @JvmOverloads constructor(
    var name: String? = null,
    var transferLimit: Float = 0f,
)
