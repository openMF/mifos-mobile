package org.mifos.mobile.core.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@Composable
fun MifosAlertDialog(
    onDismissRequest: () -> Unit,
    dismissText: String,
    onConfirmation: () -> Unit,
    confirmationText: String,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector? = null
) {
    AlertDialog(
        icon = {
            if(icon != null) { Icon(imageVector = icon, contentDescription = null) }
        },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text(confirmationText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(dismissText)
            }
        }
    )
}

