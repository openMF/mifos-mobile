/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    var closedByLastname: String? = null,
) : Parcelable
