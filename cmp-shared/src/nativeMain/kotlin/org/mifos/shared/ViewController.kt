/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.mifos.shared

import androidx.compose.ui.window.ComposeUIViewController
import cmp.shared.SharedApp
import cmp.shared.utils.initKoin
import platform.Foundation.NSUserDefaults
import platform.QuartzCore.CALayer
import platform.UIKit.UIApplication
import platform.UIKit.UITextField
import platform.UIKit.UIUserInterfaceStyle

private var secureTextField: UITextField? = null

fun viewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
) {
    SharedApp(
        updateScreenCapture = { isScreenCaptureAllowed ->
            UIApplication.sharedApplication.keyWindow?.let { window ->
                if (!isScreenCaptureAllowed) {
                    // Create secure text field to prevent screen capture/recording
                    if (secureTextField == null) {
                        val textField = UITextField()
                        textField.setSecureTextEntry(true)
                        textField.setUserInteractionEnabled(false)
                        window.addSubview(textField)
                        (textField.layer.sublayers?.firstOrNull() as? CALayer)?.let { secureLayer ->
                            window.layer.superlayer?.addSublayer(secureLayer)
                        }
                        secureTextField = textField
                    }
                } else {
                    secureTextField?.removeFromSuperview()
                    secureTextField = null
                }
            }
        },
        handleRecreate = {},
        handleThemeMode = { osValue ->
            val style = when (osValue) {
                1 -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
                2 -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
                else -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
            }
            UIApplication.sharedApplication.keyWindow?.overrideUserInterfaceStyle = style
        },
        handleAppLocale = { languageTag ->
            if (languageTag != null) {
                // Set specific language
                NSUserDefaults.standardUserDefaults.setObject(listOf(languageTag), forKey = "AppleLanguages")
            } else {
                // System Default: remove app-specific language setting
                NSUserDefaults.standardUserDefaults.removeObjectForKey("AppleLanguages")
            }
            NSUserDefaults.standardUserDefaults.synchronize()
        },
        onSplashScreenRemoved = {},
    )
}
