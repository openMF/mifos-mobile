/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.savings

import android.os.Parcel
import android.os.Parcelable

data class TimeLine(
    var submittedOnDate: List<Int> = ArrayList(),

    var submittedByUsername: String?,

    var submittedByFirstname: String?,

    var submittedByLastname: String?,

    var approvedOnDate: List<Int> = ArrayList(),

    var approvedByUsername: String?,

    var approvedByFirstname: String?,

    var approvedByLastname: String?,

    var activatedOnDate: List<Int>? = null,

    var activatedByUsername: String?,

    var activatedByFirstname: String?,

    var activatedByLastname: String?,

    var closedOnDate: List<Int>,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        arrayListOf<Int>().apply {
            parcel.readArrayList(Int::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        arrayListOf<Int>().apply {
            parcel.readArrayList(Int::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        arrayListOf<Int>().apply {
            parcel.readArrayList(Int::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        arrayListOf<Int>().apply {
            parcel.readArrayList(Int::class.java.classLoader)
        },
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(submittedOnDate)
        parcel.writeString(submittedByUsername)
        parcel.writeString(submittedByFirstname)
        parcel.writeString(submittedByLastname)
        parcel.writeList(approvedOnDate)
        parcel.writeString(approvedByUsername)
        parcel.writeString(approvedByFirstname)
        parcel.writeString(approvedByLastname)
        parcel.writeList(activatedOnDate)
        parcel.writeString(activatedByUsername)
        parcel.writeString(activatedByFirstname)
        parcel.writeString(activatedByLastname)
        parcel.writeList(closedOnDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<TimeLine> = object : Parcelable.Creator<TimeLine> {
            override fun createFromParcel(parcel: Parcel): TimeLine {
                return TimeLine(parcel)
            }

            override fun newArray(size: Int): Array<TimeLine?> {
                return arrayOfNulls(size)
            }
        }
    }
}
