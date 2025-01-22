/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.qr

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
class OrientationListener(
    val orientationChanged: (UIDeviceOrientation) -> Unit,
) : NSObject() {

    private val notificationName = platform.UIKit.UIDeviceOrientationDidChangeNotification

    @OptIn(BetaInteropApi::class)
    @Suppress("UNUSED_PARAMETER")
    @ObjCAction
    fun orientationDidChange(arg: NSNotification) {
        orientationChanged(UIDevice.currentDevice.orientation)
    }

    fun register() {
        NSNotificationCenter.defaultCenter.addObserver(
            observer = this,
            selector = NSSelectorFromString(
                OrientationListener::orientationDidChange.name + ":",
            ),
            name = notificationName,
            `object` = null,
        )
    }

    fun unregister() {
        NSNotificationCenter.defaultCenter.removeObserver(
            observer = this,
            name = notificationName,
            `object` = null,
        )
    }
}
