package org.mifos.mobile.models.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
        val payee: TransactingEntity,
        val payer: TransactingEntity,
        val amount: Amount?) : Parcelable