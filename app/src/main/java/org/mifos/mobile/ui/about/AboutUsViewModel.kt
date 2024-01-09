package org.mifos.mobile.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.mifos.mobile.models.AboutUsItem
import org.mifos.mobile.repositories.AboutUsRepository
import org.mifos.mobile.ui.enums.AboutUsListItemId
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor(
    private val aboutUsRepositoryImp: AboutUsRepository
) : ViewModel() {

    private val _aboutUsItemEvent = MutableLiveData<AboutUsListItemId>()
    val aboutUsItemEvent: LiveData<AboutUsListItemId> get() = _aboutUsItemEvent

    private val _aboutUsItems = MutableStateFlow<List<AboutUsItem>>(emptyList())

    val aboutUsItems = _aboutUsItems

    init {
        // Launch a coroutine in the viewModelScope to collect the Flow from the repository
        viewModelScope.launch {
            aboutUsRepositoryImp.getAboutUsItems().collect { items ->
                // Update the MutableStateFlow with the latest value from the Flow
                _aboutUsItems.value = items
            }
        }
    }

    fun navigateToItem(itemId: AboutUsListItemId) {
        _aboutUsItemEvent.value = itemId
    }

}