package org.mifos.mobilebanking.models.guarantor

/*
 * Created by saksham on 23/July/2018
 */

import java.util.ArrayList

data class GuarantorTemplatePayload(

        var guarantorType: GuarantorType? = null,
        var guarantorTypeOptions: ArrayList<GuarantorType>? = null
)
