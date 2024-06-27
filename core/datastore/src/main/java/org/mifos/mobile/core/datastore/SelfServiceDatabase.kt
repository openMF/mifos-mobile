package org.mifos.mobile.core.datastore

import com.raizlabs.android.dbflow.annotation.Database


@Database(name = SelfServiceDatabase.NAME, version = SelfServiceDatabase.VERSION)
object SelfServiceDatabase {
    const val NAME: String = "SelfService" // we will add the .db extension
    const val VERSION: Int = 1
}
