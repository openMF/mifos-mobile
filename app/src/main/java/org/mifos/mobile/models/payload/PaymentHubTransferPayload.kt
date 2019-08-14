package org.mifos.mobile.models.payload

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.models.paymentHub.Amount
import org.mifos.mobile.models.paymentHub.PaymentHubTransactionType
import org.mifos.mobile.models.paymentHub.TransactingEntity

@Parcelize
data class PaymentHubTransferPayload(
        @SerializedName("clientRefId")
        val clientRefId: String? = null,

        @SerializedName("payee")
        val payee: TransactingEntity,

        @SerializedName("payer")
        val payer: TransactingEntity,

        @SerializedName("amountType")
        val amountType: String,

        @SerializedName("amount")
        val amount: Amount,

        @SerializedName("transactionType")
        val transactionType: PaymentHubTransactionType,

        @SerializedName("note")
        val note: String
): Parcelable