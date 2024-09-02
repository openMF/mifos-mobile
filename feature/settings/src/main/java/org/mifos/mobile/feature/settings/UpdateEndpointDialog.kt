/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.components.MifosTextButton
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UpdateEndpointDialogScreen(
    initialBaseURL: String?,
    initialTenant: String?,
    onDismissRequest: () -> Unit,
    handleEndpointUpdate: (baseURL: String, tenant: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var baseURL by rememberSaveable { mutableStateOf(initialBaseURL) }
    var tenant by rememberSaveable { mutableStateOf(initialTenant) }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Text(text = stringResource(id = R.string.pref_base_url_title))
                Spacer(modifier = Modifier.height(8.dp))

                baseURL?.let { url ->
                    OutlinedTextField(
                        value = url,
                        onValueChange = { baseURL = it },
                        label = { Text(text = stringResource(id = R.string.enter_base_url)) },
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                tenant?.let { selectedTenant ->
                    OutlinedTextField(
                        value = selectedTenant,
                        onValueChange = { tenant = it },
                        label = { Text(text = stringResource(id = R.string.enter_tenant)) },
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    MifosTextButton(
                        text = stringResource(id = R.string.cancel),
                        onClick = onDismissRequest,
                    )

                    MifosTextButton(
                        text = stringResource(id = R.string.dialog_action_ok),
                        onClick = {
                            if (baseURL != null && tenant != null) {
                                handleEndpointUpdate.invoke(baseURL ?: "", tenant ?: "")
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
@DevicePreviews
private fun PreviewUpdateEndpointDialogScreen(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        UpdateEndpointDialogScreen(
            initialBaseURL = "URL",
            initialTenant = "soc",
            modifier = modifier,
            onDismissRequest = { },
            handleEndpointUpdate = { _, _ -> },
        )
    }
}
