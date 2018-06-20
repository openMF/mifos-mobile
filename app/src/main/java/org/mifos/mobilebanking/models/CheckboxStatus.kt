package org.mifos.mobilebanking.models

/**
 * Created by dilpreet on 3/7/17.
 */

data class CheckboxStatus @JvmOverloads constructor(
        var status: String?,
        var color: Int,
        var isChecked: Boolean = false
)
