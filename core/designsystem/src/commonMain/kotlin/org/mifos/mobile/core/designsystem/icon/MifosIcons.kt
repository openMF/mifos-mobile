/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RealEstateAgent
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import fluent.ui.system.icons.FluentIcons
import fluent.ui.system.icons.colored.AddCircle
import fluent.ui.system.icons.colored.Alert
import fluent.ui.system.icons.colored.CoinMultiple
import fluent.ui.system.icons.colored.Savings
import fluent.ui.system.icons.colored.Warning
import fluent.ui.system.icons.filled.AppRecent
import fluent.ui.system.icons.filled.ArchiveSettings
import fluent.ui.system.icons.filled.ArrowDownload
import fluent.ui.system.icons.filled.BookLetter
import fluent.ui.system.icons.filled.CaretDown
import fluent.ui.system.icons.filled.CaretUp
import fluent.ui.system.icons.filled.ChatBubblesQuestion
import fluent.ui.system.icons.filled.ChatHistory
import fluent.ui.system.icons.filled.ChatMultiple
import fluent.ui.system.icons.filled.CheckmarkCircle
import fluent.ui.system.icons.filled.ChevronRight
import fluent.ui.system.icons.filled.CoinMultiple
import fluent.ui.system.icons.filled.ContactCardRibbon
import fluent.ui.system.icons.filled.DarkTheme
import fluent.ui.system.icons.filled.DataHistogram
import fluent.ui.system.icons.filled.DataWhisker
import fluent.ui.system.icons.filled.Delete
import fluent.ui.system.icons.filled.Document
import fluent.ui.system.icons.filled.DrawerAdd
import fluent.ui.system.icons.filled.DrawerSubtract
import fluent.ui.system.icons.filled.ErrorCircle
import fluent.ui.system.icons.filled.Eye
import fluent.ui.system.icons.filled.Feed
import fluent.ui.system.icons.filled.Grid
import fluent.ui.system.icons.filled.LockClosed
import fluent.ui.system.icons.filled.Money
import fluent.ui.system.icons.filled.MoneyHand
import fluent.ui.system.icons.filled.PeopleCommunity
import fluent.ui.system.icons.filled.Person
import fluent.ui.system.icons.filled.PersonAccounts
import fluent.ui.system.icons.filled.PersonPasskey
import fluent.ui.system.icons.filled.QrCode
import fluent.ui.system.icons.filled.QuestionCircle
import fluent.ui.system.icons.filled.Receipt
import fluent.ui.system.icons.filled.ReceiptMoney
import fluent.ui.system.icons.filled.Savings
import fluent.ui.system.icons.filled.SignOut
import fluent.ui.system.icons.filled.TableCellEdit
import fluent.ui.system.icons.filled.Wallet
import fluent.ui.system.icons.filled.WifiOff
import fluent.ui.system.icons.regular.Alert
import fluent.ui.system.icons.regular.ArrowCounterclockwise
import fluent.ui.system.icons.regular.ArrowExportUp
import fluent.ui.system.icons.regular.Attach
import fluent.ui.system.icons.regular.Backspace
import fluent.ui.system.icons.regular.Calendar
import fluent.ui.system.icons.regular.Camera
import fluent.ui.system.icons.regular.CardUi
import fluent.ui.system.icons.regular.Checkmark
import fluent.ui.system.icons.regular.CheckmarkCircle
import fluent.ui.system.icons.regular.ChevronDown
import fluent.ui.system.icons.regular.ChevronLeft
import fluent.ui.system.icons.regular.ChevronUp
import fluent.ui.system.icons.regular.Dismiss
import fluent.ui.system.icons.regular.DocumentGlobe
import fluent.ui.system.icons.regular.DrawShape
import fluent.ui.system.icons.regular.Edit
import fluent.ui.system.icons.regular.Eye
import fluent.ui.system.icons.regular.EyeOff
import fluent.ui.system.icons.regular.Filter
import fluent.ui.system.icons.regular.Image
import fluent.ui.system.icons.regular.Info
import fluent.ui.system.icons.regular.Receipt
import fluent.ui.system.icons.regular.Search
import fluent.ui.system.icons.regular.Send

