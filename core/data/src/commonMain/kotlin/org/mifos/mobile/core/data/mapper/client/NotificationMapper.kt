package org.mifos.mobile.core.data.mapper.client

import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail
import org.mifos.mobile.core.network.dto.notification.NotificationUserDetailResponseDto

fun NotificationUserDetailResponseDto.toModel() : NotificationUserDetail =
    NotificationUserDetail(
        id = id,
    )