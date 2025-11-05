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
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_theme
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_theme_tip
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.icon.MifosIcons

/**
 * A sealed class representing all navigable items available on the settings screen.
 * Each object holds metadata for a specific setting, such as its title, subtitle, icon,
 * and navigation route, making it easy to generate UI components dynamically.
 *
 * This class is serializable to support navigation graph persistence.
 *
 * @property title The string resource for the setting's title.
 * @property subTitle The string resource for the setting's descriptive subtitle.
 * @property icon The vector graphic icon representing the setting.
 * @property route The unique navigation route string for the destination screen.
 */
@Serializable
sealed class SettingsItems(
    @Contextual val title: StringResource,
    @Contextual val subTitle: StringResource,
    @Contextual val icon: ImageVector,
    val route: String,
) {
    // TODO: commenting because we don't have an api for self edit profile
//    @Serializable
//    data object Profile : SettingsItems(
//        title = Res.string.feature_settings_action_profile,
//        subTitle = Res.string.feature_settings_action_profile_tip,
//        icon = MifosIcons.PersonFilled,
//        route = Constants.PROFILE,
//    )

    /** Represents the 'Change Password' setting. */
    @Serializable
    data object Password : SettingsItems(
        title = Res.string.feature_settings_action_password,
        subTitle = Res.string.feature_settings_action_password_tip,
        icon = MifosIcons.PersonPasskey,
        route = Constants.PASSWORD,
    )

    /** Represents the 'Set Passcode' setting for app authentication. */
    @Serializable
    data object AuthPasscode : SettingsItems(
        title = Res.string.feature_settings_action_auth_passcode,
        subTitle = Res.string.feature_settings_action_auth_passcode_tip,
        icon = MifosIcons.TableCellEdit,
        route = Constants.AUTH_PASSCODE,
    )

    /** Represents the 'Language' selection setting. */
    @Serializable
    data object Language : SettingsItems(
        title = Res.string.feature_settings_action_language,
        subTitle = Res.string.feature_settings_action_language_tip,
        icon = MifosIcons.BookLetter,
        route = Constants.LANGUAGE,
    )

//    TODO : uncomment once ui/ux team provide a valid colours for dark theme
    /** Represents the 'Display Theme' setting (e.g., light/dark mode). */
    @Serializable
    data object Theme : SettingsItems(
        title = Res.string.feature_settings_action_theme,
        subTitle = Res.string.feature_settings_action_theme_tip,
        icon = MifosIcons.DarkTheme,
        route = Constants.THEME,
    )
//    TODO: once ui/ux team gives this screen uncomment and implement it
//    @Serializable
//    data object Endpoint : SettingsItems(
//        title = Res.string.feature_settings_action_endpoint,
//        subTitle = Res.string.feature_settings_action_endpoint_tip,
//        icon = MifosIcons.ArchiveSettings,
//        route = Constants.ENDPOINT,
//    )

    /** Represents the 'About Us' information screen. */
    @Serializable
    data object AboutUs : SettingsItems(
        title = Res.string.feature_settings_action_about_us,
        subTitle = Res.string.feature_settings_action_about_us_tip,
        icon = MifosIcons.PeopleCommunity,
        route = Constants.ABOUT_US,
    )

    /** Represents the 'FAQ' (Frequently Asked Questions) screen. */
    @Serializable
    data object FAQ : SettingsItems(
        title = Res.string.feature_settings_action_faq,
        subTitle = Res.string.feature_settings_action_faq_tip,
        icon = MifosIcons.QuestionCircle,
        route = Constants.FAQ,
    )

    /** Represents the 'Help' or support screen. */
    @Serializable
    data object Help : SettingsItems(
        title = Res.string.feature_settings_action_help,
        subTitle = Res.string.feature_settings_action_help_tip,
        icon = MifosIcons.ChatMultiple,
        route = Constants.HELP,
    )

    /** Represents the 'App Info' screen with version and legal details. */
    @Serializable
    data object AppInfo : SettingsItems(
        title = Res.string.feature_settings_action_app_info,
        subTitle = Res.string.feature_settings_action_app_info_tip,
        icon = MifosIcons.AppRecent,
        route = Constants.APP_INFO,
    )

    /** Represents the 'Logout' action. */
    @Serializable
    data object Logout : SettingsItems(
        title = Res.string.feature_settings_action_logout,
        subTitle = Res.string.feature_settings_action_logout_tip,
        icon = MifosIcons.SignOut,
        route = Constants.LOGOUT,
    )
}

/**
 * An immutable list defining the order and content of items displayed on the settings screen.
 * This list is used to dynamically render the settings menu.
 */
internal val settingsItems: ImmutableList<SettingsItems> = persistentListOf(
//    SettingsItems.Profile,
    SettingsItems.Password,
    SettingsItems.AuthPasscode,
    SettingsItems.Language,
    SettingsItems.Theme,
//    SettingsItems.Endpoint,
    SettingsItems.AboutUs,
    SettingsItems.FAQ,
    SettingsItems.Help,
    SettingsItems.AppInfo,
    SettingsItems.Logout,
)
