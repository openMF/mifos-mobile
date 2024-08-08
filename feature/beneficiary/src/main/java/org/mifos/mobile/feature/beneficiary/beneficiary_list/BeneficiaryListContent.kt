package org.mifos.mobile.feature.beneficiary.beneficiary_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@Composable
fun ShowBeneficiary(
    beneficiaryList: List<Beneficiary>,
    onClick: (beneficiaryId: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            itemsIndexed(beneficiaryList) { index, beneficiary ->
                BeneficiaryItem(
                    beneficiary = beneficiary,
                    onClick = { onClick(beneficiary.id ?: -1) }
                )
            }
        }
    }
}

@Composable
fun BeneficiaryItem(
    beneficiary: Beneficiary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Text(
                text = "${beneficiary.name}",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${beneficiary.id}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f)
                )

                Text(
                    text = "${beneficiary.officeName}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f)
                )
            }

        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.2.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f)
        )

    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewBeneficiaryListEmpty(modifier: Modifier = Modifier) {
    val beneficiary = Beneficiary(name = "Victor", id = 242344343, officeName = "Main office")
    MifosMobileTheme {
        BeneficiaryItem(
            beneficiary = beneficiary,
            onClick = {}
        )
    }
}

