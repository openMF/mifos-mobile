/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.charge.utils.ClientChargeState
import org.mifos.mobile.feature.client_charge.R
import javax.inject.Inject

@HiltViewModel
internal class ClientChargeViewModel @Inject constructor(
    private val clientChargeRepositoryImp: ClientChargeRepository,
    preferencesHelper: PreferencesHelper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Trigger for refreshing charges
    private val refreshTrigger = MutableStateFlow(false)

    private val chargeTypeString = savedStateHandle.getStateFlow<String?>(
        key = Constants.CHARGE_TYPE,
        initialValue = null,
    )

    private val chargeTypeId = savedStateHandle.getStateFlow(
        key = Constants.CHARGE_TYPE_ID,
        initialValue = preferencesHelper.clientId ?: -1,
    )

    private val chargeType: StateFlow<ChargeType> = chargeTypeString
        .map { it?.let { ChargeType.valueOf(it) } ?: ChargeType.CLIENT }
        .stateIn(viewModelScope, SharingStarted.Lazily, ChargeType.CLIENT)

    val topBarTitleResId: StateFlow<Int>
        get() = chargeType.map { chargeType ->
            when (chargeType) {
                ChargeType.CLIENT -> R.string.client_charges
                ChargeType.SAVINGS -> R.string.savings_charges
                ChargeType.LOAN -> R.string.loan_charges
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, R.string.charges)

    val charges: StateFlow<ClientChargeState> =
        combine(chargeType, chargeTypeId, refreshTrigger) { type, id, _ ->
            Pair(type, id)
        }.flatMapLatest { typeAndId ->
            clientChargeRepositoryImp.getCharges(
                chargeType = typeAndId.first,
                chargeTypeId = typeAndId.second,
            )
                .map { ClientChargeState.Success(it) as ClientChargeState }
                .catch { emit(ClientChargeState.Error(it.message)) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ClientChargeState.Loading,
        )

    // Function to manually refresh
    fun refreshCharges() {
        viewModelScope.launch {
            refreshTrigger.tryEmit(!refreshTrigger.value) // Triggers re-computation
        }
    }
}
