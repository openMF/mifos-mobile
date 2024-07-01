package org.mifos.mobile.feature.savings.savings_account_application

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.utils.getTodayFormatted
import org.mifos.mobile.feature.savings.R

@Composable
fun SavingsAccountApplicationContent(
    existingProduct: String? = null,
    savingsAccountTemplate: SavingsAccountTemplate? = null,
    submit: (Int, Int, showToast: (Int) -> Unit) -> Unit
) {
    var selectProductId by rememberSaveable { mutableIntStateOf(-1) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedCard(
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                TitleBodyRow(
                    titleText = stringResource(R.string.client_name),
                    bodyText = savingsAccountTemplate?.clientName ?: ""
                )
                Spacer(modifier = Modifier.height(16.dp))
                TitleBodyRow(
                    titleText = stringResource(R.string.submission_date),
                    bodyText = getTodayFormatted()
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SelectProductIdDropDown(
            existingProduct = existingProduct,
            savingsAccountTemplate = savingsAccountTemplate,
            selectProductId = { selectProductId = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                submit(selectProductId, savingsAccountTemplate?.clientId ?: -1) {
                    showToast(context, it)
                }
            },
            content = { Text(text = stringResource(id = R.string.submit)) }
        )
    }
}

private fun showToast(context: Context, messageResId: Int){
    Toast.makeText(context, context.getString(messageResId), Toast.LENGTH_LONG).show()
}

@Composable
fun SelectProductIdDropDown(
    existingProduct: String?,
    savingsAccountTemplate: SavingsAccountTemplate? = null,
    selectProductId: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf(existingProduct ?: "") }
    val productOptions = savingsAccountTemplate?.productOptions.orEmpty()

    Column(
        modifier = Modifier
    ) {
        OutlinedTextField(
            value = selectedProduct,
            onValueChange = { selectedProduct = it },
            label = { Text(stringResource(id = R.string.select_product_id)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            enabled = false,
            readOnly = true,
            colors = TextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface
            ),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp
                    else Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            productOptions.forEach { product ->
                DropdownMenuItem(
                    onClick = {
                        selectProductId(product.id ?: -1)
                        selectedProduct = product.name ?: ""
                        expanded = false
                    },
                    text = { Text(text = product.name ?: "") }
                )
            }
        }
    }
}

@Composable
fun TitleBodyRow(titleText: String, bodyText: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .alpha(0.7f)
                .weight(2f)
        )
        Text(
            text = bodyText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(3f),
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun SavingsAccountApplicationContentPreview() {
    MifosMobileTheme {
        SavingsAccountApplicationContent(null, null, {i, j, k ->  })
    }
}