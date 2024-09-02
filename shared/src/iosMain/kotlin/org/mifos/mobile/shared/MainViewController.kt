package org.mifos.mobile.shared

import androidx.compose.ui.window.ComposeUIViewController
import org.mifos.mobile.shared.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}