/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.common

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

actual typealias Parcelize = Parcelize

actual typealias Parcelable = Parcelable

actual typealias IgnoredOnParcel = IgnoredOnParcel

actual typealias Parceler<P> = Parceler<P>

actual typealias TypeParceler<T, P> = TypeParceler<T, P>

actual typealias Parcel = Parcel
