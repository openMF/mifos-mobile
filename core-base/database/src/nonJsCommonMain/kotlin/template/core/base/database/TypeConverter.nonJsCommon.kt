/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.database

import androidx.room.TypeConverter

/**
 * Type alias for Room's [TypeConverter] annotation in non-JS common code.
 *
 * This type alias maps the Room [TypeConverter] annotation to be used in the non-JS common module.
 * The [TypeConverter] annotation is used to define custom type conversions for Room database entities.
 *
 * @see androidx.room.TypeConverter
 */
actual typealias TypeConverter = TypeConverter
