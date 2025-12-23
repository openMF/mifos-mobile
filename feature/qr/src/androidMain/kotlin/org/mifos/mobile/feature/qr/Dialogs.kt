/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.attafitamim.krop.core.crop.CropError
import com.attafitamim.krop.core.crop.CropperLoading
import org.mifos.mobile.core.designsystem.theme.DesignToken
import template.core.base.designsystem.theme.KptTheme

@Composable
fun CropErrorDialog(error: CropError, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { Button(onClick = onDismiss) { Text("Ok") } },
        text = { Text(error.getMessage()) },
    )
}

@Composable
fun CropError.getMessage(): String = remember(this) {
    when (this) {
        CropError.LoadingError -> "Error while opening the image !"
        CropError.SavingError -> "Error while saving the image !"
    }
}

@Composable
fun LoadingDialog(
    status: CropperLoading,
    modifier: Modifier = Modifier,
) {
    var dismissed by remember(status) { mutableStateOf(false) }
    if (!dismissed) {
        Dialog(onDismissRequest = { dismissed = true }) {
            Surface(shape = KptTheme.shapes.small) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.dp6, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(KptTheme.spacing.md),
                ) {
                    CircularProgressIndicator()
                    Text(text = status.getMessage())
                }
            }
        }
    }
}

@Composable
fun CropperLoading.getMessage(): String {
    return remember(this) {
        when (this) {
            CropperLoading.PreparingImage -> "Preparing Image"
            CropperLoading.SavingResult -> "Saving Result"
        }
    }
}
