/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.util

/**
 * Thrown when a network operation is attempted without a stable, validated
 * internet connection.
 *
 * @param message Human-readable reason shown in logs and crash reports.
 * @param cause   Optional underlying throwable.
 */
class NetworkUnavailableException(
    message: String = "No stable network connection available",
    cause: Throwable? = null,
) : IllegalStateException(message, cause)
