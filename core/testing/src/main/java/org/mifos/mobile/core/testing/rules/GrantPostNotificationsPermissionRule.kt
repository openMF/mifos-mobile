/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.rules

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.test.rule.GrantPermissionRule.grant
import org.junit.rules.TestRule

/**
 * [TestRule] granting [POST_NOTIFICATIONS] permission if running on [SDK_INT] greater than [TIRAMISU].
 */
class GrantPostNotificationsPermissionRule :
    TestRule by if (SDK_INT >= TIRAMISU) grant(POST_NOTIFICATIONS) else grant()
