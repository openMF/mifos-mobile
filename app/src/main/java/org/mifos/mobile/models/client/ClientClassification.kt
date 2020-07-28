package org.mifos.mobile.models.client

/**
 * Created by dilpreet on 10/7/17.
 */

data class ClientClassification(
        var id: Int,
        var name: String,
        var active: Boolean,
        var mandatory: Boolean
)
