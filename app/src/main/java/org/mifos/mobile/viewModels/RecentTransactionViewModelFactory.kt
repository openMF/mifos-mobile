package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.PerActivity
import javax.inject.Inject

@PerActivity
class RecentTransactionViewModelFactory @Inject constructor(private val dataManager: DataManager?) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecentTransactionViewModel::class.java)) {
            return RecentTransactionViewModel(dataManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}