package org.mifos.mobile.utils

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference
import org.mifos.mobile.api.local.PreferencesHelper

/**
 * Created by dilpreet on 11/03/18.
 */
class ConfigurationPreference : DialogPreference {
    private var preferencesHelper: PreferencesHelper? = null

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    private fun init() {
        isPersistent = false
        preferencesHelper = PreferencesHelper(context)
    }

    val baseUrl: String?
        get() = preferencesHelper?.baseUrl

    fun updateConfigurations(baseUrl: String?, tenant: String?) {
        preferencesHelper?.updateConfiguration(baseUrl, tenant)
    }

    val tenant: String?
        get() = preferencesHelper?.tenant
}