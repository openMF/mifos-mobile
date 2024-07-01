package org.mifos.mobile.feature.savings.savings_account_application

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.feature.savings.R


@Composable
fun SavingsAccountApplicationScreen(
    uiState: SavingsAccountApplicationUiState? = null,
    savingsWithAssociations: SavingsWithAssociations? = null,
    navigateBack: () -> Unit,
    submit: (Int, Int, showToast: (Int) -> Unit) -> Unit,
    retryConnection: () -> Unit,
) {
    var topBarTitleText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Column {
        MifosTopBar(
            navigateBack = navigateBack,
            title = { Text(text = topBarTitleText) }
        )

        when (uiState) {
            is SavingsAccountApplicationUiState.Error -> {
                ErrorComponent(retryConnection = retryConnection, errorMessage = uiState.errorMessage)
            }

            is SavingsAccountApplicationUiState.Loading -> {
                MifosProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            is SavingsAccountApplicationUiState.ShowUserInterface -> {
                val (titleResourceId, existingProduct) = when (uiState.requestType) {
                    SavingsAccountState.CREATE -> R.string.apply_savings_account to null
                    else -> R.string.update_savings_account to savingsWithAssociations?.savingsProductName
                }

                topBarTitleText = stringResource(id = titleResourceId)
                SavingsAccountApplicationContent(
                    existingProduct = existingProduct,
                    savingsAccountTemplate = uiState.template,
                    submit = submit
                )
            }

            is SavingsAccountApplicationUiState.Success -> {
                val messageResourceId = when (uiState.requestType) {
                    SavingsAccountState.CREATE -> R.string.new_saving_account_created_successfully
                    else -> R.string.saving_account_updated_successfully
                }
                Toast.makeText(context, stringResource(id = messageResourceId), Toast.LENGTH_SHORT).show()
                navigateBack.invoke()
            }

            else -> {}
        }
    }
}

@Composable
fun ErrorComponent(retryConnection: () -> Unit, errorMessage: String?) {
    val context = LocalContext.current
    if (!Network.isConnected(context)) {
        NoInternet(
            error = R.string.no_internet_connection,
            isRetryEnabled = true,
            retry = retryConnection
        )
        Toast.makeText(context, stringResource(R.string.internet_not_connected), Toast.LENGTH_SHORT,).show()
    } else {
        EmptyDataView(
            modifier= Modifier.fillMaxSize(),
            error = R.string.error_saving_account_details_loading,
            errorString = errorMessage
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SavingsAccountApplicationScreenPreview() {
    MifosMobileTheme {
        SavingsAccountApplicationScreen(null, null, {}, { i, j, k -> }, {})
    }
}

