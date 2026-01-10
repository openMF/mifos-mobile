package org.mifos.mobile.core.data.mapper

import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.network.dto.common.PageResponseDto

fun <Dto, Model> PageResponseDto<Dto>.toPageModel(
    mapper: (Dto) -> Model
): Page<Model> =
    Page(
        totalFilteredRecords = totalFilteredRecords,
        pageItems = pageItems.map(mapper)
    )