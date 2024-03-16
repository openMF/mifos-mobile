package org.mifos.mobile.ui.beneficiary.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

/***
 * this is a reusable top App bar component composable that takes in parameters like
 * @param[navigateBack] to take action when the navigate backs clicked
 * @param[startIcon] to insert an image vector of your choice
 * */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryTopappbar(
    navigateBack: () -> Unit,
    startIcon: ImageVector = Icons.Filled.ArrowBack,

    ) {

    TopAppBar(
        modifier = Modifier,
        title = {
            Text(text = stringResource(id = R.string.add_beneficiary),
                modifier = Modifier.padding(7.dp))
        },
        navigationIcon = {
            IconButton(
                onClick = { navigateBack.invoke() }
            ) {
                Icon(
                    imageVector = startIcon,
                    contentDescription = "Back Arrow",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable(onClick = navigateBack)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),

        )
}

@Preview
@Composable
fun LoanAccountDetailTopBarPreview() {
    MifosMobileTheme {
        BeneficiaryTopappbar({},)
    }
}