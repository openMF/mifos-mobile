package org.mifos.mobile.ui.guarantor.guarantor_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDoubleLine
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.utils.DateHelper

@Composable
fun GuarantorDetailContent(
    data: GuarantorPayload
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.first_name),
            description = data.firstname ?: "",
            descriptionStyle =  MaterialTheme.typography.bodyLarge
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.last_name),
            description = data.lastname ?: "",
            descriptionStyle =  MaterialTheme.typography.bodyLarge
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.city),
            description = data.city ?: "",
            descriptionStyle =  MaterialTheme.typography.bodyLarge
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.guarantor_type),
            description = data.guarantorType?.value ?: "",
            descriptionStyle =  MaterialTheme.typography.bodyLarge
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}