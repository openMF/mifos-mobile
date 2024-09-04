/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mifos.library.passcode.data.PasscodeRepository
import org.mifos.library.passcode.utility.Constants.PASSCODE_LENGTH
import org.mifos.library.passcode.utility.Step
import javax.inject.Inject

@HiltViewModel
internal class PasscodeViewModel @Inject constructor(
    private val passcodeRepository: PasscodeRepository,
) : ViewModel() {

    private val mOnPasscodeConfirmed = MutableSharedFlow<String>()
    val onPasscodeConfirmed = mOnPasscodeConfirmed.asSharedFlow()

    private val mOnPasscodeRejected = MutableSharedFlow<Unit>()
    val onPasscodeRejected = mOnPasscodeRejected.asSharedFlow()

    private val mActiveStep = MutableStateFlow(Step.Create)
    val activeStep = mActiveStep.asStateFlow()

    private val mFilledDots = MutableStateFlow(0)
    val filledDots = mFilledDots.asStateFlow()

    private var createPasscode: StringBuilder = StringBuilder()
    private var confirmPasscode: StringBuilder = StringBuilder()

    private val mPasscodeVisible = MutableStateFlow(false)
    val passcodeVisible = mPasscodeVisible.asStateFlow()

    private val mCurrentPasscodeInput = MutableStateFlow("")
    val currentPasscodeInput = mCurrentPasscodeInput.asStateFlow()

    private var mIsPasscodeAlreadySet = mutableStateOf(passcodeRepository.hasPasscode)

    init {
        resetData()
    }

    private fun emitActiveStep(activeStep: Step) = viewModelScope.launch {
        mActiveStep.emit(activeStep)
    }

    private fun emitFilledDots(filledDots: Int) = viewModelScope.launch {
        mFilledDots.emit(filledDots)
    }

    private fun emitOnPasscodeConfirmed(confirmPassword: String) = viewModelScope.launch {
        mOnPasscodeConfirmed.emit(confirmPassword)
    }

    private fun emitOnPasscodeRejected() = viewModelScope.launch {
        mOnPasscodeRejected.emit(Unit)
    }

    fun togglePasscodeVisibility() {
        mPasscodeVisible.value = !mPasscodeVisible.value
    }

    private fun resetData() {
        emitActiveStep(Step.Create)
        emitFilledDots(0)

        createPasscode.clear()
        confirmPasscode.clear()
    }

    fun enterKey(key: String) {
        if (mFilledDots.value >= PASSCODE_LENGTH) {
            return
        }

        val currentPasscode =
            if (mActiveStep.value == Step.Create) createPasscode else confirmPasscode
        currentPasscode.append(key)
        mCurrentPasscodeInput.value = currentPasscode.toString()
        emitFilledDots(currentPasscode.length)

        if (mFilledDots.value == PASSCODE_LENGTH) {
            if (mIsPasscodeAlreadySet.value) {
                if (passcodeRepository.getSavedPasscode() == createPasscode.toString()) {
                    emitOnPasscodeConfirmed(createPasscode.toString())
                    createPasscode.clear()
                } else {
                    emitOnPasscodeRejected()
                    // logic for retires can be written here
                }
                mCurrentPasscodeInput.value = ""
            } else if (mActiveStep.value == Step.Create) {
                emitActiveStep(Step.Confirm)
                emitFilledDots(0)
                mCurrentPasscodeInput.value = ""
            } else {
                if (createPasscode.toString() == confirmPasscode.toString()) {
                    emitOnPasscodeConfirmed(confirmPasscode.toString())
                    passcodeRepository.savePasscode(confirmPasscode.toString())
                    mIsPasscodeAlreadySet.value = true
                    resetData()
                } else {
                    emitOnPasscodeRejected()
                    resetData()
                }
                mCurrentPasscodeInput.value = ""
            }
        }
    }

    fun deleteKey() {
        val currentPasscode =
            if (mActiveStep.value == Step.Create) createPasscode else confirmPasscode

        if (currentPasscode.isNotEmpty()) {
            currentPasscode.deleteAt(currentPasscode.length - 1)
            mCurrentPasscodeInput.value = currentPasscode.toString()
            emitFilledDots(currentPasscode.length)
        }
    }

    fun deleteAllKeys() {
        if (mActiveStep.value == Step.Create) {
            createPasscode.clear()
        } else {
            confirmPasscode.clear()
        }
        mCurrentPasscodeInput.value = ""
        emitFilledDots(0)
    }

    fun restart() {
        resetData()
        mPasscodeVisible.value = false
    }
}
