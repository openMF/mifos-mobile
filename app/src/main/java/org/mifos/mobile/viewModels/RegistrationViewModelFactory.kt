package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.mifos.mobile.injection.PerActivity
import org.mifos.mobile.repositories.UserAuthRepository
import javax.inject.Inject

@PerActivity
class RegistrationViewModelFactory @Inject constructor(private val userAuthRepositoryImp: UserAuthRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(userAuthRepositoryImp) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}