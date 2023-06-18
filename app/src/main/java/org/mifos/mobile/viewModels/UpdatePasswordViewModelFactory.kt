package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.injection.PerActivity
import javax.inject.Inject

@PerActivity
class UpdatePasswordViewModelFactory @Inject constructor(
    private val dataManager: DataManager?,
    private val preferencesHelper: PreferencesHelper?
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdatePasswordViewModel::class.java)) {
            return UpdatePasswordViewModel(dataManager, preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}