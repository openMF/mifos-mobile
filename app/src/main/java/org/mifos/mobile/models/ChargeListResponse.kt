package org.mifos.mobile.models

import java.util.*

/**
 * @author Vishwajeet
 * @since 16/8/16.
 */
data class ChargeListResponse (
    var pageItems: List<Charge> = ArrayList()
)
