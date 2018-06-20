package org.mifos.mobilebanking.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * Created by ishankhanna for mifos android-client on 09/02/14.
 * Created by michaelsosnick on 1/20/17.
 */

/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

@Parcelize
data class Timeline(
        var submittedOnDate: List<Int> = ArrayList(),
        var submittedByUsername: String? = null,
        var submittedByFirstname: String? = null,
        var submittedByLastname: String? = null,
        var activatedOnDate: List<Int> = ArrayList(),
        var activatedByUsername: String? = null,
        var activatedByFirstname: String? = null,
        var activatedByLastname: String? = null,
        var closedOnDate: List<Int> = ArrayList(),
        var closedByUsername: String? = null,
        var closedByFirstname: String? = null,
        var closedByLastname: String? = null
) : Parcelable