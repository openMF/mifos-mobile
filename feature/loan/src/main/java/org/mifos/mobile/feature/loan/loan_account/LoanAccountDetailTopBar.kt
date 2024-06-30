package org.mifos.mobile.feature.loan.loan_account

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.loan.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanAccountDetailTopBar(
    navigateBack: () -> Unit,
    viewGuarantor: () -> Unit,
    updateLoan: () -> Unit,
    withdrawLoan: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier,
        title = { Text(text = stringResource(id = R.string.loan_account_details)) },
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
                modifier = Modifier.padding(start = 16.dp, end = 32.dp),
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.view_guarantor))
                    },
                    onClick = { viewGuarantor.invoke() }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.update_loan))
                    },
                    onClick = { updateLoan.invoke() }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.withdraw_loan))
                    },
                    onClick = { withdrawLoan.invoke() }
                )
            }
        }
    )
}

@Preview
@Composable
fun LoanAccountDetailTopBarPreview() {
    MifosMobileTheme {
        LoanAccountDetailTopBar({},{},{},{})
    }
}