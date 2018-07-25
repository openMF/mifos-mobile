package org.mifos.mobilebanking.utils

import org.mifos.mobilebanking.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobilebanking.models.guarantor.GuarantorPayload

/*
 * Created by saksham on 29/July/2018
*/

class RxEvent {

    data class AddGuarantorEvent(var payload: GuarantorApplicationPayload, var index: Int)

    data class DeleteGuarantorEvent(var index: Int)

    data class UpdateGuarantorEvent(var payload: GuarantorApplicationPayload, var index: Int)
}
