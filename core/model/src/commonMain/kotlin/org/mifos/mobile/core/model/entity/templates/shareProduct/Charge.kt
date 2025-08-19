package org.mifos.mobile.core.model.entity.templates.shareProduct

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class Charge(
    val id: Int? = null,
    val name: String? = null,
    val active: Boolean? = null,
    val penalty: Boolean? = null,
    val currency: Currency? = null,
    val amount: Int? = null,
    val chargeTimeType: ChargeType? = null,
    val chargeAppliesTo: ChargeType? = null,
    val chargeCalculationType: ChargeType? = null,
    val chargePaymentMode: ChargeType? = null
) : Parcelable
