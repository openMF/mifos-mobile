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

actual interface Parcelable
actual annotation class IgnoredOnParcel
actual annotation class Parcelize
actual interface Parceler<P> {
    actual fun create(parcel: Parcel): P
    actual fun P.write(parcel: Parcel, flags: Int)
}

actual annotation class TypeParceler<T, P : Parceler<in T>>

actual class Parcel {
    actual fun readString(): String? = null
    actual fun readByte(): Byte = 1

    actual fun readInt(): Int = 1

    actual fun readFloat(): Float = 1f

    actual fun readDouble(): Double = 1.0

    actual fun writeByte(value: Byte) {
    }

    actual fun writeInt(value: Int) {
    }

    actual fun writeFloat(value: Float) {
    }

    actual fun writeDouble(value: Double) {
    }

    actual fun writeString(value: String?) {
    }
}
