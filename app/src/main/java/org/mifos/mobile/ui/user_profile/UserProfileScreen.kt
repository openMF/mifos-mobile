package org.mifos.mobile.ui.user_profile

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.component.UserProfileField
import org.mifos.mobile.core.ui.component.UserProfileTopBar

/**
 * @author pratyush
 * @since 20/12/2023
 */

@Composable
fun UserProfileScreen(
    userDetails: UserDetails,
    changePassword: () -> Unit,
    isOnline: Boolean,
    bitmap: Bitmap,
    home: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        UserProfileTopBar(home = home, text = R.string.user_details)

        if (!isOnline) {
            NoInternet(
                icon = R.drawable.ic_error_black_24dp,
                error = R.string.error_fetching_user_profile
            )
            return
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            MifosUserImage(isDarkTheme = isSystemInDarkTheme(), bitmap = bitmap)
        }
        Divider(color = Color(0xFF8E9099))
        userDetails.userName?.let { UserProfileField(label = R.string.username, value = it) }
        userDetails.accountNumber?.let {
            UserProfileField(
                label = R.string.account_number, value = it
            )
        }
        userDetails.activationDate?.let {
            UserProfileField(
                label = R.string.activation_date, value = it
            )
        }
        userDetails.officeName?.let { UserProfileField(label = R.string.office_name, value = it) }
        userDetails.groups?.let { UserProfileField(label = R.string.groups, value = it) }
        userDetails.clientType?.let { UserProfileField(label = R.string.client_type, value = it) }
        userDetails.clientClassification?.let {
            UserProfileField(
                label = R.string.client_classification, value = it
            )
        }
        UserProfileField(text = R.string.change_password,
            icon = R.drawable.ic_keyboard_arrow_right_black_24dp,
            onClick = { changePassword.invoke() })

        UserProfileDetails(userDetails = userDetails)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen(
        UserDetails(),
        {},
        true,
        Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
        {}
    )
}