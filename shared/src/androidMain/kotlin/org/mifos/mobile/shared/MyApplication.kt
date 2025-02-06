package org.mifos.mobile.shared

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.mifos.mobile.shared.di.initKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
    }
}