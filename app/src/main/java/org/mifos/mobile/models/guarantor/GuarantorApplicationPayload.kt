package org.mifos.mobile.models.guarantor

/*
 * Created by saksham on 23/July/2018
 */

data class GuarantorApplicationPayload(

        var guarantorType: GuarantorType?,

        var firstName: String?,

        var lastName: String?,

        var officeName: String?
)