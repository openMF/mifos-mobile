package org.mifos.mobile.feature.savings.savings_account

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.savings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsAccountDetailTopBar(
    navigateBack: () -> Unit,
    updateSavingsAccount: () -> Unit,
    withdrawSavingsAccount: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier,
        title = { Text(text = stringResource(id = R.string.saving_account_details)) },
        navigationIcon = {
            IconButton(
                onClick = { navigateBack.invoke() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.update_savings_account))
                    },
                    onClick = { updateSavingsAccount.invoke() }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.withdraw_savings_account))
                    },
                    onClick = { withdrawSavingsAccount.invoke() }
                )
            }
        }
    )
}

@Preview
@Composable
fun SavingsAccountDetailTopBarPreview() {
    MifosMobileTheme {
        SavingsAccountDetailTopBar({}, {}, {})
    }
}