object MifosIcons {
    val Paid: ImageVector = Icons.Default.Paid
    val Logout: ImageVector = Icons.AutoMirrored.Filled.Logout
    val Help: ImageVector = Icons.AutoMirrored.Filled.Help
    val Settings: ImageVector = Icons.Default.Settings
    val Label: ImageVector = Icons.AutoMirrored.Filled.Label
    val Assignment: ImageVector = Icons.AutoMirrored.Filled.Assignment
    val People: ImageVector = Icons.Filled.People
    val RealEstateAgent: ImageVector = Icons.Filled.RealEstateAgent
    val AccountBalanceWallet: ImageVector = Icons.Filled.AccountBalanceWallet
    val CompareArrows: ImageVector = Icons.AutoMirrored.Filled.CompareArrows
    val AccountBalance: ImageVector = Icons.Filled.AccountBalance
    val Share: ImageVector = Icons.Default.Share
    val Mail: ImageVector = Icons.Outlined.Mail
    val LocationOn: ImageVector = Icons.Filled.LocationOn
    val Phone: ImageVector = Icons.Default.Phone
    val MoreVert: ImageVector = Icons.Filled.MoreVert
    val VisibilityOff: ImageVector = Icons.Filled.VisibilityOff
    val Visibility: ImageVector = Icons.Filled.Visibility
    val Info: ImageVector = Icons.Default.Info
    val ArrowDropUp: ImageVector = Icons.Default.ArrowDropUp
    val ArrowDropDown: ImageVector = Icons.Default.ArrowDropDown
    val Close: ImageVector = Icons.Filled.Close
    val OutlinedVisibilityOff: ImageVector = Icons.Outlined.VisibilityOff
    val OutlinedVisibility: ImageVector = Icons.Outlined.Visibility
    val ArrowBack = Icons.AutoMirrored.Default.ArrowBack
    val Edit = Icons.Default.Edit
    val FilterList = Icons.Filled.FilterList
    val FlashOn = Icons.Default.FlashOn
    val FlashOff = Icons.Default.FlashOff
    val Add = Icons.Filled.Add
    val Search = Icons.Filled.Search
    val Error = Icons.Filled.Error
    val Notifications = Icons.Filled.Notifications
    val NavigationDrawer = Icons.Default.Menu

    val WifiOff = FluentIcons.Filled.WifiOff

    // Recently added
    val Back = Icons.AutoMirrored.Outlined.ArrowBack
    val Home = Icons.Outlined.Home
    val HomeBoarder = Icons.Rounded.Home
    val Payment = Icons.Rounded.SwapHoriz
    val Finance = Icons.Outlined.Wallet
    val Profile = Icons.Outlined.AccountCircle
    val ProfileBoarder = Icons.Rounded.AccountCircle

    val UploadId = FluentIcons.Regular.CardUi
    val Image = FluentIcons.Regular.Image

    val Eye = FluentIcons.Regular.Eye
    val EyeOff = FluentIcons.Regular.EyeOff
    val EyeFilled = FluentIcons.Filled.Eye
    val ErrorCircle = FluentIcons.Filled.ErrorCircle
    val CheckCircle = FluentIcons.Regular.CheckmarkCircle
    val OutlinedInfo = FluentIcons.Regular.Info

    val DocumentFilled = FluentIcons.Filled.Document
    val Calendar = FluentIcons.Regular.Calendar

    val HomeTabFilled = FluentIcons.Filled.Grid
    val TransferTabFilled = FluentIcons.Filled.MoneyHand
    val PersonTabFilled = FluentIcons.Filled.Person

    val Chevron = FluentIcons.Regular.ChevronLeft
    val ChevronRight = FluentIcons.Filled.ChevronRight

    val Alert = FluentIcons.Regular.Alert
    val SearchNew = FluentIcons.Regular.Search

