package org.mifos.mobile.models.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionInfo(val transactionId: String) : Parcelable