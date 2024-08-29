/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.loan

import android.os.Parcel
import android.os.Parcelable
import org.mifos.mobile.core.model.entity.accounts.Account

data class LoanAccount(
    var loanProductId: Long = 0,

    var externalId: String? = null,

    var numberOfRepayments: Long = 0,

    var accountNo: String? = null,

    var productName: String? = null,

    var productId: Int? = null,

    var loanProductName: String? = null,

    var clientName: String? = null,

    var loanProductDescription: String? = null,

    var principal: Double = 0.toDouble(),

    var annualInterestRate: Double = 0.toDouble(),

    var status: Status? = null,

    var loanType: LoanType? = null,

    var loanCycle: Int? = null,

    var loanBalance: Double = 0.toDouble(),

    var amountPaid: Double = 0.toDouble(),

    var currency: Currency?,

    var inArrears: Boolean? = null,

    var summary: Summary? = null,

    var loanPurposeName: String? = null,

    var timeline: Timeline?,

) : Account(), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readParcelable(Status::class.java.classLoader),
        parcel.readParcelable(LoanType::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readParcelable(Currency::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readParcelable(Summary::class.java.classLoader),
        parcel.readString(),
        parcel.readParcelable(Timeline::class.java.classLoader),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(loanProductId)
        parcel.writeString(externalId)
        parcel.writeLong(numberOfRepayments)
        parcel.writeString(accountNo)
        parcel.writeString(productName)
        parcel.writeValue(productId)
        parcel.writeString(loanProductName)
        parcel.writeString(clientName)
        parcel.writeString(loanProductDescription)
        parcel.writeDouble(principal)
        parcel.writeDouble(annualInterestRate)
        parcel.writeParcelable(status, flags)
        parcel.writeParcelable(loanType, flags)
        parcel.writeValue(loanCycle)
        parcel.writeDouble(loanBalance)
        parcel.writeDouble(amountPaid)
        parcel.writeParcelable(currency, flags)
        parcel.writeValue(inArrears)
        parcel.writeParcelable(summary, flags)
        parcel.writeString(loanPurposeName)
        parcel.writeParcelable(timeline, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<LoanAccount> = object : Parcelable.Creator<LoanAccount> {

            override fun createFromParcel(parcel: Parcel): LoanAccount {
                return LoanAccount(parcel)
            }

            override fun newArray(size: Int): Array<LoanAccount?> {
                return arrayOfNulls(size)
            }
        }
    }
}
