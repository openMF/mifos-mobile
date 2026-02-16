/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.client

import org.mifos.mobile.core.model.entity.Timeline
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientClassification
import org.mifos.mobile.core.model.entity.client.ClientType
import org.mifos.mobile.core.model.entity.client.Gender
import org.mifos.mobile.core.model.entity.client.Group
import org.mifos.mobile.core.model.entity.client.Status
import org.mifos.mobile.core.network.dto.client.ClientClassificationResponseDto
import org.mifos.mobile.core.network.dto.client.ClientResponseDto
import org.mifos.mobile.core.network.dto.client.ClientTypeResponseDto
import org.mifos.mobile.core.network.dto.client.GenderResponseDto
import org.mifos.mobile.core.network.dto.client.GroupResponseDto
import org.mifos.mobile.core.network.dto.client.StatusResponseDto
import org.mifos.mobile.core.network.dto.client.TimelineResponseDto

fun ClientResponseDto.toModel(): Client =
    Client(
        id = id,
        accountNo = accountNo,
        status = status?.toModel(),
        active = active,
        activationDate = activationDate,
        dobDate = dobDate,
        firstname = firstname,
        middlename = middlename,
        lastname = lastname,
        displayName = displayName,
        fullname = fullname,
        officeId = officeId,
        officeName = officeName,
        staffId = staffId,
        staffName = staffName,
        timeline = timeline?.toModel(),
        imageId = imageId,
        isImagePresent = isImagePresent,
        externalId = externalId,
        mobileNo = mobileNo,
        clientClassification = clientClassification?.toModel(),
        clientType = clientType?.toModel(),
        gender = gender?.toModel(),
        groups = groups.map { it.toModel() },
    )

fun StatusResponseDto.toModel(): Status =
    Status(
        id = id,
        code = code,
        value = value,
    )

fun TimelineResponseDto.toModel(): Timeline =
    Timeline(
        submittedOnDate = submittedOnDate,
        submittedByUsername = submittedByUsername,
        submittedByFirstname = submittedByFirstname,
        submittedByLastname = submittedByLastname,
        activatedOnDate = activatedOnDate,
        activatedByUsername = activatedByUsername,
        activatedByFirstname = activatedByFirstname,
        activatedByLastname = activatedByLastname,
        closedOnDate = closedOnDate,
        closedByUsername = closedByUsername,
        closedByFirstname = closedByFirstname,
        closedByLastname = closedByLastname,
    )

fun ClientClassificationResponseDto.toModel(): ClientClassification =
    ClientClassification(
        id = id,
        name = name,
        active = active ?: false,
        mandatory = mandatory ?: false,
    )

fun ClientTypeResponseDto.toModel(): ClientType =
    ClientType(
        id = id,
        name = name,
        active = active ?: false,
        mandatory = mandatory ?: false,
    )

fun GenderResponseDto.toModel(): Gender =
    Gender(
        id = id,
        name = name,
        active = active ?: false,
        mandatory = mandatory ?: false,
    )

fun GroupResponseDto.toModel(): Group =
    Group(
        id = id,
        accountNo = accountNo,
        name = name,
    )
