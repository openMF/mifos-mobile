/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-wallet/blob/master/LICENSE.md
 */
package org.mifos.mifospay.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import org.mifos.mifospay.lint.designsystem.DesignSystemDetector
import org.mifos.mifospay.lint.designsystem.Material2Detector

class MifosIssueRegistry : IssueRegistry() {

    override val issues = listOf(
        DesignSystemDetector.ISSUE,
        Material2Detector.ISSUE,
        TestMethodNameDetector.FORMAT,
        TestMethodNameDetector.PREFIX,
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "Mifos Mobile",
        feedbackUrl = "https://github.com/openMF/mobile-mobile/issues",
        contact = "https://github.com/openMF/mobile-mobile",
    )
}
