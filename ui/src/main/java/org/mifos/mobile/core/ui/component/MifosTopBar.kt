package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosTopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    title: @Composable () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = {
            IconButton(
                onClick = { navigateBack.invoke() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSystemInDarkTheme())
                Color(0xFF1B1B1F)
            else
                Color(0xFFFEFBFF)
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MifosTopBarTitle(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    topBarTitleResId: Int,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(id = topBarTitleResId)) },
        navigationIcon = {
            IconButton(
                onClick = { navigateBack.invoke() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSystemInDarkTheme())
                Color(0xFF1B1B1F)
            else
                Color(0xFFFEFBFF)
        ),
    )
}
