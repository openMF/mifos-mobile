package org.mifos.mobile.ui.guarantor.guarantor_details

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuarantorDetailTopBar(
    navigateBack: () -> Unit,
    updateGuarantor: () -> Unit,
    deleteGuarantor: () -> Unit
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier,
        title = { Text(text = stringResource(id = R.string.guarantor_details)) },
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
                    text = { Text(text = stringResource(id = R.string.update_guarantor)) },
                    onClick = {
                        showMenu = false
                        updateGuarantor.invoke()
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.delete_guarantor)) },
                    onClick = {
                        showMenu = false
                        deleteGuarantor.invoke()
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun GuarantorDetailTopBarPreview() {
    MifosMobileTheme {
        GuarantorDetailTopBar({}, {}, {})
    }
}