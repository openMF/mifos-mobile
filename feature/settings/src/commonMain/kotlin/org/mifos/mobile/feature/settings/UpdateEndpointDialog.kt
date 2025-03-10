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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.cancel
import mifos_mobile.feature.settings.generated.resources.dialog_action_ok
import mifos_mobile.feature.settings.generated.resources.enter_base_url
import mifos_mobile.feature.settings.generated.resources.enter_tenant
import mifos_mobile.feature.settings.generated.resources.pref_base_url_title
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UpdateEndpointDialogScreen(
    initialBaseURL: String?,
    initialTenant: String?,
    onDismissRequest: () -> Unit,
    handleEndpointUpdate: (baseURL: String, tenant: String) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    var baseURL by rememberSaveable { mutableStateOf(initialBaseURL) }
    var tenant by rememberSaveable { mutableStateOf(initialTenant) }

    val coroutineScope = rememberCoroutineScope()

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
                Text(text = stringResource(Res.string.pref_base_url_title))
                Spacer(modifier = Modifier.height(8.dp))

                baseURL?.let { url ->
                    OutlinedTextField(
                        value = url,
                        onValueChange = { baseURL = it },
                        label = { Text(text = stringResource(Res.string.enter_base_url)) },
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                tenant?.let { selectedTenant ->
                    OutlinedTextField(
                        value = selectedTenant,
                        onValueChange = { tenant = it },
                        label = { Text(text = stringResource(Res.string.enter_tenant)) },
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    MifosTextButton(
                        content = { Text(stringResource(Res.string.cancel)) },
                        onClick = onDismissRequest,
                    )

                    MifosTextButton(
                        content = { Text(stringResource(Res.string.dialog_action_ok)) },
                        onClick = {
                            val url = baseURL?.takeIf { it.isNotBlank() }
                            val id = tenant?.takeIf { it.isNotBlank() }

                            if (url == null || id == null) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Base URL and Tenant ID are required")
                                }
                            } else {
                                handleEndpointUpdate(url, id)
                            }
                        },
                    )
                }
            }
        }
    }
}
