package org.mifos.mobile.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
//import org.mifos.mobile.utils.BeneficiaryUiState

//fun TestScope.obserrveUiState(): MutableList<BeneficiaryUiState> {
//    val uiStates = mutableListOf<BeneficiaryUiState>()
//    viewModel.beneficiaryUiState.onEach {
//        println(it)
//        uiStates.add(it)
//    }
//        .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
//    return uiStates
//}