    val SavingsAccount = FluentIcons.Filled.Wallet
    val SavingsAccountColor = FluentIcons.Colored.Savings
    val LoanAccount = FluentIcons.Filled.CoinMultiple
    val LoanAccountColor = FluentIcons.Colored.CoinMultiple
    val ShareAccount = FluentIcons.Filled.DataWhisker
    val ApplyForLoan = FluentIcons.Filled.Receipt
    val ApplyForSavings = FluentIcons.Filled.Savings
    val ApplyForShare = FluentIcons.Filled.DataHistogram
    val TransactionHistory = FluentIcons.Filled.ChatHistory
    val Charges = FluentIcons.Filled.Feed
    val Beneficiary = FluentIcons.Filled.ContactCardRibbon
    val Faq = FluentIcons.Filled.ChatBubblesQuestion

    val LockFilled = FluentIcons.Filled.LockClosed
    val Send = FluentIcons.Regular.Send
    val Backspace = FluentIcons.Regular.Backspace

    val Filter = FluentIcons.Regular.Filter
    val PersonAccounts = FluentIcons.Filled.PersonAccounts

    val Dismiss = FluentIcons.Regular.Dismiss

    val ArrowCounterClockWise = FluentIcons.Regular.ArrowCounterclockwise
    val ChevronUp = FluentIcons.Regular.ChevronUp
    val ChevronDown = FluentIcons.Regular.ChevronDown
    val CheckMark = FluentIcons.Regular.Checkmark

    val DrawerAdd = FluentIcons.Filled.DrawerAdd
    val DrawerSubtract = FluentIcons.Filled.DrawerSubtract

    val EditRegular = FluentIcons.Regular.Edit
    val ArrowExport = FluentIcons.Regular.ArrowExportUp
    val Money = FluentIcons.Filled.Money
    val MoneyHand = FluentIcons.Filled.MoneyHand
    val ChatHistory = FluentIcons.Filled.ChatHistory
    val ReceiptMoney = FluentIcons.Filled.ReceiptMoney
    val QrCode = FluentIcons.Filled.QrCode
    val Download = FluentIcons.Filled.ArrowDownload
    val CaretDown = FluentIcons.Filled.CaretDown
    val CaretUp = FluentIcons.Filled.CaretUp

    val CoinMultiple = FluentIcons.Filled.CoinMultiple

    val Warning = FluentIcons.Colored.Warning
    val Delete = FluentIcons.Filled.Delete
    val Notification = FluentIcons.Colored.Alert

//    Settings Icons

    val PersonFilled = FluentIcons.Filled.Person
    val PersonPasskey = FluentIcons.Filled.PersonPasskey
    val TableCellEdit = FluentIcons.Filled.TableCellEdit
    val BookLetter = FluentIcons.Filled.BookLetter
    val DarkTheme = FluentIcons.Filled.DarkTheme
    val ArchiveSettings = FluentIcons.Filled.ArchiveSettings
    val PeopleCommunity = FluentIcons.Filled.PeopleCommunity
    val QuestionCircle = FluentIcons.Filled.QuestionCircle
    val ChatMultiple = FluentIcons.Filled.ChatMultiple
    val AppRecent = FluentIcons.Filled.AppRecent

    val RateUs = Icons.Filled.Star
    val SignOut = FluentIcons.Filled.SignOut

    val Receipt = FluentIcons.Regular.Receipt
    val DocumentGlobe = FluentIcons.Regular.DocumentGlobe
    val Signature = FluentIcons.Regular.DrawShape
    val Camera = FluentIcons.Regular.Camera
    val Attach = FluentIcons.Regular.Attach

    val AddColor = FluentIcons.Colored.AddCircle

    val GridApps = FluentIcons.Filled.Grid
    val CheckCircle1 = FluentIcons.Filled.CheckmarkCircle
    val Pencil = Icons.Filled.Edit
    val Export = Icons.Default.IosShare
}
