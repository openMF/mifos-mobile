package org.mifos.mobile.feature.home.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.home.viewmodel.HomeNavigationItems

@Composable
fun HomeNavigationDrawer(
    userBitmap: Bitmap?,
    username: String,
    navigateItem: (HomeNavigationItems) -> Unit,
    content: @Composable () -> Unit,
    drawerState: DrawerState,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        content = content,
        drawerContent = {
            ModalDrawerSheet {
                LazyColumn {
                    item {
                        MifosUserImage(
                            modifier = Modifier
                                .padding(20.dp)
                                .size(84.dp),
                            bitmap = userBitmap,
                            username = username
                        )
                        Text(
                            text = username,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(1f)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(
                        items = HomeNavigationItems.entries.toTypedArray(),
                        itemContent = { item ->
                            Spacer(modifier = Modifier.height(12.dp))
                            NavigationDrawerItem(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                label = {
                                    Row {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = item.iconResId),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = stringResource(id = item.nameResId))
                                    }
                                },
                                selected = item == HomeNavigationItems.Home,
                                onClick = { navigateItem(item) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            if(item == HomeNavigationItems.ManageBeneficiaries) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                )
                            }
                        }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeNavigationDrawerPreview() {
    MifosMobileTheme {
        HomeNavigationDrawer(
            userBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
            username = "Avneet",
            content = {},
            navigateItem = {},
            drawerState = DrawerState(initialValue = DrawerValue.Open),
        )
    }
}