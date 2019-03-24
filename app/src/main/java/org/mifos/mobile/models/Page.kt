package org.mifos.mobile.models

import java.util.ArrayList

/**
 * @author Rajan Maurya
 */
data class Page<T> (

    var totalFilteredRecords: Int = 0,
    var pageItems: List<T> = ArrayList())