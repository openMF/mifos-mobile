package org.mifos.mobile.core.model.entity.guarantor

/*
 * Created by saksham on 23/July/2018
 */

data class GuarantorTemplatePayload(

    var guarantorType: GuarantorType? = null,
    var guarantorTypeOptions: ArrayList<GuarantorType>? = null,
)
