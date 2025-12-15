/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.component

/**
 * Defines the supported ways to provide a signature.
 */
enum class SignatureUploadType {

    /** Draw a signature within the app. */
    SIGN,

    /** Capture a signature using the camera. */
    CAPTURE,

    /** Select a signature image from the gallery. */
    GALLERY,
}
