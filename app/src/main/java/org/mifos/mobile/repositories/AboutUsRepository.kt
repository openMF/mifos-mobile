package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.models.AboutUsItem


interface AboutUsRepository {
    fun getAboutUsItems(): Flow<List<AboutUsItem>>
}


