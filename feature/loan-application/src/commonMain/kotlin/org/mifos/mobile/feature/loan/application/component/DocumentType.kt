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
 * Represents the types of documents that can be uploaded as part of a loan application.
 */
enum class DocumentType {
    /**
     * A financial document showing account activity.
     */
    BANK_STATEMENT,

    /**
     * A legal document proving ownership of a property.
     */
    PROPERTY_DOCUMENT,

    /**
     * A handwritten depiction of a person's name for verification.
     */
    SIGNATURE,
}
