package org.mifos.mobile.core.ui.component

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

fun Fragment.mifosComposeView(context: Context, content: @Composable () -> Unit): ComposeView {
    return ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            MifosMobileTheme {
                content()
            }
        }
    }
}