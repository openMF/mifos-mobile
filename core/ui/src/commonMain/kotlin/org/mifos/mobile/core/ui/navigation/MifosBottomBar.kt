
package org.mifos.mobile.core.ui.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken

@Composable
fun MifosBottomBar(
    navigationItems: List<NavigationItem>,
    selectedItem: NavigationItem?,
    onClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
) {
    BottomAppBar(
        containerColor = AppColors.customWhite,
        contentColor = Color.Unspecified,
        windowInsets = windowInsets,
        modifier = modifier
            .fillMaxWidth()
//            .shadow(
//                elevation = DesignToken.elevation.elevation,
//                spotColor = Color(0xFF5D5D5D),
//                ambientColor = Color.Black,
//            )
            .background(AppColors.customWhite),
        tonalElevation = 0.dp,
    ) {
        navigationItems.forEach { navigationItem ->
            MifosNavigationBarItem(
                contentDescriptionRes = navigationItem.contentDescriptionRes,
                selectedIconRes = navigationItem.iconResSelected,
                unselectedIconRes = navigationItem.iconRes,
                label = navigationItem.labelRes,
                isSelected = selectedItem == navigationItem,
                onClick = { onClick(navigationItem) },
                modifier = Modifier.testTag(tag = navigationItem.testTag),
            )
        }

    }
}
