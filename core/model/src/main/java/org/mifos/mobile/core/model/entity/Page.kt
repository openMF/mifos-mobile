package org.mifos.mobile.core.model.entity

/**
 * @author Rajan Maurya
 */
data class Page<T>(

    var totalFilteredRecords: Int = 0,
    var pageItems: List<T> = ArrayList(),
)
