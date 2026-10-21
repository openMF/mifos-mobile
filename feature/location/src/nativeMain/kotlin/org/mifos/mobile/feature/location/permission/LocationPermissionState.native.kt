/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.location.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.darwin.NSObject

@Composable
actual fun rememberLocationPermissionState(): LocationPermissionState = remember {
    IosLocationPermissionState()
}

/**
 * CoreLocation reports the authorization result asynchronously through its delegate, so the current
 * value is read once at construction and then refreshed every time
 * [CLLocationManagerDelegateProtocol.locationManagerDidChangeAuthorization] fires. Keeping it in a
 * snapshot state means the shared request gate recomposes as soon as the user answers the prompt.
 */
private class IosLocationPermissionState : LocationPermissionState {

    private val locationManager = CLLocationManager()

    private var isAuthorized by mutableStateOf(readIsAuthorized())

    private val delegate = LocationAuthorizationDelegate {
        isAuthorized = readIsAuthorized()
    }

    init {
        locationManager.delegate = delegate
    }

    override val status: LocationPermissionStatus
        get() = if (isAuthorized) {
            LocationPermissionStatus.Granted
        } else {
            LocationPermissionStatus.Denied
        }

    override fun requestLocationPermission() {
        locationManager.requestWhenInUseAuthorization()
    }

    override fun goToSettings() {
        val appSettingsUrl = NSURL(string = UIApplicationOpenSettingsURLString)
        if (UIApplication.sharedApplication.canOpenURL(appSettingsUrl)) {
            UIApplication.sharedApplication.openURL(appSettingsUrl)
        }
    }
}

private class LocationAuthorizationDelegate(
    private val onAuthorizationChanged: () -> Unit,
) : NSObject(), CLLocationManagerDelegateProtocol {

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        onAuthorizationChanged()
    }
}

private fun readIsAuthorized(): Boolean {
    val authorization = CLLocationManager.authorizationStatus()
    return authorization == kCLAuthorizationStatusAuthorizedWhenInUse ||
        authorization == kCLAuthorizationStatusAuthorizedAlways
}
