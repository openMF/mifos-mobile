package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection


data class FloatingActionButtonContent(
    val onClick: (() -> Unit),
    val contentColor: Color,
    val content: (@Composable () -> Unit)
)

@Composable
fun MFScaffold(
    topBarTitleResId: Int,
    navigateBack: () -> Unit,
    floatingActionButtonContent: FloatingActionButtonContent? = null,
    snackbarHost: @Composable () -> Unit = {},
    scaffoldContent: @Composable (PaddingValues) -> Unit
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    Scaffold(
        topBar = {
            MifosTopBarTitle(
                topBarTitleResId = topBarTitleResId,
                navigateBack = navigateBack
            )
        },
        floatingActionButton = {
            floatingActionButtonContent?.let { content ->
                FloatingActionButton(
                    modifier = Modifier.padding(
                        end = padding.calculateEndPadding(LayoutDirection.Ltr)
                    ),
                    onClick = content.onClick,
                    contentColor = content.contentColor,
                    content = content.content
                )
            }
        },
        snackbarHost = snackbarHost,
        content = {
            padding = it
            scaffoldContent(it)
        }
    )
}

@Composable
fun MFScaffold(
    topBar: @Composable () -> Unit,
    floatingActionButtonContent: FloatingActionButtonContent? = null,
    snackbarHost: @Composable () -> Unit = {},
    scaffoldContent: @Composable (PaddingValues) -> Unit
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    Scaffold(
        topBar = topBar,
        floatingActionButton = {
            floatingActionButtonContent?.let { content ->
                FloatingActionButton(
                    modifier = Modifier.padding(
                        end = padding.calculateEndPadding(LayoutDirection.Ltr)
                    ),
                    onClick = content.onClick,
                    contentColor = content.contentColor,
                    content = content.content
                )
            }
        },
        snackbarHost = snackbarHost,
        content = {
            padding = it
            scaffoldContent(it)
        }
    )
}