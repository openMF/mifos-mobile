/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts

/**
 * This class contains variables which are used for implementing Comparator and are common to loan,
 * savings and share account.
 * Created by dilpreet on 14/6/17.
 */
abstract class Account {
    var id: Long = 0
}
