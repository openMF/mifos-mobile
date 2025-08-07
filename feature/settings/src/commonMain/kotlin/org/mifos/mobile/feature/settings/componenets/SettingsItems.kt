/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.componenets

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_about_us
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_about_us_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_app_info
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_app_info_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_auth_passcode
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_auth_passcode_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_endpoint
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_endpoint_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_faq
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_faq_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_help
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_help_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_language
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_language_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_logout
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_logout_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_password
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_password_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_profile
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_profile_tip
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_theme
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_theme_tip
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.icon.MifosIcons

@Serializable
sealed class SettingsItems(
    @Contextual val title: StringResource,
    @Contextual val subTitle: StringResource,
    @Contextual val icon: ImageVector,
    val route: String,
) {
    @Serializable
    data object Profile : SettingsItems(
        title = Res.string.feature_settings_action_profile,
        subTitle = Res.string.feature_settings_action_profile_tip,
        icon = MifosIcons.PersonFilled,
        route = Constants.PROFILE,
    )

    @Serializable
    data object Password : SettingsItems(
        title = Res.string.feature_settings_action_password,
        subTitle = Res.string.feature_settings_action_password_tip,
        icon = MifosIcons.PersonPasskey,
        route = Constants.PASSWORD,
    )

    @Serializable
    data object AuthPasscode : SettingsItems(
        title = Res.string.feature_settings_action_auth_passcode,
        subTitle = Res.string.feature_settings_action_auth_passcode_tip,
        icon = MifosIcons.TableCellEdit,
        route = Constants.AUTH_PASSCODE,
    )

    @Serializable
    data object Language : SettingsItems(
        title = Res.string.feature_settings_action_language,
        subTitle = Res.string.feature_settings_action_language_tip,
        icon = MifosIcons.BookLetter,
        route = Constants.LANGUAGE,
    )

    @Serializable
    data object Theme : SettingsItems(
        title = Res.string.feature_settings_action_theme,
        subTitle = Res.string.feature_settings_action_theme_tip,
        icon = MifosIcons.DarkTheme,
        route = Constants.THEME,
    )

    @Serializable
    data object Endpoint : SettingsItems(
        title = Res.string.feature_settings_action_endpoint,
        subTitle = Res.string.feature_settings_action_endpoint_tip,
        icon = MifosIcons.ArchiveSettings,
        route = Constants.ENDPOINT,
    )

    @Serializable
    data object AboutUs : SettingsItems(
        title = Res.string.feature_settings_action_about_us,
        subTitle = Res.string.feature_settings_action_about_us_tip,
        icon = MifosIcons.PeopleCommunity,
        route = Constants.ABOUT_US,
    )

    @Serializable
    data object FAQ : SettingsItems(
        title = Res.string.feature_settings_action_faq,
        subTitle = Res.string.feature_settings_action_faq_tip,
        icon = MifosIcons.QuestionCircle,
        route = Constants.FAQ,
    )

    @Serializable
    data object Help : SettingsItems(
        title = Res.string.feature_settings_action_help,
        subTitle = Res.string.feature_settings_action_help_tip,
        icon = MifosIcons.ChatMultiple,
        route = Constants.HELP,
    )

    @Serializable
    data object AppInfo : SettingsItems(
        title = Res.string.feature_settings_action_app_info,
        subTitle = Res.string.feature_settings_action_app_info_tip,
        icon = MifosIcons.AppRecent,
        route = Constants.APP_INFO,
    )

    @Serializable
    data object Logout : SettingsItems(
        title = Res.string.feature_settings_action_logout,
        subTitle = Res.string.feature_settings_action_logout_tip,
        icon = MifosIcons.SignOut,
        route = Constants.LOGOUT,
    )
}

internal val settingsItems: ImmutableList<SettingsItems> = persistentListOf(
    SettingsItems.Profile,
    SettingsItems.Password,
    SettingsItems.AuthPasscode,
    SettingsItems.Language,
    SettingsItems.Theme,
    SettingsItems.Endpoint,
    SettingsItems.AboutUs,
    SettingsItems.FAQ,
    SettingsItems.Help,
    SettingsItems.AppInfo,
    SettingsItems.Logout,
)
