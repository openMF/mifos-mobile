/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDoubleLine
import org.mifos.mobile.feature.guarantor.R

@Composable
internal fun GuarantorDetailContent(
    data: GuarantorPayload,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.first_name),
            description = data.firstname ?: "",
            descriptionStyle = MaterialTheme.typography.bodyLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.last_name),
            description = data.lastname ?: "",
            descriptionStyle = MaterialTheme.typography.bodyLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.city),
            description = data.city ?: "",
            descriptionStyle = MaterialTheme.typography.bodyLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        MifosTextTitleDescDoubleLine(
            title = stringResource(id = R.string.guarantor_type),
            description = data.guarantorType?.value ?: "",
            descriptionStyle = MaterialTheme.typography.bodyLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}
