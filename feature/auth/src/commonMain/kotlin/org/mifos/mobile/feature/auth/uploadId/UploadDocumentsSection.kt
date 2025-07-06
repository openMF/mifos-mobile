/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.uploadId

import androidx.compose.runtime.Composable
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_identification_doc_label
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_profile_photo_label
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_remove_file
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_select_new_file
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_upload_your_id
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_upload_your_photo
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_view_file
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosUploadStateCard
import org.mifos.mobile.core.designsystem.component.MifosUploadedStateCard
import org.mifos.mobile.core.designsystem.icon.MifosIcons

@Composable
internal fun UploadDocumentsSection(
    state: UploadIdUiState,
    onAction: (UploadIdAction) -> Unit,
) {
    if (state.idFile != null) {
        MifosUploadedStateCard(
            removeText = stringResource(Res.string.feature_upload_id_remove_file),
            selectText = stringResource(Res.string.feature_upload_id_select_new_file),
            viewText = stringResource(Res.string.feature_upload_id_view_file),
            icon = MifosIcons.DocumentFilled,
            label = stringResource(Res.string.feature_upload_id_identification_doc_label),
            fileName = state.idFileName ?: "",
            fileSize = state.idFileSize ?: "",
            onRemoveClick = { onAction(UploadIdAction.OnRemoveId) },
            onViewClick = { },
            onSelectNewClick = { onAction(UploadIdAction.OnPickId) },
        )
    } else {
        MifosUploadStateCard(
            icon = MifosIcons.UploadId,
            text = stringResource(Res.string.feature_upload_id_upload_your_id),
            onClick = {
                onAction(UploadIdAction.OnPickId)
            },
        )
    }

    if (state.imageFile != null) {
        MifosUploadedStateCard(
            removeText = stringResource(Res.string.feature_upload_id_remove_file),
            selectText = stringResource(Res.string.feature_upload_id_select_new_file),
            viewText = stringResource(Res.string.feature_upload_id_view_file),
            icon = MifosIcons.DocumentFilled,
            label = stringResource(Res.string.feature_upload_id_profile_photo_label),
            fileName = state.imageFileName ?: "",
            fileSize = state.imageFileSize ?: "",
            onRemoveClick = { onAction(UploadIdAction.OnRemoveImage) },
            onViewClick = { },
            onSelectNewClick = { onAction(UploadIdAction.OnPickImage) },
        )
    } else {
        MifosUploadStateCard(
            icon = MifosIcons.Image,
            text = stringResource(Res.string.feature_upload_id_upload_your_photo),
            onClick = {
                onAction(UploadIdAction.OnPickImage)
            },
        )
    }
}
