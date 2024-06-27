package org.mifos.mobile.core.datastore.model

import org.mifos.mobile.core.datastore.model.Charge

/**
 * @author Vishwajeet
 * @since 16/8/16.
 */
data class ChargeListResponse(
    var pageItems: List<Charge> = ArrayList(),
)
