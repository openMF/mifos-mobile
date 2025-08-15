/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.uploadDocs

import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_upload_file_error
import mifos_mobile.feature.loan_application.generated.resources.feature_upload_property_document_error
import mifos_mobile.feature.loan_application.generated.resources.feature_upload_signature_error
import mifos_mobile.feature.loan_application.generated.resources.feature_upload_statement_error
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.toBase64DataUri
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.loan.application.component.DocumentType
import org.mifos.mobile.feature.loan.application.component.SignatureUploadType
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * `ViewModel` for the Upload Documents screen.
 *
 * This ViewModel handles the logic for a user to upload various documents, including
 * bank statements, property documents, and a signature. It manages the UI state
 * related to file selection, previewing, and submission. It uses a [BaseViewModel]
 * to manage its state ([UploadDocsState]), handle actions ([UploadDocsAction]), and
 * emit events ([UploadDocsEvent]).
 *
 */
@Suppress("CyclomaticComplexMethod")
@OptIn(ExperimentalEncodingApi::class)
internal class UploadDocsViewModel :
    BaseViewModel<UploadDocsState, UploadDocsEvent, UploadDocsAction>(
        initialState = run {
            UploadDocsState()
        },
    ) {
    override fun handleAction(action: UploadDocsAction) {
        when (action) {
            is UploadDocsAction.OnNavigateBack -> {
                sendEvent(UploadDocsEvent.NavigateBack)
            }

            is UploadDocsAction.RemoveDocument -> {
                removeFile(action.type)
            }

            is UploadDocsAction.SelectNewDocument -> handleDocumentUpload(action.type)

            is UploadDocsAction.ViewDocument -> {
                sendEvent(UploadDocsEvent.PreviewDoc(action.type))
            }

            is UploadDocsAction.UploadDocument -> handleDocumentUpload(action.type)

            is UploadDocsAction.OnContinueClick -> validateAndSubmit()

            is UploadDocsAction.ShowSignatureUploadSheet -> {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = UploadDocumentDialog.ShowSignaturePicker(isSignatureMode = false),
                    )
                }
            }

            is UploadDocsAction.DismissDialog -> {
                mutableStateFlow.update {
                    it.copy(dialogState = null)
                }
            }

            is UploadDocsAction.UploadSignature -> handleUploadSignature(action)

            is UploadDocsAction.UploadSign -> {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = null,
                        signatureDocumentFile = action.image,
                        // TODO:: Generate Unique Name & Calculate Size
                        signatureFileName = "signature.png",
                        signatureSize = "100 KB",
                    )
                }
            }

            is UploadDocsAction.Internal.UploadDocumentResultReceived -> handleUploadDocumentResult(
                action,
            )

            is UploadDocsAction.NavigateToNextScreen -> {
                mutableStateFlow.update {
                    it.copy(dialogState = null)
                }

                sendEvent(UploadDocsEvent.Next)
            }
        }
    }

    /**
     * Handles a user's request to upload a document by delegating to a specific
     * upload function based on the document type.
     *
     * @param type The [DocumentType] to be uploaded.
     */
    private fun handleDocumentUpload(type: DocumentType) {
        when (type) {
            DocumentType.BANK_STATEMENT -> uploadBankStatements()
            DocumentType.PROPERTY_DOCUMENT -> uploadPropertyDocument()
            else -> Unit
        }
    }

    /**
     * Removes a document of a specific type from the state.
     *
     * @param type The [DocumentType] of the document to be removed.
     */
    private fun removeFile(type: DocumentType) {
        when (type) {
            DocumentType.BANK_STATEMENT -> {
                mutableStateFlow.update {
                    it.copy(
                        bankStatementFile = null,
                        bankStatementFileName = null,
                        bankStatementSize = null,
                    )
                }
            }

            DocumentType.PROPERTY_DOCUMENT -> {
                mutableStateFlow.update {
                    it.copy(
                        propertyDocumentsFile = null,
                        propertyDocumentFileName = null,
                        propertyDocumentsSize = null,
                    )
                }
            }

            DocumentType.SIGNATURE -> {
                mutableStateFlow.update {
                    it.copy(
                        signatureDocumentFile = null,
                        signatureFileName = null,
                        signatureSize = null,
                    )
                }
            }
        }
    }

    /**
     * Launches a file picker to select a property document and updates the state
     * with the selected file's data and metadata.
     */
    private fun uploadPropertyDocument() {
        viewModelScope.launch {
            try {
                val image = FileKit.openFilePicker(type = FileKitType.Image)
                image?.let { file ->
                    val sizeInKB = file.size() / 1024
                    val sizeInMb = sizeInKB / 1024
                    val showFileSize = if (sizeInMb >= 1) {
                        "$sizeInMb MB"
                    } else {
                        "$sizeInKB KB"
                    }

                    mutableStateFlow.update {
                        it.copy(
                            propertyDocumentsFile = file.readBytes().toBase64DataUri(),
                            propertyDocumentFileName = file.name,
                            propertyDocumentsSize = showFileSize,
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handleErrorMessage(Res.string.feature_upload_file_error)
            }
        }
    }

    /**
     * Launches a file picker to select a bank statement and updates the state
     * with the selected file's data and metadata.
     */
    private fun uploadBankStatements() {
        viewModelScope.launch {
            try {
                val image = FileKit.openFilePicker(type = FileKitType.Image)
                image?.let { file ->
                    val sizeInKB = file.size() / 1024
                    val sizeInMb = sizeInKB / 1024
                    val showFileSize = if (sizeInMb >= 1) {
                        "$sizeInMb MB"
                    } else {
                        "$sizeInKB KB"
                    }

                    mutableStateFlow.update {
                        it.copy(
                            bankStatementFile = file.readBytes().toBase64DataUri(),
                            bankStatementFileName = file.name,
                            bankStatementSize = showFileSize,
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handleErrorMessage(Res.string.feature_upload_file_error)
            }
        }
    }

    /**
     * Handles the user's request to upload a signature by delegating to the appropriate
     * function based on the signature upload type.
     *
     * @param action The [UploadDocsAction.UploadSignature] containing the type of upload.
     */
    private fun handleUploadSignature(action: UploadDocsAction.UploadSignature) {
        when (action.type) {
            SignatureUploadType.SIGN -> handleShowSignatureUploadSheet()
            // TODO: It should launch camera
            SignatureUploadType.CAPTURE -> uploadSignature()
            SignatureUploadType.GALLERY -> uploadSignature()
        }
    }

    /**
     * Launches a file picker to select an image for the signature and updates the
     * state with the selected file's data.
     */
    private fun uploadSignature() {
        viewModelScope.launch {
            try {
                val image = FileKit.openFilePicker(type = FileKitType.Image)

                image?.let { file ->
                    val sizeInKB = file.size() / 1024
                    val sizeInMb = sizeInKB / 1024
                    val showFileSize = if (sizeInMb >= 1) {
                        "$sizeInMb MB"
                    } else {
                        "$sizeInKB KB"
                    }

                    mutableStateFlow.update {
                        it.copy(
                            dialogState = null,
                            signatureDocumentFile = file.readBytes().toBase64DataUri(),
                            signatureFileName = file.name,
                            signatureSize = showFileSize,
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handleErrorMessage(Res.string.feature_upload_file_error)
            }
        }
    }

    /**
     * Updates the dialog state to show the signature drawing sheet.
     */
    private fun handleShowSignatureUploadSheet() {
        mutableStateFlow.update {
            it.copy(
                dialogState = UploadDocumentDialog.ShowSignaturePicker(isSignatureMode = true),
            )
        }
    }

    /**
     * Updates the state to show an error dialog.
     *
     * @param message The [StringResource] for the error message.
     */
    private fun handleErrorMessage(message: StringResource) {
        mutableStateFlow.update {
            it.copy(
                dialogState = UploadDocumentDialog.Error(message),
            )
        }
    }

    /**
     * Validates that all required documents are uploaded before proceeding.
     * If any are missing, it displays an error message. Otherwise, it calls
     * the `handleSubmit` function.
     */
    private fun validateAndSubmit() = when {
        state.bankStatementFile == null -> {
            handleErrorMessage(Res.string.feature_upload_statement_error)
        }

        state.propertyDocumentsFile == null -> {
            handleErrorMessage(Res.string.feature_upload_property_document_error)
        }

        state.signatureDocumentFile == null -> {
            handleErrorMessage(Res.string.feature_upload_signature_error)
        }

        else -> {
            handleSubmit()
        }
    }

    /**
     * Handles the submission of the documents.
     */
    private fun handleSubmit() {
        viewModelScope.launch {
            // TODO: Implement the actual document submission logic here.
            // This could involve a repository call to an API.
        }
    }

    /**
     * Handles the result of a document upload request.
     *
     * @param action The [UploadDocsAction.Internal.UploadDocumentResultReceived] containing the result message.
     */
    @Suppress("UnusedParameter")
    private fun handleUploadDocumentResult(action: UploadDocsAction.Internal.UploadDocumentResultReceived) {
        // TODO: Implement logic to handle the upload result, e.g., show a toast or update state.
    }
}

/**
 * Represents the UI state for the Upload Documents screen.
 *
 * @property bankStatementFile The base64-encoded string of the bank statement file, or `null`.
 * @property propertyDocumentsFile The base64-encoded string of the property document file, or `null`.
 * @property signatureDocumentFile The base64-encoded string of the signature file, or `null`.
 * @property bankStatementSize The size of the bank statement file as a formatted string, or `null`.
 * @property propertyDocumentsSize The size of the property document file as a formatted string, or `null`.
 * @property signatureSize The size of the signature file as a formatted string, or `null`.
 * @property bankStatementFileName The name of the bank statement file, or `null`.
 * @property propertyDocumentFileName The name of the property document file, or `null`.
 * @property signatureFileName The name of the signature file, or `null`.
 * @property dialogState The state of any dialog to be shown on the screen.
 */
internal data class UploadDocsState(
    val bankStatementFile: String? = null,
    val propertyDocumentsFile: String? = null,
    val signatureDocumentFile: String? = null,
    val bankStatementSize: String? = null,
    val propertyDocumentsSize: String? = null,
    val signatureSize: String? = null,
    val bankStatementFileName: String? = null,
    val propertyDocumentFileName: String? = null,
    val signatureFileName: String? = null,
    val dialogState: UploadDocumentDialog? = null,
) {
    /**
     * A boolean indicating if the submit button should be enabled.
     */
    val isSubmitEnabled =
        bankStatementFile != null && propertyDocumentsFile != null && signatureDocumentFile != null
}

/**
 * A sealed interface representing the different types of dialogs that can be
 * shown on the Upload Documents screen.
 */
internal sealed interface UploadDocumentDialog {
    /**
     * Represents a modal bottom sheet for signature upload options.
     * @property isSignatureMode A boolean to control the content shown inside the sheet.
     */
    data class ShowSignaturePicker(val isSignatureMode: Boolean) : UploadDocumentDialog

    /**
     * Represents a generic error dialog with a message.
     * @property error The [StringResource] for the error message.
     */
    data class Error(val error: StringResource) : UploadDocumentDialog
}

/**
 * A sealed interface representing one-time events that trigger UI side effects,
 * such as navigation.
 */
internal sealed interface UploadDocsEvent {
    /** Event to navigate back from the screen. */
    data object NavigateBack : UploadDocsEvent

    /**
     * Event to navigate to a document preview screen.
     * @property documentType The [DocumentType] of the document to preview.
     */
    data class PreviewDoc(val documentType: DocumentType) : UploadDocsEvent

    /** Event to navigate to the next screen in the flow. */
    data object Next : UploadDocsEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle for the Upload Documents screen.
 */
internal sealed interface UploadDocsAction {
    /** User action to navigate back. */
    data object OnNavigateBack : UploadDocsAction

    /** User action to continue to the next screen after validation. */
    data object OnContinueClick : UploadDocsAction

    /** Action to navigate to the next screen. */
    data object NavigateToNextScreen : UploadDocsAction

    /** User action to dismiss a dialog. */
    data object DismissDialog : UploadDocsAction

    /** User action to show the signature upload modal bottom sheet. */
    data object ShowSignatureUploadSheet : UploadDocsAction

    /**
     * User action to select a signature based on a specific method.
     * @property type The [SignatureUploadType] for the chosen method.
     */
    data class UploadSignature(val type: SignatureUploadType) : UploadDocsAction

    /**
     * User action to upload a signed image.
     * @property image The base64-encoded string of the signature image.
     */
    data class UploadSign(val image: String) : UploadDocsAction

    /**
     * User action to initiate the upload of a document.
     * @property type The [DocumentType] to be uploaded.
     */
    data class UploadDocument(val type: DocumentType) : UploadDocsAction

    /**
     * User action to remove an uploaded document.
     * @property type The [DocumentType] to be removed.
     */
    data class RemoveDocument(val type: DocumentType) : UploadDocsAction

    /**
     * User action to view a previously uploaded document.
     * @property type The [DocumentType] to be viewed.
     */
    data class ViewDocument(val type: DocumentType) : UploadDocsAction

    /**
     * User action to select a new document for a given type.
     * @property type The [DocumentType] for which a new document is being selected.
     */
    data class SelectNewDocument(val type: DocumentType) : UploadDocsAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : UploadDocsAction {
        /**
         * An internal action to handle the result of a document upload request.
         * @property message The [StringResource] for the result message.
         */
        data class UploadDocumentResultReceived(val message: StringResource) : Internal
    }
}
