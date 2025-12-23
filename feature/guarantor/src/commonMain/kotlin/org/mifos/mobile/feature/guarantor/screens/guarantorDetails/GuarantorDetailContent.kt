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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.city
import mifos_mobile.feature.guarantor.generated.resources.first_name
import mifos_mobile.feature.guarantor.generated.resources.guarantor_type
import mifos_mobile.feature.guarantor.generated.resources.last_name
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.ui.component.MifosTextTitleDescDoubleLine
import template.core.base.designsystem.theme.KptTheme

@Composable
internal fun GuarantorDetailContent(
    data: GuarantorPayload,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(KptTheme.spacing.md),
    ) {
        MifosTextTitleDescDoubleLine(
            title = stringResource(Res.string.first_name),
            description = data.firstname ?: "",
            descriptionStyle = KptTheme.typography.bodyLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = KptTheme.spacing.sm))

        MifosTextTitleDescDoubleLine(
            title = stringResource(Res.string.last_name),
            description = data.lastname ?: "",
            descriptionStyle = KptTheme.typography.bodyLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = KptTheme.spacing.sm))

        MifosTextTitleDescDoubleLine(
            title = stringResource(Res.string.city),
            description = data.city ?: "",
            descriptionStyle = KptTheme.typography.bodyLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = KptTheme.spacing.sm))

        MifosTextTitleDescDoubleLine(
            title = stringResource(Res.string.guarantor_type),
            description = data.guarantorType?.value ?: "",
            descriptionStyle = KptTheme.typography.bodyLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = KptTheme.spacing.sm))
    }
}
