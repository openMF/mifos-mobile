package org.mifos.mobile.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@Composable
fun UpdateEndpointDialogScreen(
    initialBaseURL: String?,
    initialTenant: String?,
    onDismissRequest: () -> Unit,
    handleEndpointUpdate: (baseURL: String, tenant: String) -> Unit
) {
    var baseURL by rememberSaveable { mutableStateOf(initialBaseURL) }
    var tenant by rememberSaveable { mutableStateOf(initialTenant) }

    Dialog(
        onDismissRequest = { onDismissRequest.invoke() }
    ) {
        Card {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
            ) {
                Text(text = stringResource(id = R.string.pref_base_url_title))
                Spacer(modifier = Modifier.height(8.dp))

                baseURL?.let {
                    OutlinedTextField(
                        value = it,
                        onValueChange = { baseURL = it },
                        label = { Text(text = stringResource(id = R.string.enter_base_url)) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                tenant?.let {
                    OutlinedTextField(
                        value = it,
                        onValueChange = { tenant = it },
                        label = { Text(text = stringResource(id = R.string.enter_tenant)) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onDismissRequest.invoke() }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            if(baseURL != null && tenant != null) {
                                handleEndpointUpdate.invoke(baseURL ?: "", tenant ?: "")
                            }
                        }
                    )
                    {
                        Text(text = stringResource(id = R.string.dialog_action_ok))
                    }
                }
            }
        }
    }
}

@Composable
@Preview()
fun PreviewUpdateEndpointDialogScreen(modifier: Modifier = Modifier) {
    MifosMobileTheme {
        UpdateEndpointDialogScreen(
            initialBaseURL = "URL",
            initialTenant = "gsoc",
            onDismissRequest = { },
            handleEndpointUpdate = { _, _ -> }
        )
    }
}
