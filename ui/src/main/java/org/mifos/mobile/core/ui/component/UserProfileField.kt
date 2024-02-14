package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author pratyush
 * @since 20/12/2023
 */

@Composable
fun UserProfileField(
    text: Int, icon: Int, onClick: (() -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = text),
            color = Color(0xFF8E9099),
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        )
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
        )
    }
    Divider(color = if (isSystemInDarkTheme()) Color(0xFF8E9099) else Color.Black)
}

@Composable
fun UserProfileField(
    label: Int, value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = label),
            color = Color(0xFF8E9099),
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = value,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            style = TextStyle(fontSize = 14.sp),
        )
    }
    Divider(color = Color(0xFF8E9099))
}