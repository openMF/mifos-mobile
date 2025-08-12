/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.component

import androidx.compose.runtime.Composable
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_upload_docs_bank_account
import mifos_mobile.feature.loan_application.generated.resources.feature_upload_docs_collateral
import mifos_mobile.feature.loan_application.generated.resources.feature_upload_docs_signature
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosUploadStateCard
import org.mifos.mobile.core.designsystem.component.MifosUploadedStateCard
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.feature.loan.application.uploadDocs.UploadDocsAction
import org.mifos.mobile.feature.loan.application.uploadDocs.UploadDocsState

@Composable
internal fun UploadDocumentsSection(
    state: UploadDocsState,
    onAction: (UploadDocsAction) -> Unit,
) {
    if (state.bankStatementFile != null) {
        MifosUploadedStateCard(
            icon = MifosIcons.Receipt,
            label = stringResource(Res.string.feature_upload_docs_bank_account),
            fileName = state.bankStatementFileName ?: "",
            fileSize = state.bankStatementSize ?: "",
            onRemoveClick = { onAction(UploadDocsAction.RemoveDocument(DocumentType.BANK_STATEMENT)) },
            onViewClick = { },
            onSelectNewClick = { onAction(UploadDocsAction.SelectNewDocument(DocumentType.BANK_STATEMENT)) },
        )
    } else {
        MifosUploadStateCard(
            icon = MifosIcons.Image,
            text = stringResource(Res.string.feature_upload_docs_bank_account),
            onClick = {
                onAction(UploadDocsAction.UploadDocument(DocumentType.BANK_STATEMENT))
            },
        )
    }

    if (state.propertyDocumentsFile != null) {
        MifosUploadedStateCard(
            icon = MifosIcons.DocumentGlobe,
            label = stringResource(Res.string.feature_upload_docs_collateral),
            fileName = state.propertyDocumentFileName ?: "",
            fileSize = state.propertyDocumentsSize ?: "",
            onRemoveClick = { onAction(UploadDocsAction.RemoveDocument(DocumentType.PROPERTY_DOCUMENT)) },
            onViewClick = { },
            onSelectNewClick = { onAction(UploadDocsAction.SelectNewDocument(DocumentType.PROPERTY_DOCUMENT)) },
        )
    } else {
        MifosUploadStateCard(
            icon = MifosIcons.Image,
            text = stringResource(Res.string.feature_upload_docs_collateral),
            onClick = {
                onAction(UploadDocsAction.UploadDocument(DocumentType.PROPERTY_DOCUMENT))
            },
        )
    }

    if (state.signatureDocumentFile != null) {
        MifosUploadedStateCard(
            icon = MifosIcons.Signature,
            label = stringResource(Res.string.feature_upload_docs_signature),
            fileName = state.signatureFileName ?: "",
            fileSize = state.signatureSize ?: "",
            onRemoveClick = { onAction(UploadDocsAction.RemoveDocument(DocumentType.SIGNATURE)) },
            onViewClick = { },
            onSelectNewClick = { onAction(UploadDocsAction.ShowSignatureUploadSheet) },
        )
    } else {
        MifosUploadStateCard(
            icon = MifosIcons.Image,
            text = stringResource(Res.string.feature_upload_docs_signature),
            onClick = {
                onAction(UploadDocsAction.ShowSignatureUploadSheet)
            },
        )
    }
}
