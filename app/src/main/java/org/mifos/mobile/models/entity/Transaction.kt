package org.mifos.mobile.models.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
        @SerializedName("payee")
        val payee: TransactingEntity,
        @SerializedName("payer")
        val payer: TransactingEntity,
        @SerializedName("amount")
        val amount: Amount?): Parcelable