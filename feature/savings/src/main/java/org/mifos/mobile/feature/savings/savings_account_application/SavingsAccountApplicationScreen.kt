package org.mifos.mobile.feature.savings.savings_account_application

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosTopBar
import org.mifos.mobile.core.ui.component.NoInternet
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.feature.savings.R


@Composable
fun SavingsAccountApplicationScreen(
    viewModel: SavingsAccountApplicationViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    submit: (Int, Int, showToast: (Int) -> Unit) -> Unit,
) {
    val uiState by viewModel.savingsAccountApplicationUiState.collectAsStateWithLifecycle()

    SavingsAccountApplicationScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        submit = submit
    )
}

@Composable
fun SavingsAccountApplicationScreen(
    uiState: SavingsAccountApplicationUiState,
    savingsWithAssociations: SavingsWithAssociations? = null,
    navigateBack: () -> Unit,
    submit: (Int, Int, showToast: (Int) -> Unit) -> Unit,
) {
    var topBarTitleText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    MFScaffold(
        topBar = {
            MifosTopBar(
                navigateBack = navigateBack,
                title = { Text(text = topBarTitleText) }
            )
        },
        scaffoldContent = {
            Box(modifier = Modifier.padding(it)) {
                when (uiState) {
                    is SavingsAccountApplicationUiState.Error -> {
                        MifosErrorComponent()
                    }

                    is SavingsAccountApplicationUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
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
                }

            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun SavingsAccountApplicationScreenPreview() {
    MifosMobileTheme {
        SavingsAccountApplicationScreen(SavingsAccountApplicationUiState.Success(requestType = SavingsAccountState.UPDATE), null, {}, { i, j, k -> })
    }
}

