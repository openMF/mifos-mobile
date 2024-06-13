package org.mifos.mobile.ui.beneficiary_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosTitleDescSingleLineEqual
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.beneficiary.Beneficiary

@Composable
fun BeneficiaryDetailContent(
    beneficiary: Beneficiary?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        MifosTitleDescSingleLineEqual(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(4.dp),
            title = stringResource(id = R.string.beneficiary_name),
            description = beneficiary?.name.toString()
        )

        MifosTitleDescSingleLineEqual(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(4.dp),
            title = stringResource(id = R.string.account_number),
            description = beneficiary?.accountNumber.toString()
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                MifosTitleDescSingleLineEqual(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stringResource(id = R.string.client_name),
                    beneficiary?.clientName.toString()
                )

                MifosTitleDescSingleLineEqual(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stringResource(id = R.string.account_type),
                    description = beneficiary?.accountType.toString()
                )

                MifosTitleDescSingleLineEqual(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stringResource(id = R.string.transfer_limit),
                    description = beneficiary?.transferLimit.toString()
                )

                MifosTitleDescSingleLineEqual(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stringResource(id = R.string.office_name),
                    description = beneficiary?.officeName.toString()
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewBeneficiaryDetailContent(modifier: Modifier = Modifier) {
    MifosMobileTheme {
        BeneficiaryDetailContent(
            beneficiary = Beneficiary()
        )
    }
}