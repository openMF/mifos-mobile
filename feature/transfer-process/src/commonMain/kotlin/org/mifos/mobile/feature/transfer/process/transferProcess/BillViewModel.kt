/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.transferProcess

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import org.mifos.mobile.core.ui.utils.BaseViewModel

class BillViewModel(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<BillState, BillEvent, BillAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<BillRoute>()
        BillState(
            transferId = route.transferId,
            amount = route.amount,
            fromAccount = route.fromAccount,
            toAccount = route.toAccount,
            date = route.date,
            remark = route.remark,
        )
    },
) {

    override fun handleAction(action: BillAction) {
        when (action) {
            BillAction.DownloadPdf -> downloadPdf()
        }
    }

    private fun downloadPdf() {
        viewModelScope.launch {
            // Generate PDF content
            val billData = TransferBillData(
                transferId = state.transferId,
                amount = state.amount,
                fromAccount = state.fromAccount,
                toAccount = state.toAccount,
                date = state.date,
                remark = state.remark,
            )
            val pdfContent = generateBillPdf(billData)
            // Save to file
            savePdfToFile(pdfContent, "transfer_receipt_${state.transferId}.pdf")
            // Send event for success
            sendEvent(BillEvent.PdfDownloaded)
        }
    }
}

data class BillState(
    val transferId: String,
    val amount: String,
    val fromAccount: String,
    val toAccount: String,
    val date: String,
    val remark: String = "",
)

sealed interface BillEvent {
    data object PdfDownloaded : BillEvent
}

sealed interface BillAction {
    data object DownloadPdf : BillAction
}
