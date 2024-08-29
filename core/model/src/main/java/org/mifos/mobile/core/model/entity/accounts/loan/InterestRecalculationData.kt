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

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.calendardata.CalendarData

@Parcelize
data class InterestRecalculationData(
    var id: Int? = null,

    var loanId: Int? = null,

    var interestRecalculationCompoundingType: InterestRecalculationCompoundingType? = null,

    var rescheduleStrategyType: RescheduleStrategyType? = null,

    var calendarData: CalendarData,

    var recalculationRestFrequencyType: RecalculationRestFrequencyType? = null,

    var recalculationRestFrequencyInterval: Double? = null,

    var recalculationCompoundingFrequencyType: RecalculationCompoundingFrequencyType? = null,

    @SerializedName("isCompoundingToBePostedAsTransaction")
    var compoundingToBePostedAsTransaction: Boolean? = null,

    var allowCompoundingOnEod: Boolean? = null,

) : Parcelable
