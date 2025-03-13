/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccountApplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.client_name
import mifos_mobile.feature.savings.generated.resources.select_product_id
import mifos_mobile.feature.savings.generated.resources.submission_date
import mifos_mobile.feature.savings.generated.resources.submit
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate

@Composable
internal fun SavingsAccountApplicationContent(
    submit: (Int, Int, showToast: (StringResource) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    existingProduct: String? = null,
    savingsAccountTemplate: SavingsAccountTemplate? = null,
) {
    var selectProductId by rememberSaveable { mutableIntStateOf(-1) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedCard(
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                TitleBodyRow(
                    titleText = stringResource(Res.string.client_name),
                    bodyText = savingsAccountTemplate?.clientName ?: "",
                )
                TitleBodyRow(
                    titleText = stringResource(Res.string.submission_date),
                    bodyText = DateHelper.formattedFullDate,
                )
            }
        }

        SelectProductIdDropDown(
            existingProduct = existingProduct,
            selectProductId = { selectProductId = it },
            savingsAccountTemplate = savingsAccountTemplate,
        )

        MifosButton(
            content = { Text(stringResource(Res.string.submit)) },
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                submit(selectProductId, savingsAccountTemplate?.clientId ?: -1) {
                    scope.launch {
                        snackbarHostState.showSnackbar(it.toString())
                    }
                }
            },
        )
    }
}

@Composable
private fun SelectProductIdDropDown(
    existingProduct: String?,
    selectProductId: (Int) -> Unit,
    modifier: Modifier = Modifier,
    savingsAccountTemplate: SavingsAccountTemplate? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf(existingProduct ?: "") }
    val productOptions = savingsAccountTemplate?.productOptions.orEmpty()

    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = selectedProduct,
            onValueChange = { selectedProduct = it },
            label = { Text(stringResource(Res.string.select_product_id)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            enabled = false,
            readOnly = true,
            colors = TextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface,
            ),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) {
                        MifosIcons.ArrowDropUp
                    } else {
                        MifosIcons.ArrowDropDown
                    },
                    contentDescription = "Dropdown",
                )
            },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
        ) {
            productOptions.forEach { product ->
                DropdownMenuItem(
                    onClick = {
                        selectProductId(product.id ?: -1)
                        selectedProduct = product.name ?: ""
                        expanded = false
                    },
                    text = { Text(text = product.name ?: "") },
                )
            }
        }
    }
}

@Composable
private fun TitleBodyRow(
    titleText: String,
    bodyText: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .alpha(0.7f)
                .weight(2f),
        )
        Text(
            text = bodyText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(3f),
            textAlign = TextAlign.Center,
        )
    }
}
