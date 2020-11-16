package org.mifos.mobile.models.client

/**
 * Created by dilpreet on 10/7/17.
 */

data class Gender(
        var id: Int,
        var name: String? = null,
        var active: Boolean,
        var mandatory: Boolean
)
