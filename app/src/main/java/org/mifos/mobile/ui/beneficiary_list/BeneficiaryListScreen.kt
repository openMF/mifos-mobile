package org.mifos.mobile.ui.beneficiary_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.models.beneficiary.Beneficiary

@Composable
fun BeneficiaryListScreen(
    navigateBack: () -> Unit,
    addBeneficiaryClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            MifosTopBar(navigateBack = { navigateBack.invoke() }) {
                Text(text = stringResource(id = R.string.beneficiaries))
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { addBeneficiaryClicked.invoke() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            BeneficiaryItem()
            BeneficiaryItem()
            BeneficiaryItem()
            BeneficiaryItem()
            BeneficiaryItem()
            BeneficiaryItem()

        }
    }
}

@Composable
fun BeneficiaryItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp), text = "Beneficiary name",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
@Preview(showSystemUi = true)
fun Preview(modifier: Modifier = Modifier) {
    BeneficiaryListScreen(
        navigateBack = {},
        addBeneficiaryClicked = {}
    )
}