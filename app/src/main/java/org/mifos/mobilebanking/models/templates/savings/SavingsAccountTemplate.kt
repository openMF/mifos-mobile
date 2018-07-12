package org.mifos.mobilebanking.models.templates.savings

/*
 * Created by saksham on 01/July/2018
 */

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
data class SavingsAccountTemplate(
        var clientId: Int = 0,
        var clientName: String? = null,
        var withdrawalFeeForTransfers: Boolean? = null,
        var allowOverdraft: Boolean? = null,
        var enforceMinRequiredBalance: Boolean? = null,
        var withHoldTax: Boolean? = null,
        var isDormancyTrackingActive: Boolean? = null,
        var productOptions: ArrayList<ProductOptions> = ArrayList<ProductOptions>(),
        var chargeOptions: ArrayList<ChargeOptions> = ArrayList<ChargeOptions>()
) : Parcelable

