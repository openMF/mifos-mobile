package org.mifos.mobilebanking.models.templates.beneficiary

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by dilpreet on 14/6/17.
 */

@Parcelize
data class BeneficiaryTemplate(
        @SerializedName("accountTypeOptions")
        var accountTypeOptions: List<AccountTypeOption>? = null
) : Parcelable